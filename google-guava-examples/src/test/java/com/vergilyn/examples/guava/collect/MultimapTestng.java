package com.vergilyn.examples.guava.collect;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @see com.google.common.eventbus.SubscriberRegistry#register(java.lang.Object)
 */
public class MultimapTestng {

	@Test
	public void instance() {
		Multimap<String, Integer> multimap = HashMultimap.create();
		multimap.put("A", 1);
		multimap.put("A", 2);
		// 底层 <=> Collection.add(1000)
		multimap.put("A", 1000);
		multimap.put("A", 1000);

		multimap.put("B", 3);

		String actual = multimap.toString();
		String except = "{A=[1000, 1, 2], B=[3]}";
		System.out.println(actual);

		Assert.assertEquals(actual, except);
	}
}
