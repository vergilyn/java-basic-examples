package com.vergilyn.examples.usage.u0002;

import java.util.List;

import com.sun.istack.internal.NotNull;

@FunctionalInterface
public interface FlushHandler<T> {

	boolean flush(@NotNull List<T> data);
}
