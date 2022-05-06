package com.vergilyn.examples.jdk8.features.date;

import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * java.time.LocalDate，只对年月日做出处理
 * @author VergiLyn
 * @date 2018/7/11
 */
public class LocalDateTest {

    @Test
    public void constructor(){
        LocalDate now = LocalDate.now();
        System.out.println(now);

        // 相比java.util.Date，不存在偏移
        now = LocalDate.of(2018, 7, 11);
        System.out.println(now);

        now = LocalDate.parse("2018-07-11");
        System.out.println(now);

        now = LocalDate.parse("2018/07-11", DateTimeFormatter.ofPattern("yyyy/MM-dd"));
        System.out.println(now);
    }

    @Test
    public void toLocalDate(){
        Date date = new Date();
        System.out.println("Date >>>> " + DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));

        LocalDate date2LocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("date2LocalDate >>>> " + date2LocalDate);

        // 方式2： Date -> LocalDateTime -> LocalDate
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        System.out.println("LocalDateTime >>>> " + localDateTime);
        System.out.println("LocalDate >>>> " + localDate);
    }

    @Test
    public void fromLocalDate(){
        LocalDate localDate = LocalDate.now();

        LocalDateTime localDateTime = localDate.atStartOfDay();

        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    @SneakyThrows
    @Test
    public void toMilliseconds(){
        String dateStr = "2022-02-24";
        String pattern = "yyyy-MM-dd";
        Date date = DateUtils.parseDate(dateStr, pattern);

        // the number of milliseconds since `1970-01-01 00:00:00 GMT` represented by this date.
        System.out.println("date.getTime() >>>> " + date.getTime());

        // 只能到`seconds`，然后 seconds 转换一下就可以了
        LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        System.out.println("LocalDate epoch-second:" + zonedDateTime.toEpochSecond());
        // 转换一下，或者直接`* 1000`
        System.out.println("LocalDate epoch-mills :" + TimeUnit.SECONDS.toMillis(zonedDateTime.toEpochSecond()));

        Instant utcInstant = zonedDateTime.toLocalDateTime().toInstant(ZoneOffset.UTC);
        System.out.println("LocalDate epoch-mills(UTC):" + utcInstant.toEpochMilli());

        // 这个是正确的，但是感觉不友好。没有类似`ZoneOffset.default()`之类的 (`ZoneOffset.systemDefault()` 不是期望的)
        Instant expectInstant = zonedDateTime.toLocalDateTime().toInstant(ZoneOffset.ofHours(8));
        System.out.println("LocalDate epoch-mills(+8):" + expectInstant.toEpochMilli());

    }

    @Test
    public void format(){
        LocalDate now = LocalDate.now();
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
    }

    /* LocalDate只存在年月日，不存在时分秒等 */
    // @org.testng.annotations.Test(expectedExceptions = UnsupportedTemporalTypeException.class, expectedExceptionsMessageRegExp = "Unsupported field: HourOfDay")
    public void exception(){
        LocalDate now = LocalDate.now();

        System.out.println("localDate format: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void extend(){
        LocalDate localDate = LocalDate.parse("2018-06-30");
        System.out.println("withMonth(1)            : " + localDate.withMonth(1)); // 2018-01-30
        System.out.println("withMonth(2)            : " + localDate.withMonth(2)); // 2018-02-28

        System.out.println("withDayOfMonth(10)      : " + localDate.withDayOfMonth(10));  // 2018-06-10
        try {
            // exception: java.time.DateTimeException: Invalid date 'JUNE 31'
            System.out.println(localDate.withDayOfMonth(31));
        }catch (DateTimeException e){
            System.out.println("withDayOfMonth(31)      : " + e.getMessage());
        }

        System.out.println("plusDays(1)             : " + localDate.plusDays(1));      // 2018-07-01
        System.out.println("plusDays(-10)           : " + localDate.plusDays(-10));    // 2018-06-20
        System.out.println("plusMonths(4)           : " + localDate.plusMonths(4));    // 2018-10-30
        System.out.println("plusMonths(7)           : " + localDate.plusMonths(7));    // 2019-01-30
        System.out.println("plusYears(2)            : " + localDate.plusYears(2));     // 2020-06-30

    }

    @Test
    public void lastDayOfMonth(){
        // 判断月末
        LocalDate yes = LocalDate.parse("2018-02-28");
        System.out.printf("%s 是不是月末：%b", yes, yes.with(TemporalAdjusters.lastDayOfMonth()).isEqual(yes)).println();

        LocalDate no = LocalDate.parse("2018-07-11");
        System.out.printf("%s 是不是月末：%b", no, no.with(TemporalAdjusters.lastDayOfMonth()).isEqual(no)).println();
    }

    @Test
    public void epochDay(){
        LocalDate now = LocalDate.now();

        // 可以方便用于计算时间差
        long epochDay = now.toEpochDay();
        System.out.println(epochDay);
    }
}
