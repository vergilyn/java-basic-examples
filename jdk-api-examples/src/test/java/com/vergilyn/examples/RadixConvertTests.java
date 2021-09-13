package com.vergilyn.examples;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 二进制（binary/BIN）、八进制（Octal/OTC）、十进制（Decimal/DEC）、十六进制（Hexadecimal/HEX）
 * <p>
 *   1. 最高位为符号位，“0”表示正，“1”表示负，其余位表示数值的大小。
 * </p>
 * @author VergiLyn
 * @date 2020-01-07
 */
public class RadixConvertTests {

    @Test
    public void stringToBinary(){
        String str = "Welcome";

        char[] chars = str.toCharArray();

        StringBuilder result = new StringBuilder();
        for (char aChar : chars) {
            result.append(Integer.toBinaryString(aChar));
        }

        System.out.println(result);
    }

    /**
     * TODO 2021-08-10 {@linkplain String#getBytes()} 与 {@linkplain String#toCharArray()} 区别？
     */
    @Test
    public void todo(){
        String str = "abcd42";

        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        char[] chars = str.toCharArray();

        // [97, 98, 99, 100, 52, 50]
        System.out.println("bytes >>>> " + Arrays.toString(bytes));

        // [a, b, c, d, 4, 2]  对应 ascii
        System.out.println("chars >>>> " + Arrays.toString(chars));
    }

    @Test
    @DisplayName("负数的二进制表示")
    public void negative(){
        /* https://blog.csdn.net/xushiyu1996818/article/details/83269526
         * 原码：就是二进制定点表示法，即最高位为符号位，“0”表示正，“1”表示负，其余位表示数值的大小。
         * 反码：正数的反码与其原码相同；负数的反码是对其原码逐位取反，但符号位除外。
         * 补码：正数的补码与其原码相同；负数的补码是在其反码的末位加1。
         */
        System.out.println("1 的二进制: " + Integer.toBinaryString(1));

        /* -1: 1111_1111_1111_1111_1111_1111_1111_1111
         * 1) 原码：1000_0000_0000_0000_0000_0000_0000_0001
         * 2) 反码：1111_1111_1111_1111_1111_1111_1111_1110
         * 3) 补码：1111_1111_1111_1111_1111_1111_1111_1111
         */
        System.out.println("-1 的二进制: " + Integer.toBinaryString(-1));

    }
}
