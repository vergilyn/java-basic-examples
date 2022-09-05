package com.vergilyn.examples.queue;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.testng.collections.Lists;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 期望：监听集合（collect、queue等），当达到期望的 max-elements 时，执行处理方法。
 *
 * @author vergilyn
 * @since 2022-08-29
 */
public class QueueDrainToTests {

	/**
	 * {@link BlockingQueue#drainTo(Collection, int)} 如果 `queue.size < maxElements` 也会立即返回，<b>并不会造成阻塞</b>。
	 */
	@ParameterizedTest
	@ValueSource(ints = {4, 8})
	public void test(int limit){
		BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(limit * 2);

		for (int i = 0; i < limit; i++) {
			queue.offer(i);
		}

		int drainMaxEles = 5;
		List<Integer> drains = Lists.newArrayList();
		// 并不会 阻塞。
		queue.drainTo(drains, drainMaxEles);

		System.out.printf("drains >>>> limit: %d, drains-max: %d, size: %d, eles: %s, queue: %s \n",
		                  limit, drainMaxEles, drains.size(), JSON.toJSONString(drains), JSON.toJSONString(queue));

	}
}
