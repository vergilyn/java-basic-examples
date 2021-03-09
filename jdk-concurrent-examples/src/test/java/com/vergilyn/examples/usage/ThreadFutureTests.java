package com.vergilyn.examples.usage;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-03-05
 */
public class ThreadFutureTests {


	@Test
	public void test(){
		AtomicInteger sum = new AtomicInteger(0);
		int size = 5;
		ExecutorService executorService = Executors.newFixedThreadPool(size);
		List<Future<?>> futures = Lists.newArrayListWithCapacity(size);


		for (int i = 0; i < size; i++) {

			Future<?> future = executorService.submit(() -> {
				sum.incrementAndGet();
				try {
					int s = RandomUtils.nextInt(0, 10);
					TimeUnit.SECONDS.sleep(s);
					System.out.println(s);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			futures.add(future);
		}

		futures.forEach(future -> {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		});

		System.out.println(sum.get());
	}
}
