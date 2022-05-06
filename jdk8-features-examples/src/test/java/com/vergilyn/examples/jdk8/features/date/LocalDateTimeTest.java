package com.vergilyn.examples.jdk8.features.date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <a href="https://zhuanlan.zhihu.com/p/104848429">时间戳和LocalDateTime和Date互转和格式化</a>
 *
 * @author vergilyn
 * @since 2021-11-19
 */
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

	/**
	 * `(ZoneOffset.ofHours(8)`写法不友好，且还需要区分 unix 是不是 UTC+8之类的。
	 */
	@Test
	public void unix() {
		// 2021-10-19 12:34:56.123 >>>> 1634618096123L
		long unixTime = 1634618096123L;
		LocalDateTime time = LocalDateTime.of(2021, 10, 19, 12, 34, 56, (int) TimeUnit.MILLISECONDS.toNanos(123L));
		System.out.println("time >>>> " + time);

		// long mills = time.toEpochSecond(ZoneOffset.UTC);  // 1634646896L
		long mills = time.toEpochSecond(ZoneOffset.ofHours(8)) * 1000 + TimeUnit.NANOSECONDS.toMillis(time.getNano());
		System.out.println("LocalDateTime to Milliseconds >>>> " + mills);
		assertThat(mills).isEqualTo(unixTime);

		LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(mills / 1000,
		                                                          (int) TimeUnit.MILLISECONDS.toNanos(mills % 1000),
		                                                          ZoneOffset.ofHours(8));
		System.out.println("Milliseconds to LocalDateTime >>>> " + localDateTime);

		// 推荐。但`ZoneOffset.ofHours(8)`写法还是不友好。
		long milliseconds = localDateTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();

		System.out.println("Milliseconds to LocalDateTime >>>> " + milliseconds);

	}

	@Test
	public void toEpochDay() {
		LocalDateTime now = LocalDateTime.now();

		System.out.println(now.toLocalDate().toEpochDay());
	}

	@Test
	public void toEpochMills(){
		Date date = new Date();
		System.out.println("date >>>> " + date.getTime());

		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

		System.out.println("UTC epoch-second  : " + localDateTime.toEpochSecond(ZoneOffset.UTC));

		ZoneOffset systemZoneOffset = ZoneOffset.ofHours(8);
		long rightEpochSecond = localDateTime.toEpochSecond(systemZoneOffset);
		System.out.println("UTC+8 epoch-second: " + rightEpochSecond);

		// 只是`秒`后的部分，精确到 nano
		int nanoOfSecond = localDateTime.getNano();
		System.out.println("nano-of-second: " + nanoOfSecond);

		long epochNano = TimeUnit.SECONDS.toNanos(rightEpochSecond) + nanoOfSecond;
		System.out.println("epoch-nano: " + epochNano);
		System.out.println("epoch-mills: " + TimeUnit.NANOSECONDS.toMillis(epochNano));

		// 另外中获得 epoch-mills 的方式
		Instant instant = localDateTime.toInstant(systemZoneOffset);
		System.out.println("[2]epoch-mills: " + instant.toEpochMilli());

	}
}
