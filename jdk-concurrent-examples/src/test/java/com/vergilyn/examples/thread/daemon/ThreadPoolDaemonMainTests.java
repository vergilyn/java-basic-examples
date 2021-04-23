package com.vergilyn.examples.thread.daemon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author vergilyn
 * @since 2021-04-23
 */
public class ThreadPoolDaemonMainTests extends AbstractThreadTests{

	public static void main(String[] args) {
		ThreadPoolDaemonMainTests tests = new ThreadPoolDaemonMainTests();

		// true, 立即退出JVM
		// false, 始终挂起（JVM不会退出）。除非调用`threadPool.shutdown()`
		final boolean daemon = false;
		final long sleep = 2;

		ExecutorService threadPool = Executors.newFixedThreadPool(1, r -> {
			Thread thread = new Thread(r);
			thread.setDaemon(daemon);
			return thread;
		});

		threadPool.submit(tests.build(daemon, sleep));

		// 等待已提交的任务执行完成，打印信息间隔 2s
		// threadPool.shutdown();

		// 尝试执行已提交的任务，任务线程中虽然 sleep 2s，但会被立即唤醒并执行。打印信息间隔 0s
		threadPool.shutdownNow();
	}
}
