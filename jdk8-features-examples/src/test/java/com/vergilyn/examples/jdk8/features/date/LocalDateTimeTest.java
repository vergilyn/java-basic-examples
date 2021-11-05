package com.vergilyn.examples.jdk8.features.date;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeTest {

	@Test
	public void convert() throws ParseException {
		String format = "yyyy-MM-dd HH:mm:ss.SSS";
		String dateStr = "2021-03-10 12:05:10.001";

		Date date = DateUtils.parseDate(dateStr, format);

		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		String localDateTimeStr = localDateTime.format(DateTimeFormatter.ofPattern(format));

		System.out.printf("Date to LocalDateTime >>>> \nDate: %s \nLoDT: %s \n", dateStr, localDateTimeStr);
		assertThat(localDateTimeStr).isEqualTo(dateStr);

		// RIGHT
		Date date2 = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		String dateStr2 = DateFormatUtils.format(date2, format);
		System.out.printf("LocalDateTime to Date >>>> \nLoDT: %s \nDate: %s \n", localDateTimeStr, dateStr2);
		assertThat(dateStr2).isEqualTo(localDateTimeStr);

		// ERROR: actual `2021-03-10 20:05:10.001`
		Date date1 = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
		String dateStr1 = DateFormatUtils.format(date1, format);
		System.out.printf("LocalDateTime to Date >>>> \nLoDT: %s \nDate: %s \n", localDateTimeStr, dateStr1);
		assertThat(dateStr1).isEqualTo(localDateTimeStr);
	}

	@Test
	public void epochDay(){
		LocalDateTime now = LocalDateTime.now();

		System.out.println(now.toLocalDate().toEpochDay());
	}

	/**
	 * `(ZoneOffset.ofHours(8)`写法不友好，且还需要区分 unix 是不是 UTC+8之类的。
	 */
	@Test
	public void unix(){
		// 2021-10-19 12:34:56.123 >>>> 1634618096123L
		long unixTime = 1634618096123L;
		LocalDateTime time = LocalDateTime.of(2021, 10, 19, 12, 34, 56, (int) TimeUnit.MILLISECONDS.toNanos(123L));
		System.out.println("time >>>> " + time);

		// long mills = time.toEpochSecond(ZoneOffset.UTC);  // 1634646896L
		long mills = time.toEpochSecond(ZoneOffset.ofHours(8)) * 1000
						+ TimeUnit.NANOSECONDS.toMillis(time.getNano());
		System.out.println("LocalDateTime to Milliseconds >>>> " + mills);
		assertThat(mills).isEqualTo(unixTime);

		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(mills / 1000,
		                                                          (int) TimeUnit.MILLISECONDS.toNanos(mills % 1000),
		                                                          ZoneOffset.ofHours(8));
		System.out.println("Milliseconds to LocalDateTime >>>> " + localDateTime);


	}
}
