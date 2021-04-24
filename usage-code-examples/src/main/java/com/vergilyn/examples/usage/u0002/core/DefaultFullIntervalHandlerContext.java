package com.vergilyn.examples.usage.u0002.core;

import javax.annotation.Nullable;

import com.vergilyn.examples.usage.u0002.FlushFailureHandler;
import com.vergilyn.examples.usage.u0002.FlushHandler;
import com.vergilyn.examples.usage.u0002.FullIntervalHandlerContext;
import com.vergilyn.examples.usage.u0002.storage.StoragePolicy;

public class DefaultFullIntervalHandlerContext<T> implements FullIntervalHandlerContext<T> {
	public static final int DEFAULT_THRESHOLD = 100;
	public static final long DEFAULT_INTERVAL_MS = 5000;

	private final Class<T> storageClass;
	private final StoragePolicy<T> storage;

	private final FlushHandler<T> handler;
	private FlushFailureHandler<T> failureHandler;
	private int threshold;
	private long intervalMs;

	public DefaultFullIntervalHandlerContext(Class<T> storageClass, StoragePolicy<T> storage, FlushHandler<T> handler) {
		this(storageClass, storage, handler, DEFAULT_THRESHOLD, DEFAULT_INTERVAL_MS);
	}

	public DefaultFullIntervalHandlerContext(Class<T> storageClass, StoragePolicy<T> storage, FlushHandler<T> handler,
			int threshold, long intervalMs) {
		this.storage = storage;
		this.storageClass = storageClass;
		this.threshold = threshold;
		this.intervalMs = intervalMs;
		this.handler = handler;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public void setIntervalMs(long intervalMs) {
		this.intervalMs = intervalMs;
	}

	public void setFailureHandler(FlushFailureHandler<T> failureHandler) {
		this.failureHandler = failureHandler;
	}

	@Override
	public int getThreshold() {
		return this.threshold;
	}

	@Override
	public long getIntervalMs() {
		return this.intervalMs;
	}

	@Override
	public Class<T> getStorageClass() {
		return this.storageClass;
	}

	@Override
	public StoragePolicy<T> storage() {
		return this.storage;
	}

	@Override
	public FlushHandler<T> handler() {
		return this.handler;
	}

	@Nullable
	@Override
	public FlushFailureHandler<T> failureHandler() {
		return this.failureHandler;
	}
}
