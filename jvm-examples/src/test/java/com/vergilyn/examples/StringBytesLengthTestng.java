package com.vergilyn.examples;

import java.nio.charset.StandardCharsets;

import org.testng.annotations.Test;

/**
 * <a href="https://mp.weixin.qq.com/s/qU5U6S_hNKJ3N2wjkRqHOQ">一个字符串中到底能有多少个字符? 我竟然算错了！</a>
 * 1. 普通的中文字：字符串的长度是2，每个中文字按UTF-8编码是三个字节，字符数组的长度看起来也没问题 <br/>
 *
 * 2. emojis字符：我们设置了两个emojis字符，男女头像。结果字符串的长度是4, UTF-8编码8个字节，字符数组的长度是4 <br/>
 *
 * 3. 生僻的中文字：我们设置了两个中文字，其中一个是生僻的中文字。结果字符串的长度是3， UTF-8编码7个字节，字符数组的长度是3 <br/>
 * @author vergilyn
 * @date 2020-02-04
 */
public class StringBytesLengthTestng {
    private static final String SIMPLIFIED_CHINESE = "淡无欲";
    public static final String EMOJI = "\uD83D\uDC66\uD83D\uDC69";

    @Test
    public void test(){
        System.out.println("1. string length =" + SIMPLIFIED_CHINESE.length());  // 3
        System.out.println("1. string bytes length =" + SIMPLIFIED_CHINESE.getBytes().length);  // 9
        System.out.println("1. string bytes(utf-8) length =" + SIMPLIFIED_CHINESE.getBytes(StandardCharsets.UTF_8).length);  // 9
        System.out.println("1. string bytes(utf-16) length =" + SIMPLIFIED_CHINESE.getBytes(StandardCharsets.UTF_16).length);  // 8
        System.out.println("1. string char length =" + SIMPLIFIED_CHINESE.toCharArray().length);  // 3
        System.out.println();

        System.out.println("2. string length =" + EMOJI.length());  // 4
        System.out.println("2. string bytes length =" + EMOJI.getBytes().length);  // 8
        System.out.println("2. string bytes(utf-8) length =" + EMOJI.getBytes(StandardCharsets.UTF_8).length);  // 8
        System.out.println("2. string bytes(utf-16) length =" + EMOJI.getBytes(StandardCharsets.UTF_16).length);  // 10
        System.out.println("3. string char length =" + EMOJI.toCharArray().length);  // 4
        System.out.println();

        System.out.println(Double.MAX_VALUE);
    }
}
