package com.vergilyn.examples.jdk8.features.date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Date;

/**
 *
 * @author vergilyn
 * @since 2021-08-09
 */
public class DateConvertTest {

	/**
	 * @see cn.hutool.core.date.LocalDateTimeUtil#of(Date)
	 */
	@Test
	public void date2LocalDateTime(){
		Date date = new Date();
		System.out.println("Date: " + DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));

		ZonedDateTime zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
		LocalDate localDate = zonedDateTime.toLocalDate();
		LocalTime localTime = zonedDateTime.toLocalTime();
		LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();

		System.out.println("Date > localDate: " + localDate);
		System.out.println("Date > localTime: " + localTime);
		System.out.println("Date > localDateTime: " + localDateTime);

		// 方式2，然后再由 LocalDateTime 得到 LocalDate/LocalTime
		LocalDateTime localDateTime1 = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		System.out.println("localDateTime1 >>>> " + localDateTime1);
	}

	@Test
	public void localDateTime2Date(){
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println("localDateTime >>>> " + localDateTime);

		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		System.out.println("date >>>> " + DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	@Test
	public void jdk8TimerConvert(){
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println("localDateTime >>>> " + localDateTime);

		LocalDate localDate = localDateTime.toLocalDate();
		System.out.println("localDate >>>> " + localDate);

		LocalTime localTime = localDateTime.toLocalTime();
		System.out.println("localTime >>>> " + localTime);
	}
}
