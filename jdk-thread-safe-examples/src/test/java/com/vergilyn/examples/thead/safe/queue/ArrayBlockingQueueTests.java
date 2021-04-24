package com.vergilyn.examples.thead.safe.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-24
 */
public class ArrayBlockingQueueTests {

	private static final int SIZE = 10_000;
	private static final ArrayBlockingQueue<Integer> QUEUE = new ArrayBlockingQueue<>(SIZE);

	private static final AtomicInteger index = new AtomicInteger(0);

	/**
	 *
	 */
	@Test
	public void readWrite(){
		ExecutorService writePool = Executors.newFixedThreadPool(100, r -> {
			Thread thread = new Thread(r);
			thread.setName("thread-write");
			return thread;
		});

		for (int i = 0; i < SIZE; i++) {
			writePool.submit(() -> QUEUE.offer(index.incrementAndGet()));
		}

		ExecutorService readPool = Executors.newFixedThreadPool(100, r -> {
			Thread thread = new Thread(r);
			thread.setName("thread-read");
			return thread;
		});

		AtomicInteger readTimes = new AtomicInteger(0);

		while (readTimes.intValue() <= SIZE + 1000){
			readPool.submit(() -> {
				System.out.printf("%d: %s \n", readTimes.incrementAndGet(), QUEUE.poll());
			});
		}
	}
}
