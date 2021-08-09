package com.vergilyn.examples.jdk8.features.date;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-08-09
 */
public class DateCalculateTests {

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
