package com.vergilyn.examples.jdk8.features.date;

import cn.hutool.core.date.DateUnit;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
	 *
	 * <p>{@linkplain cn.hutool.core.date.DateUtil#between(Date, Date, DateUnit)} <br/>
	 * 核心就是：“(end.getTime() - begin.getTime()) / unit” <br/>
	 * 不满足期望，比如 `2021-08-09 12:00:00 ~ 2021-08-10 01:00:00`，不满 24hours，但期望天数差是 1days！
	 */
	@Test
	public void numberOfDaysBetween(){
		LocalDate prev = LocalDate.parse("2021-08-09");
		LocalDate next = LocalDate.parse("2021-08-11");

		long prevEpochDay = prev.toEpochDay();
		long nextEpochDay = next.toEpochDay();

		System.out.printf("next - prev = %d", (nextEpochDay - prevEpochDay));

	}


}
