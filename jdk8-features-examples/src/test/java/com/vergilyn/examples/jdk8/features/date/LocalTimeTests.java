package com.vergilyn.examples.jdk8.features.date;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalTimeTests {
	private final LocalTime begin = LocalTime.of(1, 0, 0);
	private final LocalTime now = LocalTime.of(1, 5, 0);
	private final LocalTime end = LocalTime.of(1, 10, 0);

	@Test
	public void test(){
		int beginSecond = begin.toSecondOfDay();
		int nowSecond = now.toSecondOfDay();
		int endSecond = end.toSecondOfDay();

		System.out.printf("time: %s, secondOfDay: %d \n", begin, beginSecond);
		System.out.printf("time: %s, secondOfDay: %d \n", now, nowSecond);
		System.out.printf("time: %s, secondOfDay: %d \n", end, endSecond);

		/**
		 * 相对 {@linkplain LocalTime#isAfter(LocalTime)}，
		 * 通过 toSecondOfDay() 让代码可读性更高（并且起到 lte/gte 效果）
 		 */
		// begin <= now <= end
		boolean range = beginSecond <= nowSecond && nowSecond <= endSecond;

		assertThat(range).isTrue();
	}
}
