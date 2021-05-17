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
@SuppressWarnings("JavadocReference")
public class CacheRemovalListenerTests {
	private final RemovalListener<Integer, LocalTime> removalListener = notification ->
			System.out.printf("#removalListener() >>>> key: %s, value: %s, cause: %s \n",
					notification.getKey(), notification.getValue(), notification.getCause());

	private final RemovalListener<Integer, LocalTime> asyncRemovalListener = RemovalListeners
			.asynchronous(removalListener, Executors.newFixedThreadPool(1));

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

	/**
	 * 1) removal-listener 方法是被<b>同步调用</b>的（在移除缓存的那个线程中执行，例如put/invalidate/invalidateAll）。
	 * 如果removal-listener比较耗时，会导致调用者线程阻塞时间变长。
	 *
	 * 2) 创建cache的时候只能添加1个监听器，这个监听器对象会被<b>多个线程共享</b>，所以如果监听器需要操作共享资源，那么一定要做好同步控制。
	 */
	@SneakyThrows
	@Test
	public void syncExecListener(){
		Cache<Integer, LocalTime> cache = CacheBuilder.newBuilder()
				.maximumSize(2)
				.expireAfterAccess(1, TimeUnit.SECONDS)
				.removalListener(notification -> {
					try {
						System.out.printf("[%s] >>>> removal-listener begin: key = %s\n",
								LocalTime.now(), notification.getKey());

						TimeUnit.SECONDS.sleep(5);

						System.out.printf("[%s] >>>> removal-listener end:  key = %s\n",
								LocalTime.now(), notification.getKey());
					} catch (InterruptedException e) {
					}
				})
				.build();

		cache.put(1, LocalTime.now());

		TimeUnit.SECONDS.sleep(2);

		cache.put(2, LocalTime.now());

		cache.invalidateAll();

	}

	/**
	 *
	 */
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

	/**
	 * why not invoke `removalListener`?
	 * <p>
	 *   1) {@linkplain com.google.common.cache.LocalCache.Segment#get(java.lang.Object, int) `getIfPresent(...)`}
	 *     -> {@linkplain com.google.common.cache.LocalCache.Segment#postReadCleanup() `postReadCleanup(...)`}
	 *     <blockquote>
	 *        Performs routine cleanup following a read. <b>Normally cleanup happens during writes</b>.
	 *        If cleanup is not observed after a sufficient number of reads, try cleaning up from the read thread.
	 *     </blockquote>
	 *   2) {@linkplain com.google.common.cache.LocalCache#DRAIN_THRESHOLD DRAIN_THRESHOLD}
	 * </p>
	 *
	 * 因为expired发生在read-stage，且`segment.readCount = 1`，不满足`postReadCleanup(...)`中的
	 * `(readCount.incrementAndGet() & DRAIN_THRESHOLD) == 0`（readCount % 64 == 0），
	 * 所以不会进行`cleanUp`（但是返回null），也不会notify-removal-listener。
	 */
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

		LocalTime value = cache.getIfPresent(1);
		System.out.printf("key: %s, value: %s", 1, value);

		TimeUnit.SECONDS.sleep(2);

		/** {@linkplain com.google.common.cache.LocalCache#DRAIN_THRESHOLD}*/
		int enoughTimes = 0x3F;  // 63
		System.out.println("for trigger removal-listener, invoke `getIfPresent()` enough-times >>>> begin");
		for (int i = 0; i < enoughTimes; ) {
			cache.getIfPresent(1);
			System.out.println("times: " + (++i + 1));  // 前面已经调用过1次
		}
		System.out.println("for trigger removal-listener, invoke `getIfPresent()` enough-times >>>> end");

	}
}
