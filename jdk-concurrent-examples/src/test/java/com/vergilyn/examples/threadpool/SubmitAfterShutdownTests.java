package com.vergilyn.examples.threadpool;

import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SubmitAfterShutdownTests {

	/**
	 * 如果 <b>不调用</b>{@link ExecutorService#shutdown()}，{@link ExecutorService#awaitTermination(long, TimeUnit)}
	 * <p> <b>Q1.</b> 会等待 多久，返回true/false？
	 * <p> <b>A1.</b>
	 *  会等待 `awaitMillis`，即使所有已调教的任务已执行完毕，也不存在等待执行的任务。
	 *  <b>返回值 始终是 false。</b>
	 */
	@SneakyThrows
	@ParameterizedTest
	@CsvSource(value = {
			"false,2,6,2000,10000", // 验证`Q1`，执行耗时接近 10s，证明会一直等待，并且
			"true,2,6,2000,20000", // 执行耗时接近 6s，刚好是6个任务执行完成耗时。
	})
	public void test(boolean isShutdown, int execThreadNums, int taskNums, int taskCostMillis, int awaitMillis){
		ExecutorService executorService = Executors.newFixedThreadPool(execThreadNums);

		Stopwatch stopwatch = Stopwatch.createStarted();
		for (int i = 0; i < taskNums; i++) {
			executorService.submit(() -> {
				try {
					TimeUnit.MILLISECONDS.sleep(taskCostMillis);
					System.out.printf("[thread_%s] time: %s \n", Thread.currentThread().getName(), LocalTime.now());
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
		}

		if (isShutdown){
			executorService.shutdown();
		}

		boolean termination = executorService.awaitTermination(awaitMillis, TimeUnit.MILLISECONDS);
		stopwatch.stop();

		System.out.printf("[%s] termination: %s, elapsed: %s \n", LocalTime.now(), termination, stopwatch);
	}
}
