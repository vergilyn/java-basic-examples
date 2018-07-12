package com.vergilyn.examples.jdk8.features.reference;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.testng.annotations.Test;

/**
 * method-reference，对象的方法引用：`instance::method`，`this::method`，`super::method`
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

    @Test
    public void superRefer(){
        Supplier<String> thisSupp = this::toString;
        System.out.println(thisSupp.get());

        Supplier<String> superSupp = super::toString;
        System.out.println(superSupp.get());
    }

    public void instanceMethod(String x){
        System.out.println("invoke instance-method: " + x);
    }

    @Override
    public String toString() {
        return "invoke this-toString()";
    }
}
