package com.vergilyn.examples;

import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class ListIndexOfTest {

	@Test
	public void valid(){
		List<Integer> list1 = Lists.newArrayList();
		List<Integer> list2 = Lists.newArrayList();

		for (int i = 0; i <= 10; i++){
			int nextInt = RandomUtils.nextInt(1_000, 10_000);
			list1.add(new Integer(nextInt));
			list2.add(new Integer(nextInt));
		}

		// contains 通过 indexOf判断，indexOf 最终是调用 equal。
		list1.forEach(integer -> {
			System.out.printf(" %d >>>> indexOf: %d, contain: %b \n",
						integer, list2.indexOf(integer), list2.contains(integer));
		});
	}
}
