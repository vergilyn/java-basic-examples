package com.vergilyn.examples;

import org.testng.annotations.Test;

/**
 * 操作运算符
 * @author VergiLyn
 * @date 2020-01-02
 * @see <a href="https://www.runoob.com/java/java-operators.html">Java 运算符</a>
 */
public class OperatorTest {

    @Test
    public void test1(){
        int capacity = 16;
        float factory = 0.75F;
        float ft = ((float)capacity / factory) + 1.0F;
        System.out.println(ft);
    }

    @Test(description = "或运算 | 规")
    public void test2(){
        // boolean 有true则为true
        boolean bool = false;
        bool |= false;
        System.out.println(bool);  // false

        bool |= true;
        System.out.println(bool);  // true

        /* 有一个为1，则为1
         * 4 = 0100
         * 8 = 1000
         * 12 = 1100
         */
        int ia = 4, ib = 8;
        System.out.printf(" 4 | 8 = " + (ia | ib));
    }

    @Test(description = "右移位运算符 >>；左移位运算符 <<")
    public void test3(){
        // 右位移运算符为>>，规则：按二进制形式把所有的数字向右移动对应的位数，低位移出（舍弃），高位的空位补零。
        // 左移位运算符为<<，规则：按二进制形式把所有的数字向左移动对应的位数，高位移出（舍弃），低位的空位补零。

        // n >> 1，等价于 n/2
        System.out.println(1 >> 1);

        // n << 1，等价于 n * 2
        System.out.println("1 << 1 = " + (1 << 1));
        System.out.println("3 << 1 = " + (3 << 1));

        // 1 << n，等价于 2^n
        System.out.println("1 << 4 = " + (1 << 4));  // 16
        System.out.println("1 << 30 = " + (1 << 30)); // 1_073_741_824


        // `>>` 带符号右移。正数右移高位补0，负数右移高位补1。
        // `>>>` 无符号右移。无论是正数还是负数，高位通通补0。
        System.out.println("1 >>> 16 = " + (1 >>> 16));
        System.out.println("16 >>> 16 = " + (16 >>> 16));
        System.out.println("32 >>> 16 = " + (32 >>> 16));
    }
}
