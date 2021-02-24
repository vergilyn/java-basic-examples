package com.vergilyn.examples.jdk8.features.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

public class LocalDateTimeTest {

	@Test
	public void convert(){
		Date date = new Date();

		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		System.out.println(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));
		System.out.println(localDateTime);
	}

	@Test
	public void epochDay(){
		LocalDateTime now = LocalDateTime.now();

		System.out.println(now.toLocalDate().toEpochDay());
	}
}
