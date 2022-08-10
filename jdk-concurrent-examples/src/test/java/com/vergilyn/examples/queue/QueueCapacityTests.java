package com.vergilyn.examples.queue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueCapacityTests {

	/**
	 * `Queue` 指定的是 max-size，（`List`指定的是初始容量，并不是最大容量）
	 *
	 * @see ArrayBlockingQueue#offer(Object)
	 */
	@Test
	public void queueCapacity(){
		ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(2);

		queue.add("1");
		queue.add("2");

		Assertions.assertThrows(IllegalStateException.class,
		                        () -> queue.add("3"),
		                        () -> "Queue full");
	}


	@Test
	public void collectCapacity(){
		List<String> list = new ArrayList<>(2);

		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
	}

}
