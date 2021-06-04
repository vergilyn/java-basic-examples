package com.vergilyn.examples.jvm.allocate;

import java.lang.reflect.Field;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author vergilyn
 * @since 2021-06-04
 */
@SuppressWarnings("JavadocReference")
class IntegerCacheTests {

	/**
	 * @see Integer.IntegerCache
	 */
	@Test
	public void integerCache() {
		Integer a = 127;
		Integer b = 127;

		assertTrue(a == b);

		Integer a1 = 128;
		Integer b1 = 128;
		Assertions.assertFalse(a1 == b1);
	}

	/**
	 * `{@code -XX:AutoBoxCacheMax=<size>} `
	 *
	 * @see Integer.IntegerCache#cache
	 * @see <a href="https://mp.weixin.qq.com/s/FVv4QGgx047h8e6l1JfKJQ">JAVA 实现if(a==1 && a==2 && a==3)，为true</a>
	 */
	@Test
	@SneakyThrows
	public void neuropathy() {
		Class cache = Integer.class.getDeclaredClasses()[0];
		Field c = cache.getDeclaredField("cache");
		c.setAccessible(true);
		Integer[] array = (Integer[]) c.get(cache);

		// array[129] is 1
		array[130] = array[129]; // Set 2 to be 1
		array[131] = array[129]; // Set 3 to be 1

		// 一定要强转成 Integer
		assertTrue((Integer) 1 == (Integer) 2);
		assertTrue((Integer) 1 == (Integer) 3);
		assertTrue((Integer) 2 == (Integer) 3);
	}
}
