package com.vergilyn.examples.jdk8.features.reference;

import java.util.function.Consumer;

import org.testng.annotations.Test;

/**
 * method-reference，对象的方法引用：`instance::method`
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class InstanceMethodReferTest {

    @Test
    public void instance(){
        Consumer<String> instanceMethod = this::instanceMethod;
        instanceMethod.accept("instance");
    }

    public void instanceMethod(String x){
        System.out.println("invoke instance-method: " + x);
    }
}
