package com.vergilyn.examples.jdk8.features.function;

import java.util.function.Function;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * java.util.function.Function：只有一个参数（类型T），有返回值（类型R）
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 * @see java.util.function.Function
 */
public class FunctionTest {

    public static String increment(int a){
        return String.valueOf(a + 1);
    }

    public static String operation(int a, Function<Integer, String> action){
        return action.apply(a);
    }

    @Test
    public void function(){
        int x = 1;

        // 匿名内部类写法
        String anonymous = operation(x, new Function<Integer, String>() {
            @Override
            public String apply(Integer a) {
                return String.valueOf(a + 3);
            }
        });
        Assert.assertEquals(anonymous, "4");

        // lambda调用已存在的方法
        String lambdaExists = operation(x, a -> increment(a));
        Assert.assertEquals(lambdaExists, "2");

        // method-reference
        String refer = operation(x, FunctionTest::increment);
        Assert.assertEquals(refer , "2");

        // lambda自定义函数接口
        String lambda = operation(x, a -> String.valueOf(a * 3));
        Assert.assertEquals(lambda, "3");
    }
}
