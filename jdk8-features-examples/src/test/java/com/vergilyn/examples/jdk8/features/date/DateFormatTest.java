package com.vergilyn.examples.jdk8.features.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;

/**
 * {@linkplain java.text.SimpleDateFormat} 是线程不安全的（`parse`\`format`均不安全）；<br/>
 * JDK8中提供了##线程安全##的类: {@linkplain java.time.format.DateTimeFormatter}
 * @author VergiLyn
 * @date 2019-11-11
 */
public class DateFormatTest {

    @Test
    public void test(){
        LocalDate date = LocalDate.now();

        System.out.println("default: " + date);


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM-dd");
        System.out.println("DateTimeFormatter: " + date.format(dateTimeFormatter));
        System.out.println("BASIC_ISO_DATE: " + date.format(DateTimeFormatter.BASIC_ISO_DATE));

    }
}
