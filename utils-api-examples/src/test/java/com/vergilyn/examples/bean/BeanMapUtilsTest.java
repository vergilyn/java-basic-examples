package com.vergilyn.examples.bean;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/6/11
 */
public class BeanMapUtilsTest {
    private Map<String, Object> source;

    @BeforeClass
    public void before() {
        source = Maps.newHashMap();
        source.put("id", 1L);
        source.put("date", new Date());
        source.put("str", "string");
    }

    @Test
    public void gson() {
        System.out.println("gson: " + BeanMapUtils.gson(source, TestBean.class));
    }

    @Test
    public void jackson() {
        System.out.println("jackson: " + BeanMapUtils.jackson(source, TestBean.class));
    }

    @Test
    public void apache() {
        System.out.println("apache: " + BeanMapUtils.apache(source, TestBean.class));
    }

    @Test
    public void mapToBean() {
        System.out.println("beanMap: " + BeanMapUtils.mapToBean(source, TestBean.class));
    }

    @Data
    @AllArgsConstructor
    @ToString
    class TestBean{
        private Long id;
        private Date date;
        private String str;
    }
}
