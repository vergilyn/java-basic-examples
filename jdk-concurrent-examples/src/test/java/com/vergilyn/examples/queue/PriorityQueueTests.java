package com.vergilyn.examples.queue;

import java.util.Comparator;
import java.util.PriorityQueue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
/**
 *
 * @author vergilyn
 * @since 2021-04-15
 *
 * @see <a href="https://www.liaoxuefeng.com/wiki/1252599548343744/1265120632401152">【廖雪峰】使用PriorityQueue</a>
 */
public class PriorityQueueTests {

	@Test
	public void test(){
		PriorityQueue<String> queue = new PriorityQueue((Comparator<String>) (o1, o2) -> {
			int l1 = Integer.parseInt(StringUtils.substringAfter(o1, "-"));
			int l2 = Integer.parseInt(StringUtils.substringAfter(o2, "-"));
			return l2 - l1;
		});

		queue.add("vip-1");
		queue.add("vip-3");
		queue.add("vip-2");
		queue.add("vip-4");
		queue.add("vip-006");

		do {
			System.out.println(queue.poll());
		}while (!queue.isEmpty());
	}
}
