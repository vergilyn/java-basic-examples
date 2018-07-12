package com.vergilyn.examples.jdk8.features.function;

import java.util.function.Supplier;

import org.testng.annotations.Test;

/**
 * java.util.function.Supplier(提供者)：没有参数，返回一个泛型值。例如for-each。
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/12
 * @see java.util.function.Supplier
 * @see java.util.function.Consumer
 */
public class SupplierTest {

    @Test
    public void supplier(){
        Supplier<String> supplier = () -> "supplier return!";
        System.out.println(supplier.get());
    }
}
