package com.vergilyn.examples.jdk8.features.lambda;

import java.util.Comparator;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class LambdaBasicTest {

    private List<Integer> initList;

    @BeforeClass
    public void init(){
        initList = Lists.newArrayList(2, 1, 3, 4);
    }

    @Test
    public void test(){

        // 匿名内部类
        List<Integer> inner = Lists.newArrayList(initList);
        inner.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1.compareTo(o2);
            }
        });
        System.out.println("inner: " + inner);

        // lambda
        List<Integer> lambda = Lists.newArrayList(initList);
        lambda.sort((o1, o2) -> o1.compareTo(o2));
        System.out.println("lambda: " + lambda);

        // function, 引用静态方法, Class::staticMethodName
        List<Integer> reference = Lists.newArrayList(initList);
        reference.sort(LambdaBasicTest::compareTo);
        System.out.println("reference: " + lambda);

    }

    public static int compareTo(Integer o1, Integer o2) {
        return o1.compareTo(o2);
    }
}
