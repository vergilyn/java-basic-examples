package com.vergilyn.examples.usage.u0002;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class FullIntervalHandlerTemplateTest {

	FlushHandler<LogInfo> handler = data -> {
		System.out.printf("%s >>>> %s \n", LocalTime.now(), JSON.toJSONString(data));
		return true;
	};

	@SneakyThrows
	@Test
	public void interval(){
		FullIntervalHandlerTemplate<LogInfo> consumer = new FullIntervalHandlerTemplateBuilder<>(LogInfo.class)
										.handler(handler)
										.interval(2, TimeUnit.SECONDS)
										.build();

		consumer.add(new LogInfo(1, LocalTime.now()));

	}

	@SneakyThrows
	@Test
	public void threshold(){
		FullIntervalHandlerTemplate<LogInfo> consumer = new FullIntervalHandlerTemplateBuilder<>(LogInfo.class)
										.handler(handler)
										.interval(2, TimeUnit.SECONDS)
										.threshold(2)
										.build();

		consumer.add(new LogInfo(1, LocalTime.now()));
		consumer.add(new LogInfo(2, LocalTime.now()));
		consumer.add(new LogInfo(3, LocalTime.now()));

	}

	/**
	 * 正常关闭时，通过 {@linkplain Runtime#addShutdownHook(Thread)} 尽量保证执行完成
	 */
	@SneakyThrows
	@Test
	public void awaitFlushComplete(){
		FlushHandler<LogInfo> handler = data -> {
			try {
				TimeUnit.SECONDS.sleep(data.size());
				System.out.printf("%s >>>> %s \n", LocalTime.now(), JSON.toJSONString(data));
			} catch (InterruptedException e) {
			}
			return true;
		};

		FullIntervalHandlerTemplate<LogInfo> consumer = new FullIntervalHandlerTemplateBuilder<>(LogInfo.class)
															.handler(handler)
															.interval(200, TimeUnit.SECONDS)
															.threshold(2)
															.build();

		consumer.add(new LogInfo(1, LocalTime.now()));
		consumer.add(new LogInfo(2, LocalTime.now()));  // trigger flush
		consumer.add(new LogInfo(3, LocalTime.now()));

		// manual trigger shutdown-hook
		System.exit(-1);
	}
}