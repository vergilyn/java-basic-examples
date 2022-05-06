package com.vergilyn.examples.jdk8.features;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author vergilyn
 * @since 2022-02-16
 *
 * @see cn.hutool.core.date.LocalDateTimeUtil
 */
public class LocalDateTimeUtils {
	public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

	/**
	 * 毫秒转{@link LocalDateTime}，根据时区不同，结果会产生时间偏移
	 *
	 * @param epochMilli 从1970-01-01T00:00:00Z开始计数的毫秒数
	 * @param zoneId     时区
	 * @return
	 */
	public static LocalDateTime of(long epochMilli) {
		return of(Instant.ofEpochMilli(epochMilli));
	}

	public static LocalDateTime of(Date date){
		if (date == null){
			return null;
		}

		return of(date.toInstant());
	}

	public static LocalDateTime of(Instant instant){
		return of(instant, DEFAULT_ZONE_ID);
	}

	public static LocalDateTime of(Instant instant, ZoneId zoneId) {
		zoneId = zoneId == null ? DEFAULT_ZONE_ID : zoneId;
		if (null == instant) {
			return null;
		}

		return LocalDateTime.ofInstant(instant, zoneId);
	}

	public static Date toDate(LocalDateTime localDateTime){
		return toDate(localDateTime, DEFAULT_ZONE_ID);
	}

	public static Date toDate(LocalDateTime localDateTime, ZoneId zone){
		if (localDateTime == null){
			return null;
		}

		return Date.from(localDateTime.atZone(zone).toInstant());
	}

}
