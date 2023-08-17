package com.vergilyn.examples.usage.type;

import org.junit.jupiter.api.Test;
import org.springframework.beans.SimpleTypeConverter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeConvertTests {

    @Test
    void test(){
        Map<String, Class<?>> map = new HashMap<>();
        map.put(Dog.class.getSimpleName(), Dog.class);
        map.put(Cat.class.getSimpleName(), Cat.class);

        Map<String, Animal> data = new HashMap<>();
        data.put(Dog.class.getSimpleName(), new Dog());
        data.put(Cat.class.getSimpleName(), new Cat());

        for (Map.Entry<String, Animal> entry : data.entrySet()) {
            String key = entry.getKey();

            Class<?> clazz = map.get(key);

            Object cast = clazz.cast(entry.getValue());
        }
    }

    /**
     *
     *
     * @see org.springframework.beans.TypeConverter
     */
    @Test
    void test_spring_TypeConverter(){
        // 从 String 到一些基本类型基本都可以。  但 date 的不行
        SimpleTypeConverter converter = new SimpleTypeConverter();

        Long toLong = converter.convertIfNecessary("1234", Long.class);
        assertThat(toLong).isEqualTo(1234L);

        LocalDate localDate = converter.convertIfNecessary("2023-08-17", LocalDate.class);
        System.out.println(localDate);
    }

    private static abstract class Animal {

        public String getName(){
            return this.getClass().getSimpleName();
        }
    }

    private static class Cat extends Animal {

        public String doCat(){
            return "do-cat";
        }
    }

    private static class Dog extends Animal {

        public String doDog(){
            return "do-dog";
        }
    }
}
