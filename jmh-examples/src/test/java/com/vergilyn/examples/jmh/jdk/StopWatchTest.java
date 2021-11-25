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

	@Test
	public void test(){
	}

	/**
	 * 个人推荐。例如 `00:00:02.007`
	 */
	@SneakyThrows
	@Test
	public void apache(){
		org.apache.commons.lang3.time.StopWatch stopWatch = new org.apache.commons.lang3.time.StopWatch();
		stopWatch.start();

		TimeUnit.SECONDS.sleep(2);

		stopWatch.stop();

		System.out.println(stopWatch.toString());
	}

	/**
	 * 例如`2.015 s`
	 */
	@SneakyThrows
	@Test
	public void guava(){
		com.google.common.base.Stopwatch stopWatch = com.google.common.base.Stopwatch.createUnstarted();
		stopWatch.start();

		TimeUnit.SECONDS.sleep(2);

		stopWatch.stop();

		System.out.println(stopWatch.toString());
	}

	/**
	 * 过于详细
	 */
	@SneakyThrows
	@Test
	public void spring(){
		org.springframework.util.StopWatch stopWatch = new org.springframework.util.StopWatch();

		stopWatch.start("stage-1");
		TimeUnit.SECONDS.sleep(2);
		stopWatch.stop();

		stopWatch.start("stage-2");
		TimeUnit.SECONDS.sleep(1);
		stopWatch.stop();

		print("toString()", stopWatch.toString());

		print("prettyPrint()", stopWatch.prettyPrint());

		print("shortSummary()", stopWatch.shortSummary());

	}

	private void print(String flag, Object content){
		System.out.println(flag + " >>>> ");
		System.out.println(content);
		System.out.println();
	}
}
