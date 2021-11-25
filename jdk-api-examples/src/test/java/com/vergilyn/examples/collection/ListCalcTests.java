package com.vergilyn.examples.collection;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListCalcTests {

	@Test
	@DisplayName("并集")
	public void union(){
		List<Integer> first = Lists.newArrayList(1, 2, 3, 4, 5);
		List<Integer> second = Lists.newArrayList(3, 4, 5, 6, 7);

		Collection<Integer> union = CollectionUtils.union(first, second);

		assertThat(union).containsExactlyInAnyOrder(1, 2, 3, 4, 5, 6, 7);
	}

	@Test
	@DisplayName("交集")
	public void intersection(){
		List<Integer> first = Lists.newArrayList(1, 2, 3, 4, 5);
		List<Integer> second = Lists.newArrayList(3, 4, 5, 6, 7);

		Collection<Integer> intersection = CollectionUtils.intersection(first, second);

		assertThat(intersection).containsExactlyInAnyOrder(3, 4, 5);
	}

	@Test
	public void disjunction(){
		List<Integer> first = Lists.newArrayList(1, 2, 3, 4, 5);
		List<Integer> second = Lists.newArrayList(3, 4, 5, 6, 7);

		Collection<Integer> subtract = CollectionUtils.disjunction(first, second);

		assertThat(subtract).containsExactlyInAnyOrder(1, 2, 6, 7);
	}

	@Test
	public void subtract(){
		List<Integer> first = Lists.newArrayList(1, 2, 3, 4, 5);
		List<Integer> second = Lists.newArrayList(3, 4, 5, 6, 7);

		Collection<Integer> subtract = CollectionUtils.subtract(first, second);

		assertThat(subtract).containsExactlyInAnyOrder(1, 2);
	}

	@Test
	public void sum(){
		List<Integer> first = Lists.newArrayList(1, 2, 3, 4, 5);

		System.out.println(first.stream().mapToInt(Integer::intValue).sum());

	}
}
