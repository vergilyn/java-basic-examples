package com.vergilyn.examples.concurrent;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 的 park/unpark 相对于 await/notify：
 *
 * @author vergilyn
 * @since 2022-10-13
 */
@Slf4j
public class LockSupportTests {

	@SneakyThrows
	@Test
	public void helloworld() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				log.info("[vergilyn] `LockSupport#park()` >>>> Before");
				LockSupport.park();
				log.info("[vergilyn] `LockSupport#park()` >>>> After");
			}
		};

		thread.start();

		TimeUnit.SECONDS.sleep(2);

		LockSupport.unpark(thread);

	}

	/**
	 * <a href="https://zhuanlan.zhihu.com/p/369128734">知乎，写给小白看的LockSupport</a>
	 */
	@SneakyThrows
	@Test
	public void lockSupport_2() {
		Thread th = new Thread(() -> {
			// 唤醒当前线程，会设置`permit = 1`。
			//   连续调用时， permit 始终等于 1。
			LockSupport.unpark(Thread.currentThread());

			// 阻塞当前线程，因为上一步`unpark，permit=1`，park直接消耗permit， 所以此处实际不会阻塞！
			//   不会抛出 InterruptedException！
			LockSupport.park();

			// 为什么 能执行此方法？ 见上分析
			System.out.println("子线程执行---------");
		});
		th.start();
		// 睡眠2秒
		TimeUnit.SECONDS.sleep(2);
		System.out.println("主线程执行---------");
	}

	@Test
	public void notifySpecifyThread() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(5);

		AtomicInteger threadIndex = new AtomicInteger(0);
		Map<Integer, Thread> threadMap = Maps.newConcurrentMap();
		for (int i = 0; i < 5; i++) {
			executor.submit(new Thread(){
				@Override
				public void run() {
					int index = threadIndex.getAndIncrement();
					// ** 不能写成`this` **
					// 1. 线程池执行时，传入的`new Thread()`只是个 task，不是真实的 thread！
					threadMap.put(index, Thread.currentThread());

					log.info("[vergilyn][t-{}] `LockSupport#park()` >>>> Before ", index);
					if (index > 0) {
						LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(index));
					} else {
						LockSupport.park();
					}
					log.info("[vergilyn][t-{}] `LockSupport#park()` >>>> After", index);
				}
			});
		}
		executor.shutdown();

		TimeUnit.SECONDS.sleep(1);

		LockSupport.unpark(threadMap.get(0));
		LockSupport.unpark(threadMap.get(3));

		executor.awaitTermination(5, TimeUnit.SECONDS);
	}

}
