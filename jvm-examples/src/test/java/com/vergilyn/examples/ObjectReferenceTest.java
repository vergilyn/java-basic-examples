package com.vergilyn.examples;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.collections.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectReferenceTest {

    /**
     * 无论是 filter/map 都不会创建新的对象
     */
    @Test
    void testIsNewInstance(){
        Person p1 = new Person(1, "138xxxx0001");
        Person p2 = new Person(2, "138xxxx0002");
        Person p3 = new Person(3, "138xxxx0003");

        List<Person> all = Lists.newArrayList(p1, p2, p3);

        List<Person> even = all.stream().filter(person -> person.id % 2 == 0).collect(Collectors.toList());
        List<Person> odd = all.stream().filter(person -> person.id % 2 != 0).collect(Collectors.toList());

        Assertions.assertThat(even.get(0) == p2).isTrue();
        Assertions.assertThat(odd.get(0) == p1).isTrue();
        Assertions.assertThat(odd.get(1) == p3).isTrue();
    }


    @Data
    @NoArgsConstructor
    private static class Person implements Serializable {
        private Integer id;

        private String mobile;

        public Person(Integer id, String mobile) {
            this.id = id;
            this.mobile = mobile;
        }
    }
}
