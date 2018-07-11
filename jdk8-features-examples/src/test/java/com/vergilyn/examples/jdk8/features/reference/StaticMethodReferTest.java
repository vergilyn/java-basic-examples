package com.vergilyn.examples.jdk8.features.reference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.google.common.base.Supplier;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * method-reference，静态方法引用：`Class::static_method`
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class StaticMethodReferTest {

    @Test
    public void test(){
        Supplier<LocalDateTime > now = StaticMethodReferTest::now;
        System.out.println("test: " + now.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

        List<Integer> list = Lists.newArrayList(2, 1, 3, 4);
        list.forEach(StaticMethodReferTest::increment);
    }

    private static LocalDateTime now(){
        System.out.println("static-method now()");
        return LocalDateTime .now();
    }

    private static int increment(int x){
        System.out.println("static-method increment(x): " + x);
        return x + 1;
    }
}
