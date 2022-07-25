package com.vergilyn.examples.jdk8.features.date;

import cn.hutool.core.date.DateUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

/**
 * @author vergilyn
 * @since 2021-08-09
 *
 * @see Duration
 * @see ChronoUnit
 */
public class DateCalculateTests {

	@Test
	public void duration() {
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
	 * 可以很方便的 计算时间差 <br/>
	 * 1. 特别注意，<b>（向下）取整，直接不管低单位的计算</b>
	 */
	@Test
	public void chronoUnit(){
		LocalDate localDate = LocalDate.of(2022, 3, 4);

		// min -> 00:00
		LocalDateTime prevLocal = LocalDateTime.of(localDate, LocalTime.MIN);
		// max -> 23:59:59.999999999
		LocalDateTime nextLocal = LocalDateTime.of(localDate.plusDays(2), LocalTime.MAX);

		String prev = prevLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		String next = nextLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		String prefix = prev + " between " + next;

		System.out.printf("%s >> %s days \n", prefix, ChronoUnit.DAYS.between(prevLocal, nextLocal));

		System.out.printf("%s >> %s hours \n", prefix, ChronoUnit.HOURS.between(prevLocal, nextLocal));

		// 4319 minutes
		System.out.printf("%s >> %s minutes \n", prefix, ChronoUnit.MINUTES.between(prevLocal, nextLocal));

		// 259199 seconds = 4319 * 60 + 59
		System.out.printf("%s >> %s seconds \n", prefix, ChronoUnit.SECONDS.between(prevLocal, nextLocal));

		// 259199999 millis = 259199000 + 999
		System.out.printf("%s >> %s millis \n", prefix, ChronoUnit.MILLIS.between(prevLocal, nextLocal));

	}

	/**
	 * 间隔天数
	 *
	 * <p>{@linkplain cn.hutool.core.date.DateUtil#between(Date, Date, DateUnit)} <br/>
	 * 核心就是：“(end.getTime() - begin.getTime()) / unit” <br/>
	 * 不满足期望，比如 `2021-08-09 12:00:00 ~ 2021-08-10 01:00:00`，不满 24hours，但期望天数差是 1days！
	 */
	@Test
	public void numberOfDaysBetween() {
		LocalDate prev = LocalDate.parse("2021-08-09");
		LocalDate next = LocalDate.parse("2021-08-11");

		long prevEpochDay = prev.toEpochDay();
		long nextEpochDay = next.toEpochDay();

		System.out.printf("[epoch] next - prev = %d \n", (nextEpochDay - prevEpochDay));

		// java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Seconds
		// Duration duration = Duration.between(prev, next);

		System.out.printf("[chrono] next - prev = %d days", ChronoUnit.DAYS.between(prev, next));

		// java.time.temporal.UnsupportedTemporalTypeException: Unsupported unit: Hours
		System.out.printf("[chrono] next - prev = %d hours", ChronoUnit.HOURS.between(prev, next));
	}

	/**
	 * 通过 {@linkplain ChronoUnit} 可以很方便的计算 当天剩余的时间。
	 */
	@Test
	public void calcTodayRemainSeconds(){
		LocalDateTime begin = LocalDateTime.now();
		LocalDateTime tomorrow = LocalDateTime.of(begin.toLocalDate().plusDays(1), LocalTime.MIN);

		// 虽然可以通过 date -> instant(implements Temporal)，可以满足 ChronoUnit.between(Temporal, Temporal)
		// 但不能直接用`Instant`，between 会抛出异常： （解决方法，date 转成 LocalDateTime）
		//   java.time.DateTimeException: Unable to obtain Instant from TemporalAccessor: 2022-03-05T00:00 of type java.time.LocalDateTime
		Date date = Date.from(begin.atZone(ZoneId.systemDefault()).toInstant());
		LocalDateTime now = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		String prefix = String.format("%s between %s ", format(now), format(tomorrow));

		System.out.printf("%s >> %d days \n", prefix, ChronoUnit.DAYS.between(now, tomorrow));
		System.out.printf("%s >> %d hours \n", prefix, ChronoUnit.HOURS.between(now, tomorrow));
		System.out.printf("%s >> %d minutes \n", prefix, ChronoUnit.MINUTES.between(now, tomorrow));
		System.out.printf("%s >> %d seconds \n", prefix, ChronoUnit.SECONDS.between(now, tomorrow));
		System.out.printf("%s >> %d millis \n", prefix, ChronoUnit.MILLIS.between(now, tomorrow));
	}

	/**
	 * 任意时间，获取所在月的最后一天。
	 */
	@ParameterizedTest
	@CsvSource({
			"2022-06-15,2022-06-30",
			"2022-02-10,2022-02-28",
			"2024-02-10,2024-02-29"
	})
	public void monthLastDay(String localDateStr, String expectedStr){
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate localDate = LocalDate.parse(localDateStr, dateTimeFormatter);

		// 核心
		LocalDate actualDate = localDate.with(TemporalAdjusters.lastDayOfMonth());

		LocalDate expectedDate = LocalDate.parse(expectedStr);

		System.out.printf("source: %s, expected: %s, actual: %s \n", localDateStr, expectedDate, actualDate);
		Assertions.assertEquals(expectedDate, actualDate);
	}

	private String format(LocalDateTime localDateTime){
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS,nnnnnnnnn"));
	}

	private String format(Instant instant){
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS,nnnnnnnnn"));
	}
}
