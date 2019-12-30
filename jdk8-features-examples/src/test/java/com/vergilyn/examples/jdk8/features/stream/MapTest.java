package com.vergilyn.examples.jdk8.features.stream;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author VergiLyn
 * @date 2019-12-10
 */
public class MapTest {

    /**
     * {@link Stream#map(Function) } 与 {@link Stream#flatMap(Function)} 的区别
     * <p>
     * 1. (重点) 结果集不同
     *
     */
    @Test
    public void mapVsFlatmap(){
        List<String> list = Lists.newArrayList("hello you","hello me","hello world");

        List<String[]> map = list.stream().map(this::split).collect(Collectors.toList());

        List<String> flatmap = list.stream().flatMap(s -> Stream.of(split(s))).collect(Collectors.toList());

        System.out.println(map);
        System.out.println(flatmap);
    }

    protected String[] split(String str){
        return str.split(" ");
    }
}
