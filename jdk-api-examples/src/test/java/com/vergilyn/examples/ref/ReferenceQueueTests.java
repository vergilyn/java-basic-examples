package com.vergilyn.examples.ref;

import java.lang.ref.ReferenceQueue;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-15
 *
 * @see com.google.common.cache.LocalCache.Segment#Segment(com.google.common.cache.LocalCache, int, long, com.google.common.cache.AbstractCache.StatsCounter)
 */
@SuppressWarnings("JavadocReference")
public class ReferenceQueueTests {

	@Test
	public void peek(){
		ReferenceQueue<Integer> ref = new ReferenceQueue<>();

	}
}
