package com.vergilyn.examples.usage.u0002;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConcurrentTest {

	@Test
	@SneakyThrows
	public void test() {
		int size = 1_000_000;

		AtomicInteger count = new AtomicInteger(0);

		FlushHandler<LogInfo> handler = data -> {
			System.out.println(JSON.toJSONString(data));
			count.addAndGet(data.size());
			return true;
		};

		FullIntervalHandlerTemplate<LogInfo> consumer = new FullIntervalHandlerTemplateBuilder<>(LogInfo.class)
				.handler(handler).interval(2, TimeUnit.SECONDS).threshold(50).build();

		ExecutorService pool = Executors.newFixedThreadPool(100);

		List<Future<?>> futures = Lists.newArrayListWithCapacity(size);
		for (int i = 0; i < size; i++) {
			LogInfo logInfo = new LogInfo(i, null);
			Future<?> future = pool.submit(() -> {
				consumer.add(logInfo);
			});

			futures.add(future);
		}

		futures.forEach(future -> {
			try {
				future.get();
			} catch (Exception e) {
			}
		});

		TimeUnit.SECONDS.sleep(10);

		System.out.println("[junit] >>>> futures.size: " + futures.size());
		System.out.println("[junit] >>>> datas.size: " + count);
		Assertions.assertThat(count.get()).isEqualTo(size);
//		System.out.println(expected);

	}
}
