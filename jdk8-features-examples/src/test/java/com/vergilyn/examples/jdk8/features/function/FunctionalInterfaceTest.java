package com.vergilyn.examples.jdk8.features.function;

import org.testng.annotations.Test;

/**
 * jdk8提供的functional-interface在package：{@linkplain java.util.function}。
 * <p>{@link FunctionalInterface @FunctionalInterface}主要用于编译级错误检查，加上该注解，当你写的接口不符合函数式接口定义的时候，编译器会报错。
 *
 * <p>函数式接口，首先是一个接口，其次这个接口里面只能有一个抽象方法（允许定义default-method、static-method）。即Single Abstract Method interfaces。
 *
 * <p>参考：
 *     <ol>
 *         <li><a href="https://www.cnblogs.com/runningTurtle/p/7092632.html">Java 8 函数式接口 - Functional Interface</a></li>
 *     </ol>
 *
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 * @see java.util.function.Function
 */
public class FunctionalInterfaceTest {

    @Test
    public void function(){

    }
}
