package com.vergilyn.examples.jdk8.features.date;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

/**
 * java.time.LocalDate，只对年月日做出处理
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class LocalDateTest {
    private LocalDate now = LocalDate.now();

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

    @org.junit.jupiter.api.Test
    public void convert(){
        Date date = new Date();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());

        LocalDate localDate = localDateTime.toLocalDate();

        System.out.println(DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(localDateTime);
        System.out.println(localDate);
    }

    @Test
    public void format(){
        LocalDate now = LocalDate.now();
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
    }

    /* LocalDate只存在年月日，不存在时分秒等 */
    @org.testng.annotations.Test(expectedExceptions = UnsupportedTemporalTypeException.class, expectedExceptionsMessageRegExp = "Unsupported field: HourOfDay")
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

    public static void main(String[] args) {
        LocalDate before = LocalDate.parse("2021-08-09");
        LocalDate after = LocalDate.parse("2021-08-18");

        System.out.println(after.toEpochDay() - before.toEpochDay());
    }
}
