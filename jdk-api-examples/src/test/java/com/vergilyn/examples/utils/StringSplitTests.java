package com.vergilyn.examples.utils;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		// 返回类型: java.util.Collections$UnmodifiableRandomAccessList
		List<String> strings = Splitter.on(SEPARATOR_CHAR).trimResults().splitToList(string);

		print(strings.toArray());

		// java.lang.UnsupportedOperationException
		// strings.add("5");

		List<String> arrayList = Lists.newArrayList(strings);
		arrayList.add("5");
		print(arrayList.toArray());
	}

	@Test
	public void guavaToSet(){
		// `guava: 31.1-jre` 支持 splitToStream。
		Stream<String> strings = Splitter.on(SEPARATOR_CHAR).trimResults().splitToStream(string);

		Set<String> sets = strings.collect(Collectors.toSet());

		print(sets.toArray());
	}


	private void print(Object[] arrays){
		for (int i = 0; i < arrays.length; i++) {
			System.out.printf("[%d]:\"%s\"\n", i, arrays[i].toString());
		}
	}
}
