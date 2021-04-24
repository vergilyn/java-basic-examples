package com.vergilyn.examples.usage.u0002.storage;

import java.util.Collection;
import java.util.List;
import java.util.Queue;

public interface StoragePolicy<T> {
	boolean isEmpty();

	int size();

	/**
	 * Retrieves and removes the head of this storage,
	 * or returns {@code null} if this storage is empty.
	 *
	 * @param limit -1: get all
	 * @return the head of this storage, or {@code null} if this storage is empty
	 *
	 * @see Queue#poll()
	 */
	List<T> poll(int limit);

	boolean add(T data);

	boolean addAll(Collection<T> data);

	boolean addFirst(Collection<T> data);

	boolean addLast(Collection<T> data);
}
