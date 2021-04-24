package com.vergilyn.examples.usage.u0002.storage;

import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author vergilyn
 * @since 2021-04-24
 */
public class ArrayBlockingQueueStoragePolicy<T> extends AbstractQueueStoragePolicy<T> {

	public ArrayBlockingQueueStoragePolicy() {
		// VFIXME 2021-04-24 ArrayBlockingQueue 并不会扩容，当queue-full时，`add` return false。
		// 由于VM限制，不能设置成 Integer.MAX_VALUE，`OutOfMemoryError: Requested array size exceeds VM limit`
		// 若过大，可能`OutOfMemoryError: Java Heap Space`
		super(new ArrayBlockingQueue<T>(1000));
	}

}
