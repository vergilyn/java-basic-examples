package com.vergilyn.examples.jmh.jdk;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

/**
 * 多个阶段 不同耗时，以及总共耗时。
 * spring的可以完全满足需求，apache/guava 都是基本的，简单计算后也可以满足。
 *
 * @author vergilyn
 * @since 2021-11-24
 */
public class StopWatchStageTests {

	/**
	 * apache-stopwatch, 必须先 reset 再 re-start。
	 */
	@Test
	public void apache(){
		org.apache.commons.lang3.time.StopWatch stopwatch = org.apache.commons.lang3.time.StopWatch.createStarted();

		sleep(2);
		stopwatch.stop();
		System.out.println("[stage-1]cost: " + stopwatch);

		// Stopwatch must be reset before being restarted.
		stopwatch.reset();
		stopwatch.start();

		sleep(1);
		stopwatch.stop();
		System.out.println("[stage-2]cost: " + stopwatch);
	}

	/**
	 * 可以间断，也可以通过`reset`重置。
	 */
	@Test
	public void guava(){
		com.google.common.base.Stopwatch stopwatch = com.google.common.base.Stopwatch.createStarted();

		sleep(2);
		System.out.println("[stage-1]cost: " + stopwatch.stop());

		// stopwatch.reset();
		stopwatch.start();

		sleep(1);
		System.out.println("[stage-2]cost: " + stopwatch.stop());

	}

	/**
	 * 能达到期望的效果，但是 输出要自己调整！
	 * （spring.stopwatch 确实更强，就是输出样式不太友好，要自己定制。）
	 *
	 * @see com.google.common.base.Stopwatch#toString()
	 */
	@Test
	public void spring(){
		org.springframework.util.StopWatch stopwatch = new org.springframework.util.StopWatch();

		stopwatch.start("stage-1");
		sleep(2);
		stopwatch.stop();
		System.out.println("[stage-1]cost: " + format(stopwatch.getLastTaskTimeNanos()));

		stopwatch.start("stage-2");
		sleep(1);
		stopwatch.stop();

		System.out.println("[stage-2]cost: " + format(stopwatch.getLastTaskTimeNanos()));

		System.out.println("total-cost: " + format(stopwatch.getTotalTimeNanos()));
		System.out.println("(default)total-cost: " + stopwatch);

	}

	/**
	 * @see com.google.common.base.Stopwatch#toString()
	 */
	private String format(long nanos){
		return Duration.ofNanos(nanos).toString();
	}


	private void sleep(long seconds){
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
		}
	}
}
