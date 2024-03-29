package com.vergilyn.examples.jmh.jdk;

import com.vergilyn.examples.jmh.SpringStopWatchFormat;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

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
	 * 例如 `00:00:02.007`
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
	 * 推荐，可读性相当好。例如`2.015 s`
	 */
	@SneakyThrows
	@Test
	public void guava(){
		com.google.common.base.Stopwatch stopWatch = com.google.common.base.Stopwatch.createStarted();

		TimeUnit.SECONDS.sleep(2);

		stopWatch.stop();

		System.out.println(stopWatch.toString());
	}

	/**
	 * 过于详细，输出可阅读性不够友好 （只能是 ns）。但支持 多阶段！
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

		// 参考 guava 输出
		print("guava-style", SpringStopWatchFormat.toString(stopWatch));
	}

	private void print(String flag, Object content){
		System.out.println(flag + " >>>> ");
		System.out.println(content);
		System.out.println();
	}
}
