package com.vergilyn.examples.jmh.jdk;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-09
 *
 * @see org.apache.commons.lang3.time.StopWatch
 * @see org.springframework.util.StopWatch
 * @see com.google.common.base.Stopwatch
 */
public class StopWatchTest {

	@SneakyThrows
	@Test
	public void apache(){
		org.apache.commons.lang3.time.StopWatch stopWatch = new org.apache.commons.lang3.time.StopWatch();
		stopWatch.start();

		TimeUnit.SECONDS.sleep(2);

		stopWatch.stop();

		System.out.println(stopWatch.toString());
	}

	@SneakyThrows
	@Test
	public void spring(){
		org.springframework.util.StopWatch stopWatch = new org.springframework.util.StopWatch();
		stopWatch.start();

		TimeUnit.SECONDS.sleep(2);

		stopWatch.stop();

		System.out.println(stopWatch.prettyPrint());

		System.out.println(stopWatch.shortSummary());
	}

	@SneakyThrows
	@Test
	public void guava(){
		com.google.common.base.Stopwatch stopWatch = com.google.common.base.Stopwatch.createUnstarted();
		stopWatch.start();

		TimeUnit.SECONDS.sleep(2);

		stopWatch.stop();

		System.out.println(stopWatch.toString());
	}
}
