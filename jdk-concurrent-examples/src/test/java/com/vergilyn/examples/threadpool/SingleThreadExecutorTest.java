package com.vergilyn.examples.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 *
 * TODO 2021-11-22
 * 单线程异步执行，当存在多个任务时，直接舍弃后续任务。
 *
 * @author vergilyn
 * @since 2021-11-22
 */
public class SingleThreadExecutorTest {

	/**
	 * 3个任务都会执行，期望是：直接舍弃后续任务。
	 *
	 * @see Executors#newSingleThreadExecutor()
	 */
	@SneakyThrows
	@Test
	public void error(){
		ExecutorService executor = Executors.newSingleThreadExecutor();

		Future<?> future1 = executor.submit(new SingleThreadTask());
		Future<?> future2 = executor.submit(new SingleThreadTask());
		Future<?> future3 = executor.submit(new SingleThreadTask());

		Object o1 = future1.get();
		Object o2 = future2.get();
		Object o3 = future3.get();

	}

	/**
	 * 1&2 会执行， 3被静默舍弃 discard。
	 * 无法设置 `queue = 0`，最少都是 `queue >= 1`
	 */
	@SneakyThrows
	@Test
	public void right(){
		RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();

		ExecutorService executor = new ThreadPoolExecutor(1, 1,
		                                                  0L, TimeUnit.MILLISECONDS,
		                                                  new LinkedBlockingQueue<Runnable>(1),
		                                                  Executors.defaultThreadFactory(),
		                                                  rejectedExecutionHandler
		                                                  );

		Future<?> future1 = executor.submit(new SingleThreadTask());
		Future<?> future2 = executor.submit(new SingleThreadTask());
		Future<?> future3 = executor.submit(new SingleThreadTask());

		Object o1 = future1.get();
		Object o2 = future2.get();
		Object o3 = future2.get();

	}

	@Slf4j
	private static class SingleThreadTask implements Runnable{
		private static final AtomicInteger COUNT = new AtomicInteger(0);

		@SneakyThrows
		@Override
		public void run() {
			String logPrefix = String.format("[%s][%s]", Thread.currentThread().getName(), COUNT.incrementAndGet());

			System.out.printf("%s running... \n", logPrefix);

			TimeUnit.SECONDS.sleep(2);

			System.out.printf("%s end... \n", logPrefix);
		}
	}
}
