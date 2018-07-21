package com.vergilyn.examples.jdk8.features.lambda;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 *
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class LambdaBasicTest {

    private List<Integer> initList = Lists.newArrayList(2, 1, 3, 4);

    @Test
    public void basic(){
        // 类型声明
        MathOperation addition = (int a, long b) -> a + b;

        // 不用类型声明
        MathOperation subtraction = (a, b) -> a - b;

        // 大括号中的返回语句
        MathOperation multiplication = (int a, long b) -> { return a * b; };

        // 没有大括号及返回语句
        MathOperation division = (int a, long b) -> a / b;

        System.out.println("10 + 5 = " + this.operate(10, 5L, addition));
        System.out.println("10 - 5 = " + this.operate(10, 5L, subtraction));
        System.out.println("10 x 5 = " + this.operate(10, 5L, multiplication));
        System.out.println("10 / 5 = " + this.operate(10, 5L, division));

        // 如果只有1个参数，可以省略参数的"(...)";
        // 如果方法体只有一个表达式，可以省略方法体的"{...}" 和 "return"关键字。
        Function<String, String> lambda = s -> "return " + s ;
        System.out.println(lambda.apply("lambda"));

        Function<String, String> lambda2 = s -> {
            System.out.println("lambda2");
            return "return " + s ;
        };
        System.out.println(lambda2.apply("lambda2"));

    }

    private long operate(int a, long b, MathOperation mathOperation){
        return mathOperation.operation(a, b);
    }

    interface MathOperation {
        long operation(int a, long b);
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
