package com.vergilyn.examples.jdk8.features.function;

import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

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
        System.out.println("predicate: " + predicate.test("vergilyn"));
    }
}
