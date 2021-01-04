package com.vergilyn.examples.jdk8.features.interfaces;

import java.util.Map;
import java.util.function.Supplier;

import org.testng.annotations.Test;
import org.testng.collections.Maps;

/**
 * 例如 spring-cloud-context:2.2.5.RELEASE,
 * <pre>
 *   package org.springframework.cloud.context.scope;
 *
 *   public class GenericScope implements ...{
 *
 *  	public Object get(String name, ObjectFactory<?> objectFactory) {
 *          // 底层实际是 Map.putIfAbsent()
 * 		    BeanLifecycleWrapper value = this.cache.put(name,
 * 				new BeanLifecycleWrapper(name, objectFactory));
 * 		    // ...
 *      }
 *   }
 * </pre>
 *
 * 不管是否需要都会调用constructor，可以通过`FunctionInterface`优化。
 */
public class OptimizeInvalidCodeTestng {

	private static final Map<String, Temp> TEMP_MAP = Maps.newHashMap();

	@Test
	public void test(){
		// 每次都会调用`Temp`的构造函数(先调用constructor，再调用method)
		badMethod("a", new Temp("a"));
		badMethod("a", new Temp("a"));

		System.out.println("================");

		// 使用时才会调用`Temp`的constructor
		optMethod("b", () -> new Temp("b"));
		optMethod("b", () -> new Temp("b"));
	}

	private void badMethod(String key, Temp obj){
		System.out.println("bad-method putIfAbsent >>>> before");

		TEMP_MAP.putIfAbsent(key, obj);

		System.out.println("bad-method putIfAbsent >>>> after");
	}

	private void optMethod(String key, Supplier<Temp> obj){
		System.out.println("opt-method putIfAbsent >>>> before");

		Temp v = TEMP_MAP.get(key);
		if (v == null) {
			TEMP_MAP.put(key, obj.get());
		}

		System.out.println("opt-method putIfAbsent >>>> after");
	}

	static class Temp{
		private String name;

		public Temp(String name) {
			System.out.println("Temp(String name)");
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
