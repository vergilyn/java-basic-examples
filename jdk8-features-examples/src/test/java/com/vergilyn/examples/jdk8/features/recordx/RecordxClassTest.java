package com.vergilyn.examples.jdk8.features.recordx;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class RecordxClassTest {


    @Test
    void test_recordx() {

        Person p1 = new Person(1001, "p1", TypeEnum.A);
        System.out.println("p1 >>>> " + p1);

        Person p1x = new Person(1001, "p1x", TypeEnum.A);
        Person p10 = new Person(1001, "p1", TypeEnum.A);

        Assertions.assertThat(p1).isNotEqualTo(p1x);
        Assertions.assertThat(p1).isEqualTo(p10);

        Map<Person, Integer> map = Map.of(p1, 1);
        Assertions.assertThat(map).containsKey(p10);
    }


    private record Person(Integer id, String name, TypeEnum type) {

    }

    private enum TypeEnum {
        A, B, C
    }
}
