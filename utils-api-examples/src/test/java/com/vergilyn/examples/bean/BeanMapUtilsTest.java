package com.vergilyn.examples.bean;

import java.util.Date;
import java.util.Map;

import com.google.common.collect.Maps;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/6/11
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BeanMapUtilsTest {
    private Map<String, Object> source;

    @Before
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
}

class TestBean{
    private Long id;
    private Date date;
    private String str;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "TestBean{" + "id=" + id + ", date=" + date + ", str='" + str + '\'' + '}';
    }
}