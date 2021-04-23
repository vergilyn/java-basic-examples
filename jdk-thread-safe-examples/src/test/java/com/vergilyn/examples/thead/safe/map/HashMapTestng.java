package com.vergilyn.examples.thead.safe.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.SneakyThrows;
import org.testng.annotations.Test;

/**
 * 1. <a href="https://www.jianshu.com/p/cd92772c9504">HashMap多线程不安全问题总结</a>
 * <blockquote>
 *     1) 多线程put的时候出现数据丢失 <br/>
 *       在线程A、B put的数据发生hash碰撞并且数组里面已经有数据的情况下，两个线程同时获得头结点的next指针，next指针只能指向一条数据，比如有另外一条数据丢失。
 * </blockquote>
 *
 * @author vergilyn
 * @since 2021-04-15
 */
public class HashMapTestng {
	private static final int size = 10_000;

	/**
	 * <pre>
	 *   造成数据丢失时，输出可能结果：
	 *   0, value = null
	 *   [vergilyn] >>>> counter: 10000, map.size: 9998, map.keySet().size: 9998
	 *   [vergilyn] >>>> include: 9999, exclude: 1
	 *   [0]
	 * </pre>
	 *
	 * 1. `hashmap.size` = `keySet().size` < `expected-size` <br/>
	 * 2. `map.size` + `exclude.size` != `size`
	 */
	@SneakyThrows
	@Test
	public void hashmap(){
		HashMap<Integer, Integer> hashmap = new HashMap<>(size);
		template(hashmap);
	}

	/**
	 * 不会存在数据丢失，并且 `map.size = size`
	 */
	@SneakyThrows
	@Test
	public void concurrentHashMap(){
		ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>(size);
		template(concurrentHashMap);
	}

	private void template(Map<Integer, Integer> map){
		AtomicInteger counter = new AtomicInteger(0);
		concurrentPut(map, size, counter);

		Set<Integer>[] split = split(map, size, null, (key, value) -> {
			System.out.println(key + ", value = " + value);
		});

		Set<Integer> include = split[0];
		Set<Integer> exclude = split[1];

		System.out.printf("[vergilyn] >>>> counter: %d"
						+ ", map.size: %d"
						+ ", map.keySet().size: %d\n",
				counter.get(), map.size(), map.keySet().size());

		System.out.printf("[vergilyn] >>>> include: %d, exclude: %d \n", include.size(), exclude.size());
		System.out.println(exclude);
	}

	private Set<Integer> sub(Set<Integer> expected, Map<Integer, Boolean> actual){
		Set<Integer> sub = Sets.newHashSetWithExpectedSize(expected.size() - actual.size());

		for (Integer integer : expected) {
			if (actual.keySet().contains(integer)){
				continue;
			}
			sub.add(integer);
		}

		return sub;
	}

	@SneakyThrows
	private void concurrentPut(Map<Integer, Integer> source, int size, AtomicInteger counter){
		ExecutorService executor = Executors.newFixedThreadPool(100);

		List<Future<?>> futures = Lists.newArrayListWithCapacity(size * 2);
		Future<?> future;
		for (int i = 0; i < size; i++) {
			future = executor.submit(() -> {
				Integer key = counter.getAndIncrement();
				source.put(key, key);
			});

			futures.add(future);
		}

		futures.forEach(f -> {
			try {
				f.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		TimeUnit.SECONDS.sleep(2);
	}

	private Set<Integer>[] split(Map<Integer, Integer> source, int size,
			BiConsumer<Integer, Integer> includeCallback, BiConsumer<Integer, Integer> excludeCallback){
		// excepted: [0, size - 1]
		Set<Integer> include = Sets.newLinkedHashSetWithExpectedSize(size);
		Set<Integer> exclude = Sets.newLinkedHashSetWithExpectedSize(size);
		for (int i = 0; i < size; i++) {
			Integer value = source.get(i);
			if (value != null){
				include.add(i);
				if (includeCallback != null){
					includeCallback.accept(i, value);
				}
			}else {
				exclude.add(i);
				if (excludeCallback != null){
					excludeCallback.accept(i, value);
				}
			}
		}

		return (Set<Integer>[]) new Set[] { include, exclude };
	}
}
