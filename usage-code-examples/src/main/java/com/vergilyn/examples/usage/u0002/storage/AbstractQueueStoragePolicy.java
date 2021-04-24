package com.vergilyn.examples.usage.u0002.storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author vergilyn
 * @since 2021-04-24
 */
public abstract class AbstractQueueStoragePolicy<T> implements StoragePolicy<T> {

	protected final Queue<T> queue;

	public AbstractQueueStoragePolicy(Queue<T> queue) {
		this.queue = queue;
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public List<T> poll(int limit) {
		limit = limit == -1 ? queue.size() : limit;
		List<T> list = new ArrayList<>(limit);

		T data;
		for (int i = 0; i < limit; i++) {
			data = queue.poll();
			if (data == null){
				break;
			}
			list.add(data);
		}

		return list;
	}

	@Override
	public boolean add(T data) {
		return queue.add(data);
	}

	@Override
	public boolean addAll(Collection<T> data) {
		return queue.addAll(data);
	}

	@Override
	public boolean addFirst(Collection<T> data) {
		/* Queue 未提供 addFirst 方法（Deque支持）：
		 *   现在使用场景是，flush失败时，希望将 data 重新放到 head/tail。
		 *   只针对此场景的话，可以先peek，那么此处就不需要操作。（但成功后记得要remove。）
		 *   并且，flush-fail时，如果是TAIL-POLICY，需要移动到tail
		 */
		return addLast(data);
	}

	@Override
	public boolean addLast(Collection<T> data) {
		return queue.addAll(data);
	}
}
