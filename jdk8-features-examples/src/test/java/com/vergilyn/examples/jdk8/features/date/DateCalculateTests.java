package com.vergilyn.examples.jdk8.features.date;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-08-09
 */
public class DateCalculateTests {

	@Test
	public void test(){
		// java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Seconds
		// final Duration duration = Duration.between(prev, next);

		LocalDateTime prev = LocalDateTime.of(2021, 8, 9, 12, 34, 56);
		LocalDateTime next = LocalDateTime.of(2021, 8, 11, 14, 56, 0);

		Duration duration = Duration.between(prev, next);

		System.out.println("days: " + duration.toDays());  // 2
		System.out.println("hours: " + duration.toHours());  // 50
		System.out.println("minutes: " + duration.toMinutes());  // 3021
		System.out.println("seconds: " + duration.getSeconds());  // 181264 = 3021 * 60 + 4
		System.out.println("millis: " + duration.toMillis());  // 181264000
	}

	/**
	 * 间隔天数
	 */
	@Test
	public void numberOfDaysBetween(){
		LocalDate prev = LocalDate.parse("2021-08-09");
		LocalDate next = LocalDate.parse("2021-08-11");

		long prevEpochDay = prev.toEpochDay();
		long nextEpochDay = next.toEpochDay();

		System.out.printf("%d", (nextEpochDay - prevEpochDay));
	}
}
