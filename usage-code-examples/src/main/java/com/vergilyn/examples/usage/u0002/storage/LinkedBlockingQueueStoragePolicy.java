package com.vergilyn.examples.usage.u0002.storage;

import java.util.concurrent.LinkedBlockingQueue;

public class LinkedBlockingQueueStoragePolicy<T> extends AbstractQueueStoragePolicy<T> {

	public LinkedBlockingQueueStoragePolicy() {
		super(new LinkedBlockingQueue<>());
	}

}
