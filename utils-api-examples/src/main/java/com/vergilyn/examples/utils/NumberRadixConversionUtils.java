package com.vergilyn.examples.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 1) 只能转换“正整数”
 * @author vergilyn
 * @see <a href="https://www.cnblogs.com/by-lhc/p/11906037.html">java 10进制与62进制相互转换</a>
 * @since 2021-05-10
 */
public class NumberRadixConversionUtils {
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', };

	public static String to62Radix(long num, int minLength) {
		StringBuilder builder = new StringBuilder();
		do {
			int remainder = (int) (num % DIGITS.length);
			builder.append(DIGITS[remainder]);
			num = num / 62;
		} while (num != 0);

		String str = builder.reverse().toString();
		return StringUtils.leftPad(str, minLength, DIGITS[0]);
	}

	public static long toDecimal(String str) {
		// 去掉开头补位的数字
		str = str.replace("^" + DIGITS[0] + "*", "");
		long value = 0;
		char tempChar;
		int tempCharValue;

		for (int i = 0, len = str.length(); i < len; i++) {
			tempChar = str.charAt(i);
			tempCharValue = indexDigits(tempChar);
			//单字符值在进制规则下表示的值
			value += (long) (tempCharValue * Math.pow(DIGITS.length, str.length() - i - 1));
		}
		return value;
	}

	private static int indexDigits(char ch){
		for (int i = 0, len = DIGITS.length; i < len; i++) {
			if (DIGITS[i] == ch){
				return i;
			}
		}

		return -1;
	}
}
