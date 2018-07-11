package com.vergilyn.examples.jdk8.features.date;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.UnsupportedTemporalTypeException;

import org.testng.annotations.Test;

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

    @Test
    public void format(){
        LocalDate now = LocalDate.now();
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")));
    }

    /* LocalDate只存在年月日，不存在时分秒等 */
    @Test(expectedExceptions = UnsupportedTemporalTypeException.class, expectedExceptionsMessageRegExp = "Unsupported field: HourOfDay")
    public void exception(){
        LocalDate now = LocalDate.now();
        System.out.println("localDate format: " + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @Test
    public void extend(){
        LocalDate localDate = LocalDate.parse("2018-06-30");
        System.out.println(localDate.withMonth(1)); // 2018-01-30
        System.out.println(localDate.withMonth(2)); // 2018-02-28

        System.out.println(localDate.withDayOfMonth(10));  // 2018-06-10
        try {
            // exception: java.time.DateTimeException: Invalid date 'JUNE 31'
            System.out.println(localDate.withDayOfMonth(31));
        }catch (DateTimeException e){
            System.out.println("exception: " + e.getMessage());
        }

        System.out.println(localDate.plusDays(1));      // 2018-07-01
        System.out.println(localDate.plusMonths(4));    // 2018-10-30
        System.out.println(localDate.plusMonths(7));    // 2019-01-30
        System.out.println(localDate.plusYears(2));     // 2020-06-30

    }

    @Test
    public void lastDayOfMonth(){
        // 判断月末
        LocalDate yes = LocalDate.parse("2018-02-28");
        System.out.printf("%s 是不是月末：%b", yes, yes.with(TemporalAdjusters.lastDayOfMonth()).isEqual(yes)).println();

        LocalDate no = LocalDate.parse("2018-07-11");
        System.out.printf("%s 是不是月末：%b", no, no.with(TemporalAdjusters.lastDayOfMonth()).isEqual(no)).println();
    }
}
