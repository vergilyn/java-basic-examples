package com.vergilyn.examples.jdk8.features.stream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.vergilyn.examples.jdk8.features.Person;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author vergilyn
 * @date 2019-10-31
 */
public class DistinctTest {

    /**
     * 基本类型，或者 重写对象的`hashcode()`和`equals()`
     */
    @Test
    public void basicType(){
        List<Integer> list = Lists.newArrayList(1, 2, 3, 3, 4, 5, 4, 5);

        System.out.println("基本类型利用stream去重 >>>> ");

        List<Integer> distinct = list.stream().distinct().collect(Collectors.toList());
        System.out.printf("source: %d, distinct: %d, result: %s", list.size(), distinct.size(), distinct);
    }

    /**
     * 自定义对象，根据任意属性去重（不重写 `hashcode()`和`equals()`）
     * <a href=""></a>
     */
    @Test
    public void property(){
        Person p1 = new Person(1L, "p1", "A");
        Person p2 = new Person(1L, "p1", "B");
        Person p3 = new Person(3L, "p1", "C");

        List<Person> list = Lists.newArrayList(p1, p2, p3, p2, p3);

        ArrayList<Person> collect = list.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingLong(Person::getId))), ArrayList::new));

        System.out.println(collect);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
