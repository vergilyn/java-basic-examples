package com.vergilyn.examples.usage.u0002;

import javax.annotation.Nullable;

import com.sun.istack.internal.NotNull;
import com.vergilyn.examples.usage.u0002.storage.StoragePolicy;

public interface FullIntervalHandlerContext<T> {

	@NotNull int getThreshold();

	@NotNull long getIntervalMs();

	@NotNull Class<T> getStorageClass();

	@NotNull StoragePolicy<T> storage();

	@NotNull FlushHandler<T> handler();

	@Nullable FlushFailureHandler<T> failureHandler();
}
