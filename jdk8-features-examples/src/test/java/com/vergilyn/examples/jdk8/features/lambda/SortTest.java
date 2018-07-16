package com.vergilyn.examples.jdk8.features.lambda;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import org.testng.annotations.Test;


/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/16
 */
public class SortTest {
    private final List<Person> users = Lists.newArrayList(
            new Person("jack", 17, 10),
            new Person("jack", 18, 10),
            new Person("jack", 19, 11),
            new Person("apple", 25, 15),
            new Person("tommy", 23, 8),
            new Person("jessica", 15, 13)
    );

    @Test
    public void basic(){
        // java7排序
        System.out.println("java7排序 >>>> ");
        List<Person> java7 = Lists.newArrayList(users);
        Collections.sort(java7, new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return Person.compareTo(o1, o2);
            }
        });
        for (Person user : java7) {
            System.out.println(user);
        }

        // java8排序
        System.out.println("\r\njava8排序 >>>> ");
        List<Person> java8 = Lists.newArrayList(users);
        java8.sort(Person::compareTo);
        java8.forEach(System.out::println);

        // java8进阶排序
        System.out.println("\r\njava8进阶排序 >>>> ");
        List<Person> java82 = Lists.newArrayList(users);
        java82.sort(Comparator.comparingInt(Person::getAge).reversed()); // 支持反向
        java82.forEach(System.out::println);

        // java8进阶多字段排序
        System.out.println("\r\njava8进阶多字段排序 >>>> ");
        List<Person> java83 = Lists.newArrayList(users);
        java83.sort(Comparator.comparing(Person::getName)
                .thenComparing(Person::compareTo)
                .thenComparing(Person::getCredits));
        java83.forEach(System.out::println);

    }

    private static class Person {
        private String name;
        private Integer age;
        private Integer credits;

        public Person(String name, Integer age, Integer credits) {
            this.name = name;
            this.age = age;
            this.credits = credits;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Integer getCredits() {
            return credits;
        }

        public void setCredits(Integer credits) {
            this.credits = credits;
        }

        public static int compareTo(Person o1, Person o2){
            return o1.getAge().compareTo(o2.getAge());
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", age=" + age + ", credits=" + credits + '}';
        }
    }
}
