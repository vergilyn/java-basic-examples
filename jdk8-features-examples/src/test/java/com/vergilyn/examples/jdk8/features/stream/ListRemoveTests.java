package com.vergilyn.examples.jdk8.features.stream;

import java.util.List;

import com.google.common.collect.Lists;

import org.junit.jupiter.api.Test;

public class ListRemoveTests {

	@Test
	public void removeIf(){
		List<String> list = Lists.newArrayList("1", "2", "A1", "B2");

		// true: remove  （filter 是 true 保留）
		list.removeIf(s -> s.contains("1"));

		System.out.println(list);
	}
}
