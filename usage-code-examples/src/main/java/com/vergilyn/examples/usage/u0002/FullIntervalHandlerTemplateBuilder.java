package com.vergilyn.examples.usage.u0002;

import java.util.concurrent.TimeUnit;

import com.vergilyn.examples.usage.u0002.core.DefaultFullIntervalHandlerContext;
import com.vergilyn.examples.usage.u0002.core.DefaultFullIntervalHandlerTemplate;
import com.vergilyn.examples.usage.u0002.storage.LinkedBlockingQueueStoragePolicy;
import com.vergilyn.examples.usage.u0002.storage.StoragePolicy;

import static com.vergilyn.examples.usage.u0002.core.DefaultFullIntervalHandlerContext.DEFAULT_INTERVAL_MS;
import static com.vergilyn.examples.usage.u0002.core.DefaultFullIntervalHandlerContext.DEFAULT_THRESHOLD;

public final class FullIntervalHandlerTemplateBuilder<T> {
	private StoragePolicy<T> storage;
	private Class<T> storageClass;
	private int threshold = DEFAULT_THRESHOLD;
	private long intervalMs = DEFAULT_INTERVAL_MS;
	private FlushHandler<T> handler;
	private FlushFailureHandler<T> failureHandler;

	public FullIntervalHandlerTemplateBuilder(Class<T> storageClass) {
		this.storageClass = storageClass;
	}

	public FullIntervalHandlerTemplateBuilder<T> storage(StoragePolicy<T> storage) {
		this.storage = storage;
		return this;
	}

	public FullIntervalHandlerTemplateBuilder<T> threshold(int threshold) {
		this.threshold = threshold;
		return this;
	}

	public FullIntervalHandlerTemplateBuilder<T> interval(long intervalMs) {
		this.intervalMs = intervalMs;
		return this;
	}

	public FullIntervalHandlerTemplateBuilder<T> interval(long interval, TimeUnit unit) {
		this.intervalMs = TimeUnit.MILLISECONDS.convert(interval, unit);
		return this;
	}

	public FullIntervalHandlerTemplateBuilder<T> handler(FlushHandler<T> handler) {
		this.handler = handler;
		return this;
	}

	public FullIntervalHandlerTemplateBuilder<T> failureHandler(FlushFailureHandler<T> failureHandler) {
		this.failureHandler = failureHandler;
		return this;
	}

	public FullIntervalHandlerTemplate<T> build(){
		if (handler == null){
			throw new IllegalArgumentException("handler must not null!");
		}

		if (this.storage == null){
			storage = new LinkedBlockingQueueStoragePolicy<>();
		}

		DefaultFullIntervalHandlerContext<T> context = new DefaultFullIntervalHandlerContext<>(storageClass, storage, handler);
		context.setFailureHandler(failureHandler);
		context.setIntervalMs(intervalMs);
		context.setThreshold(threshold);

		return new DefaultFullIntervalHandlerTemplate<>(context);
	}
}
