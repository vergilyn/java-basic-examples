package com.vergilyn.examples.jdk8.features.stream;

import cn.hutool.core.collection.ListUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.ListUtils;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	/**
	 * @see com.google.common.collect.Lists#partition(List, int)
	 * @see org.apache.commons.collections4.ListUtils#partition(List, int)
	 * @see cn.hutool.core.collection.ListUtil#partition(List, int)
	 */
	@Test
	public void partition(){
		List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7);

		List<List<Integer>> guava = Lists.partition(list, 3);

		List<List<Integer>> apache = ListUtils.partition(list, 3);

		List<List<Integer>> hutool = ListUtil.partition(list, 3);

		// 因为是 `Map<Boolean, List<T>>`，
		Map<Boolean, List<Integer>> jdk = list.stream().collect(Collectors.partitioningBy(integer -> integer > 3));
	}
}
