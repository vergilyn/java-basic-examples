package com.vergilyn.examples.jdk8.features.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-08-09
 */
public class DateConvertTest {

	@Test
	public void date2LocalDateTime(){
		Date date = new Date();
		System.out.println("date >>>> " + DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));

		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
		System.out.println("localDateTime >>>> " + localDateTime);
	}

	@Test
	public void localDateTime2Date(){
		LocalDateTime localDateTime = LocalDateTime.now();
		System.out.println("localDateTime >>>> " + localDateTime);

		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		System.out.println("date >>>> " + DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));
	}
}
