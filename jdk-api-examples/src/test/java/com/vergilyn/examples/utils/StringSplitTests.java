package com.vergilyn.examples.utils;

import java.util.List;

import com.google.common.base.Splitter;

import org.junit.jupiter.api.Test;

public class StringSplitTests {
	/** 逗号前后可能存在多余的空格 */
	private final String string = "1, 2, 3 ,4";
	private static final String SEPARATOR_CHAR = ",";

	/**
	 * 保留了 空格。
	 */
	@Test
	public void split(){
		String[] split = string.split(SEPARATOR_CHAR);
		print(split);
	}

	/**
	 * 可以去掉 前后的空格。
	 */
	@Test
	public void guava(){
		List<String> strings = Splitter.on(SEPARATOR_CHAR).trimResults().splitToList(string);

		print(strings.toArray());
	}


	private void print(Object[] arrays){
		for (int i = 0; i < arrays.length; i++) {
			System.out.printf("[%d]:\"%s\"\n", i, arrays[i].toString());
		}
	}
}
