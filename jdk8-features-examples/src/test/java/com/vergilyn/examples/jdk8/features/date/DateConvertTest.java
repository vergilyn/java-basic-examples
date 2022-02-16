package com.vergilyn.examples.jdk8.features.date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
		System.out.println("date >>>> " + DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));

		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		System.out.println("localDateTime >>>> " + localDateTime);

		// (暂时推荐)从 Date -> LocalDateTime -> LocalDate/LocalTime
		// 不推荐，因为Date的相应方法都是`@Deprecated`
		LocalDate localDate = LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
		System.out.println("(@Deprecated)localDate >>>> " + localDate);

		LocalDate localDate1 = localDateTime.toLocalDate();
		System.out.println("Date -> LocalDateTime -> LocalDate >>>> " + localDate1);
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
