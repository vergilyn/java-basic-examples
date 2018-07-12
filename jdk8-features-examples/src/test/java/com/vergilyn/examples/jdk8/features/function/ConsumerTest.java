package com.vergilyn.examples.jdk8.features.function;

import java.util.function.Consumer;

import org.testng.annotations.Test;

/**
 * java.util.function.Consumer(消费者)：接收一个泛型T的参数，没有返回值。例如for-each。
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 * @see java.util.function.Consumer
 * @see java.util.function.Supplier
 */
public class ConsumerTest {

    @Test
    public void consumer(){
        Consumer<String> anonymous = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        anonymous.accept("anonymous");

        Consumer<String> lambda = s -> System.out.println(s);
        lambda.accept("lambda");

        Consumer<String> refer = System.out::println;
        refer.accept("refer");
    }
}
