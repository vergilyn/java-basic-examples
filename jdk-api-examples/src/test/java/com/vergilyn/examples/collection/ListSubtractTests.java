package com.vergilyn.examples.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ListSubtractTests {
	/** [100, 1099] */
	private List<Integer> first;
	/** [100, 149] ∪ [200, 1199] */
	private List<Integer> second;

	@BeforeEach
	private void beforeEach(){
		first = buildList(100, 1000);

		second = buildList(100, 50);
		second.addAll(buildList(200, 1000));

		Collections.shuffle(first);
		Collections.shuffle(second);
	}

	@Test
	public void subtract(){
		Collection<Integer> subtract = CollectionUtils.subtract(second, first);
		System.out.printf("size: %d, subtract: %s", subtract.size(), subtract);

		// expected: [1100, 1199]
		List<Integer> expected = buildList(1100, 100);
		assertThat(subtract).containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	public void disjunction(){
		Collection<Integer> subtract = CollectionUtils.disjunction(second, first);
		System.out.printf("size: %d, disjunction: %s", subtract.size(), subtract);

		// expected: [150, 199] ∪ [1100, 1199]
		List<Integer> expected = buildList(150, 50);
		expected.addAll(buildList(1100, 100));

		assertThat(subtract).containsExactlyInAnyOrderElementsOf(expected);
	}

	private List<Integer> buildList(Integer seed, long limit){
		return Stream.iterate(seed, integer -> ++integer)
						.limit(limit)
						.collect(Collectors.toList());
	}
}
