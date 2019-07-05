package com.vergilyn.examples.jdk8.features.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.vergilyn.examples.jdk8.features.Person;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

/**
 * @author VergiLyn
 * @date 2019-07-05
 */
public class GroupTest {

    private List<Person> list;

    @BeforeTest
    public void init(){
        list = Lists.newArrayList();

        list.add(new Person(1L, "1", "A"));
        list.add(new Person(2L, "2", "B"));
        list.add(new Person(3L, "3", "C"));
        list.add(new Person(4L, "4", "C"));
        list.add(new Person(5L, "5", "A"));
        list.add(new Person(6L, "6", "B"));
        list.add(new Person(7L, "7", "B"));
    }

    @Test
    public void group(){
        // 期望根据 person#type 分组 -> Map<String, Lists<Person>>

        Map<String, List<Person>> map = list.stream().collect(Collectors.groupingBy(Person::getType));

        System.out.println(map.getClass().getSimpleName() + ": " + JSON.toJSONString(map));

        // classifier：分组按照什么分类
        // mapFactory：分组最后用什么容器保存返回
        // downstream：按照第一个参数分类后，对应的分类的结果如何收集
        map = list.stream().collect(Collectors.groupingBy(Person::getType, Maps::newLinkedHashMap, Collectors.toList()));

        System.out.println(map.getClass().getSimpleName() + ": " + JSON.toJSONString(map));
    }
}
