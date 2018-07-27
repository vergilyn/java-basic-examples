package com.vergilyn.examples.bean;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vergilyn.examples.utils.TransformUtils;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/25
 */
public class TransformUtilsTest {
    private List<Person> persons;

    @BeforeTest
    public void init(){
        Person p1 = new Person(1L, "vergil");
        Person p2 = new Person(2L, "dante");
        Person p3 = new Person(3L, "vergilyn");

        persons = Lists.newArrayList(p1, p2, p3);
    }

    @Test
    public void test(){
        LinkedHashMap<String, Person> map = TransformUtils.listToMap(persons, person -> "key-" + person.getId());
        String json = JSON.toJSONString(map,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNonStringKeyAsString,  // 如果key是数值类型，默认是没有双引号的。例如 {1:"xx"}
                SerializerFeature.WriteNonStringValueAsString // 如果value是数值类型，默认是没有双引号的。例如 {"key": 123}
                );

        System.out.println(json);
    }

    private class Person{
        private Long id;
        private String name;
        private LocalDateTime date;

        public Person() {
            this.date = LocalDateTime.now();
        }

        public Person(Long id, String name) {
            this();
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        @Override
        public String toString() {
            return "Person{" + "id=" + id + ", name='" + name + '\'' + ", date=" + date + '}';
        }
    }
}
