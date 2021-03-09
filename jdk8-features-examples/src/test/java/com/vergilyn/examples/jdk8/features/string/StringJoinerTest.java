package com.vergilyn.examples.jdk8.features.string;

import java.util.StringJoiner;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-02-25
 */
public class StringJoinerTest {

	@Test
	public void constructor(){
		StringJoiner joiner = new StringJoiner(",");
		joiner.add("1").add("2").add("3");

		System.out.println(joiner.toString());  // 1,2,3

		StringJoiner other = new StringJoiner("-", "[", "]");
		other.add("4").add("5").add("6");

		System.out.println(other.toString());  // [4-5-6]

		StringJoiner merge = joiner.merge(other);
		System.out.println(merge.toString());  // 1,2,3,4-5-6
	}
}
