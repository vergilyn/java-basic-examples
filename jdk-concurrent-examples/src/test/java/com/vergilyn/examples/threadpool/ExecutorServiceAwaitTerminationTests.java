package com.vergilyn.examples.threadpool;

import cn.hutool.core.thread.NamedThreadFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testng.collections.Lists;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutorServiceAwaitTerminationTests {

	/**
	 * 参考：org.apache.rocketmq.common.utils.ThreadUtils#shutdownGracefully(ExecutorService, long, TimeUnit)
	 * <pre>{@code
	 *     public static void shutdownGracefully(ExecutorService executor, long timeout, TimeUnit timeUnit) {
	 *         // Disable new tasks from being submitted.
	 *         executor.shutdown();
	 *         try {
	 *             // Wait a while for existing tasks to terminate.
	 *             if (!executor.awaitTermination(timeout, timeUnit)) {
	 *                 executor.shutdownNow();
	 *                 // Wait a while for tasks to respond to being cancelled.
	 *                 if (!executor.awaitTermination(timeout, timeUnit)) {
	 *                     log.warn(String.format("%s didn't terminate!", executor));
	 *                 }
	 *             }
	 *         } catch (InterruptedException ie) {
	 *             // (Re-)Cancel if current thread also interrupted.
	 *             executor.shutdownNow();
	 *             // Preserve interrupt status.
	 *             Thread.currentThread().interrupt();
	 *         }
	 *     }
	 * }
	 * </pre>
	 */
	@SneakyThrows
	@ParameterizedTest
	// 0: 不会等待，直接返回
	// 2s: 等到 2s， 返回 false
	// 5s: 等到 4s+（因为 2个task 最长执行 4s），返回 true。
	@ValueSource(ints = {0, 2000, 5000})
	public void test(long awaitTimeout){
		String logPrefix = "[timeout-" + awaitTimeout + "]";

		// 参考：rocketMQ： org.apache.rocketmq.client.impl.consumer.ConsumeMessageOrderlyService
		LinkedBlockingQueue<Runnable> consumeRequestQueue = new LinkedBlockingQueue<Runnable>();
		ExecutorService executorService = new ThreadPoolExecutor(
				2,
				8,
				1000 * 60,
				TimeUnit.MILLISECONDS,
				consumeRequestQueue,
				new NamedThreadFactory("ConsumeMessageThread_", false));

		executorService.submit(() -> {
			log.info("{} task-01 running....", logPrefix);
			sleepSafe(3000);
			log.info("{} task-01 finish....", logPrefix);
		});

		executorService.submit(() -> {
			log.info("{} task-02 running....", logPrefix);
			sleepSafe(4000);
			log.info("{} task-02 finish....", logPrefix);
		});

		executorService.shutdown();

		executorService.awaitTermination(awaitTimeout, TimeUnit.MILLISECONDS);

		log.info("{} isTerminated：{}", logPrefix, executorService.isTerminated());

	}

	private void sleepSafe(long millis){
		try {
			TimeUnit.MILLISECONDS.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}

	public static void main(String[] args) {
		List<Object> objects = Lists.newArrayList();

		System.out.println(objects.stream().findFirst().get());
	}
}
