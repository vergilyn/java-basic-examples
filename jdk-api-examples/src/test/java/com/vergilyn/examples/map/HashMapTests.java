package com.vergilyn.examples.map;

import java.util.Map;

import com.google.common.collect.Maps;

import org.junit.jupiter.api.Test;

public class HashMapTests {

	@Test
	public void putIfAbsent(){
		Map<String, Integer> map = Maps.newHashMap();

		Integer v1 = map.putIfAbsent("a", 1);
		System.out.println(v1);  // null
		System.out.println(map.get("a"));  // 1

		Integer v2 = map.putIfAbsent("a", 2);
		System.out.println(v2);  // null
		System.out.println(map.get("a"));  // 1

	}
}
