package com.vergilyn.examples.jdk8.features.function;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

/**
 * java.util.function.Predicate(断定)：接收一个泛型T的参数，返回一个boolean。
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/12
 * @see java.util.function.Predicate
 */
public class PredicateTest {

    @Test
    public void predicate(){
        Predicate<String> predicate = name -> StringUtils.equals(name, "zard");

        Assertions.assertThat(predicate.test("vergilyn")).isFalse();
        Assertions.assertThat(predicate.test("zard")).isTrue();
    }

    @Test
    public void and(){
        Predicate<String> contain = name -> StringUtils.contains(name, "zard");
        Predicate<String> gtSize = name -> name.length() > 5;

        Assertions.assertThat(contain.and(gtSize).test("123zard456")).isTrue();
        Assertions.assertThat(contain.and(gtSize).test("zard")).isFalse();
        Assertions.assertThat(contain.and(gtSize).test("vergilyn")).isFalse();
    }

    @Test
    public void or(){
        Predicate<String> contain = name -> StringUtils.contains(name, "zard");
        Predicate<String> gtSize = name -> name.length() > 5;

        Assertions.assertThat(contain.or(gtSize).test("123zard456")).isTrue();
        Assertions.assertThat(contain.or(gtSize).test("zard")).isTrue();
        Assertions.assertThat(contain.or(gtSize).test("vergilyn")).isTrue();
    }
}
