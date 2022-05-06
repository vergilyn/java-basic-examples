package com.vergilyn.examples.jdk8.features.date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public class DateTests {

	@Test
	public void ofLocalDateTime(){
		LocalDateTime now = LocalDateTime.now();

		Date date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
	}

	/**
	 *
	 */
	@Test
	public void retainYearMonthDay(){
		Date date = new Date();

		Calendar calendar = getInstance();
		calendar.setTime(date);

		// calendar-instance  中包含了 当前的
		Calendar target = getInstance();
		System.out.printf("Calendar.getInstance() >> year: %s, month: %s, date: %s, "
				                  + "hour: %s, minute: %s, second: %s, millis: %s  \n",
		                  target.get(YEAR), target.get(MONTH), target.get(DATE),
		                  target.get(HOUR), target.get(MINUTE), target.get(SECOND),
		                  target.get(MILLISECOND)
		);

		// hour、minute、second、millis 都必须显示设置成`0`
		// target.set(calendar.get(YEAR), calendar.get(MONTH), calendar.get(DATE), 0, 0, 0);
		target.set(HOUR_OF_DAY, 0);
		target.set(MINUTE, 0);
		target.set(SECOND, 0);
		target.set(MILLISECOND, 0);

		Date time = target.getTime();

		System.out.println(toString(time));

	}

	private String toString(Date date){
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS");
	}
}
