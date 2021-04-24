package com.vergilyn.examples.guava.cache;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SEE: Nacos-2.0.0 `StringPool.class`
 * @author vergilyn
 * @since 2021-04-15
 */
@SuppressWarnings("JavadocReference")
public class CacheExpireAfterAccessTests {

	/**
	 * `expireAfterAccess()`: <br/>
	 *   <p> 1) 访问时(eg. {@linkplain Cache#getIfPresent(Object)})，去校验是否expired
	 *      {@linkplain com.google.common.cache.LocalCache.Segment#getLiveEntry(Object, int, long) `getLiveEntry(...)`}。
	 *      <br/>如果expired，则remove <b>当前segment?</b>中所有失效的key-value
	 *      {@linkplain com.google.common.cache.LocalCache.Segment#expireEntries(long) `expireEntries(now)`}
	 *
	 *    <p> 2) 写入时(eg. {@linkplain Cache#put(Object, Object)})，也会通过
	 *      {@linkplain com.google.common.cacheLocalCache.Segment#preWriteCleanup(long) `preWriteCleanup(now)`}
	 *      触发调用 `expireEntries(now)`
	 */
	@SneakyThrows
	@Test
	public void expireAfterAccess(){
		Cache<String, LocalTime> cache = CacheBuilder.newBuilder()
							.maximumSize(10)
							.expireAfterAccess(5, TimeUnit.SECONDS)
							.build();

		LocalTime va = LocalTime.now();
		cache.put("a", va);

		TimeUnit.SECONDS.sleep(2);

		LocalTime vb = LocalTime.now();
		cache.put("b", vb);

		TimeUnit.SECONDS.sleep(4);

		System.out.println("key[a] = " + cache.getIfPresent("a"));
		System.out.println("key[b] = " + cache.getIfPresent("b"));

		assertThat(cache.getIfPresent("a")).isNull();
		assertThat(cache.getIfPresent("b")).isSameAs(vb);

		TimeUnit.SECONDS.sleep(10);

	}

}
