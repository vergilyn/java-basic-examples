package com.vergilyn.examples.jdk8.features.date;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-08-09
 */
public class DateParseTests {

	@SneakyThrows
	@Test
	public void parse(){
		String dateStr = "2021-08-09";
		String timeStr = "16:20:30.123";
		String datetimeStr = dateStr + " " + timeStr;

		Date date = DateUtils.parseDate(datetimeStr, "yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("data >>>> " + date);

		LocalDate localDate = LocalDate.parse(dateStr);
		System.out.println("localDate >>>> " + localDate);

		LocalTime localTime = LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
		System.out.println("localTime >>>> " + localTime);

		LocalDateTime localDateTime = LocalDateTime.parse(datetimeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
		System.out.println("localDateTime >>>> " + localDateTime);
	}
}
