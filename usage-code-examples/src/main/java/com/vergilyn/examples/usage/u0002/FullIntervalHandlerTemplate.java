package com.vergilyn.examples.usage.u0002;

import java.util.Collection;

public interface FullIntervalHandlerTemplate<T> {

	boolean add(T data);
	boolean addAll(Collection<T> data);

	void manualNotifyFlush(int limit);
}
