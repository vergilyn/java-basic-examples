package com.vergilyn.examples.jdk8.features.stream;

import java.util.List;

import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;

public class ListTests {

	@Test
	public void test(){
		final String delimiter = "$";
		List<String> list = Lists.newArrayList("1", "2", "3");

		StringBuilder builder = new StringBuilder();
		for (String s : list) {
			builder.append(s).append(delimiter);
		}

		// 期望有简单的写法 达到该效果
		System.out.println("expected: " + builder.toString());

		// 如果 `list.isEmpty` 结果不一样
		String join = String.join(delimiter, list) + delimiter;
		System.out.println("stream.join >>>> " + join);
	}
}
