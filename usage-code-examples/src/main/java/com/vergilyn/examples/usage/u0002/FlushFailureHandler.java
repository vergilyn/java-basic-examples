package com.vergilyn.examples.usage.u0002;

import java.util.List;

import com.sun.istack.internal.NotNull;

@FunctionalInterface
public interface FlushFailureHandler<T> {

	void flushFailure(@NotNull List<T> data, Throwable throwable);
}
