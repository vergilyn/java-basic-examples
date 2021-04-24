package com.vergilyn.examples.guava.cache;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalListeners;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-23
 */
public class CacheRemovalListenerTests {
	private final RemovalListener<Integer, LocalTime> removalListener = notification ->
			System.out.printf("#removalListener() >>>> key: %s, value: %s, cause: %s \n",
					notification.getKey(), notification.getValue(), notification.getCause());

	private final RemovalListener<Integer, LocalTime> asyncRemovalListener = RemovalListeners
			.asynchronous(removalListener, Executors.newFixedThreadPool(1));

	/**
	 * <a href="https://blog.csdn.net/aitangyong/article/details/53127605">Guava Cache 缓存数据被移除后的监听器RemovalListener</a>
	 * <blockquote>
	 *   <p>1. 默认情况下，监听器方法是被<b>同步调用</b>的（在移除缓存的那个线程中执行）。如果监听器方法比较耗时，会导致调用者线程阻塞时间变长。
	 *     例如main线程中调用`invalidate()`，会导致main线程阻塞。
	 *
	 *   <p>2. 创建cache的时候只能添加1个监听器，这个监听器对象会被<b>多个线程共享</b>，所以如果监听器需要操作共享资源，那么一定要做好同步控制。
	 * </blockquote>
	 */
	@SneakyThrows
	@Test
	public void removalListener(){
		Cache<Integer, LocalTime> cache = CacheBuilder.newBuilder()
					.maximumSize(2)
					.expireAfterWrite(1, TimeUnit.SECONDS)
					.removalListener(removalListener)
				.build();

		cache.put(1, LocalTime.now());
		cache.invalidate(1);  // removal-listener, cause: EXPLICIT

		cache.put(2, LocalTime.now());
		cache.put(3, LocalTime.now());
		cache.invalidateAll();  // removal-listener, cause: EXPLICIT

		cache.put(4, LocalTime.now());
		cache.put(5, LocalTime.now());
		cache.put(6, LocalTime.now());  // removal-listener, cause: SIZE

		TimeUnit.SECONDS.sleep(2);
		cache.put(7, LocalTime.now()); // removal-listener, cause: EXPIRED

		TimeUnit.SECONDS.sleep(2);
	}

	@SneakyThrows
	@Test
	public void putTriggerRemovalListener(){
		Cache<Integer, LocalTime> cache = CacheBuilder.newBuilder()
				.maximumSize(2)
				.expireAfterAccess(1, TimeUnit.SECONDS)
				.removalListener(removalListener)
				.build();

		cache.put(1, LocalTime.now());

		TimeUnit.SECONDS.sleep(2);

		cache.put(2, LocalTime.now());

		TimeUnit.SECONDS.sleep(2);
	}

	@SneakyThrows
	@Test
	public void getIfPresentTriggerRemovalListener(){
		Cache<Integer, LocalTime> cache = CacheBuilder.newBuilder()
				.maximumSize(2)
				.expireAfterAccess(1, TimeUnit.SECONDS)
				.removalListener(notification ->
						System.out.printf("#removalListener() >>>> key: %s, value: %s, cause: %s \n",
								notification.getKey(), notification.getValue(), notification.getCause()))
				.build();

		cache.put(1, LocalTime.now());

		TimeUnit.SECONDS.sleep(2);

		// VFIXME 2021-04-23 why not invoke `removalListener`?
		LocalTime value = cache.getIfPresent(1);
		System.out.printf("key: %s, value: %s", 1, value);

		TimeUnit.SECONDS.sleep(2);
	}
}
