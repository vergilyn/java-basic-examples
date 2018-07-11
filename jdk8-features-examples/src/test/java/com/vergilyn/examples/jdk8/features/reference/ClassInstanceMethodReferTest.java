package com.vergilyn.examples.jdk8.features.reference;

import java.util.function.BiPredicate;

import org.testng.annotations.Test;

/**
 * method-reference，引用类的实例方法：`Class::instance-method`
 * <p>任意对象（属于同一个类）的实例方法引用，如下示例，引用的是String中任意一个对象的`equals`方法。
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class ClassInstanceMethodReferTest {

    @Test
    public void instance(){
        BiPredicate<String, String> biPre = String::equals;
        // biPre = (str1,str2) -> str1.equals(str2);  // lambda
        System.out.println(biPre.test("vergilyn", "zard"));
    }

    @Test
    public void npe(){
        BiPredicate<String, String> biPre = String::equals;
        System.out.println(biPre.test("zard", null));   // false
        System.out.println(biPre.test(null, "zard"));   // NullPointerException
    }
}
