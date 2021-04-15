package com.vergilyn.examples.threadpool;

import java.time.LocalTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
public class ScheduledThreadPoolExecutorTests {


	@SneakyThrows
	@Test
	public void test(){
		ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(2);

		// 只会执行 1次
		scheduledExecutor.submit(() -> log.info("[vergilyn] >>>> 111111"));
		scheduledExecutor.submit(() -> log.info("[vergilyn] >>>> 222222"));

		// 每间隔 1s 执行1次
		scheduledExecutor.scheduleAtFixedRate(() -> log.info("[vergilyn] >>>> 333333"),
								0, 1, SECONDS);

		new Semaphore(0).acquire();
	}

	/**
	 * 需求：有一个task，间隔xx执行一次，当触发某个条件后，该task不再执行。<br/>
	 *   1) {@linkplain ScheduledFuture#cancel(boolean)}，能达到目的，但相对不优雅。<br/>
	 *   2)
	 */
	@SneakyThrows
	@Test
	@Deprecated
	public void badCloseSomeTask(){
		ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(2);

		ArrayBlockingQueue<Boolean> queue = new ArrayBlockingQueue<>(1);
		int max = 5;
		AtomicInteger count = new AtomicInteger(0);

		// 这种cancel不够优雅。
		ScheduledFuture<?> scheduledFuture = scheduledExecutor.scheduleAtFixedRate(() -> {
			long index = count.getAndIncrement();
			System.out.printf("[vergilyn][%d] >>>> %s \n", index, LocalTime.now());
			if (index > max){
				queue.add(true);
			}
		}, 0, 1, SECONDS);

		queue.take();
		scheduledFuture.cancel(true);
		System.out.printf("[vergilyn] >>>> cancel scheduled-task. \n");

		// prevent jvm exit
		new Semaphore(0).tryAcquire(1, MINUTES);
	}

	@SneakyThrows
	@Test
	public void goodCloseSomeTask(){
		ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(1);

		int max = 5;
		AtomicInteger count = new AtomicInteger(0);

		/* 不能写成 lambda 表达式：
		 * Java Lambda 表达式中的this是 “表达式调用者对象”，匿名内部类的的this是 “匿名内部类对象本身”。
		 *   https://blog.csdn.net/qq_30436011/article/details/104824225
		 */
		scheduledExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				long index = count.getAndIncrement();
				System.out.printf("[vergilyn][%d] >>>> %s \n", count.get(), LocalTime.now());

				if (index < max){
					scheduledExecutor.schedule(this, 1, SECONDS);
				}
			}
		}, 0, SECONDS);

		// prevent jvm exit
		new Semaphore(0).tryAcquire(10, SECONDS);
		System.out.printf("[vergilyn] >>>> jvm exit, %s \n", LocalTime.now());
	}
}
