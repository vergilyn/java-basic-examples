package com.vergilyn.examples.utils;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class NumberRadixConversionUtilsTests {

	@Test
	public void fill(){
		long num = 10_000;
		assertConversion(num, 6);
	}

	@RepeatedTest(100)
	public void convert(){
		long num = RandomUtils.nextLong(0, Long.MAX_VALUE);
		assertConversion(num, 15);
	}

	protected void assertConversion(long num, int minLength){
		String to62Radix = NumberRadixConversionUtils.to62Radix(num, minLength);

		long decimal = NumberRadixConversionUtils.toDecimal(to62Radix);

		System.out.printf("num: %d, 62Radix: %s, decimal: %d\n", num, to62Radix, decimal);
		Assertions.assertThat(decimal).isEqualTo(num);
	}
}