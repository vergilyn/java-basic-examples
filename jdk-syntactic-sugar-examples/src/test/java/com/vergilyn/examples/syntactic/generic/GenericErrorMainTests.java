package com.vergilyn.examples.syntactic.generic;

import java.util.concurrent.ThreadLocalRandom;

import com.sun.org.glassfish.gmbal.Description;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author vergilyn
 * @see <a href="https://www.cnblogs.com/dengchengchao/p/9717097.html">Java 干货之深入理解Java泛型</a>
 * @since 2021-04-07
 */
public class GenericErrorMainTests {

	// 泛型擦除后，实际等价于 `Object toArray(Object[] args)`
	final <T> T[] toArray(T... args) {
		return args;
	}

	// 泛型擦除后，实际等价于 `Object[] toArray(Object a, Object b, Object c)`
	<T> T[] pickTwo(T a, T b, T c) {
		switch (ThreadLocalRandom.current().nextInt(3)) {
			case 0:
				return toArray(a, b);  // 可变参数会根据调用类型转换为对应的数组，这里a,b,c都是Object
			case 1:
				return toArray(a, c);
			case 2:
				return toArray(b, c);
		}
		throw new AssertionError(); // Can't get here
	}

	/**
	 * <pre>
	 *   摘自：[深入理解Java泛型](https://www.cnblogs.com/dengchengchao/p/9717097.html)
	 *   可以看到，问题就在于可变参数那里，使用可变参数编译器会自动把我们的参数包装为一个数组传递给对应的方法，
	 *   而这个数组的包装在泛型中，会最终翻译为`new Object`，那么`toArray`接受的实际类型是一个`Object[]`，当然不能强制转换为`String[]`
	 *   代码出错的关键点就在于泛型经过擦除后，类型变为了`Object`导致可变参数直接包装出了一个`Object`数组产生的类型转换失败。
	 * </pre>
	 *
	 * 没有理解博客说的含义，但根据测试代码的观察，ClassCastException是因为`toArray()`返回的结果是`Object[]`被强制转换成了`String[]`导致的异常。
	 */
	@Test
	@Description("java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.String;")
	public void error(){
		// right
		Object[] objects = pickTwo("Good", "Fast", "Cheap");
		System.out.println("objects >>>> " + objects);
		assertThat(objects.toString()).startsWith("[Ljava.lang.Object");

		// error
		Assertions.assertThrows(ClassCastException.class, () -> {
			String[] strings = pickTwo("Good", "Fast", "Cheap");
		}).printStackTrace();
	}

	@Test
	public void right(){
		String[] strings = toArray("A", "B", "C");
		System.out.println("strings >>>> " + strings);
		assertThat(strings.toString()).startsWith("[Ljava.lang.String");

		Object[] objects = toArray("A", "B", "C");
		System.out.println("objects >>>> " + objects);
		assertThat(objects.toString()).startsWith("[Ljava.lang.Object");  // error
	}
}
