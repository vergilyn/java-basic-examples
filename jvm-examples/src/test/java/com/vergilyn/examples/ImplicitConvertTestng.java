package com.vergilyn.examples;

import org.testng.annotations.Test;

/**
 * @author vergilyn
 * @date 2020-05-11
 */
public class ImplicitConvertTestng {

    /** 69、a=a+b与a+=b有什么区别吗?
     * <pre>
     *   += 操作符会进行隐式自动类型转换 ,此处a+=b隐式的将加操作的结果类型强制转换为持有结果的类型,
     *   而a=a+b则不会自动进行类型转换.
     * </pre>
     */
    @Test
    public void test(){
        byte a = 127;
        byte b = 127;
        // b = a + b; // 编译错误:cannot convert from int to byte

        b += a; // `+=` 会隐式转换
        System.out.println("b = " + b);  // b = -2


        short s1= 1;
        // s1 = s1 + 1;  // 编译错误，此处的`1`表示 int，`+`运算后类型提升为int。
        s1 += 1;  // 会对右边的表达式结果强转匹配左边的数据类型
        System.out.println("s1 = " + s1);  // s1 = 2
    }
}
