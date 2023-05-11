package com.vergilyn.examples.usage.u0014;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class IntToByteArrayTests {

	/**
	 * <a href="https://stackoverflow.com/questions/1936857/convert-integer-into-byte-array-java">
	 *     Convert integer into byte array (Java)</a>
	 */
	@Test
	public void test(){
		int num = 2023;
		Integer integer = num;
		String str = integer + "";

		System.out.println("ByteBuffer >>>> " + ArrayUtils.toString(ByteBuffer.allocate(4).putInt(integer).array()));
		System.out.println("string >>>> " + ArrayUtils.toString(str.getBytes()));
	}
}
