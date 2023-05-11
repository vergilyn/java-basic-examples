package com.vergilyn.examples.jdk8.features.date;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeTests {
	String format = "yyyy-MM-dd HH:mm:ss Z";

	@Test
	public void test(){
		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.ofHours(8));

		String utc8 = now.format(DateTimeFormatter.ofPattern(format));
		System.out.println("UTC+8 : " + utc8);

		ZonedDateTime parse = ZonedDateTime.parse(utc8, DateTimeFormatter.ofPattern(format));


	}

	/**
	 * {@linkplain LocalDateTime} 是不带时区的!
	 */
	@SneakyThrows
	@Test
	public void incorrect(){
		LocalDateTime now = LocalDateTime.now();

		Throwable throwable = Assertions.catchThrowable(() -> now.format(DateTimeFormatter.ofPattern(format)));

		Assertions.assertThat(throwable).isInstanceOf(java.time.temporal.UnsupportedTemporalTypeException.class)
				.hasMessage("Unsupported field: OffsetSeconds");

		throw throwable;
	}
}
