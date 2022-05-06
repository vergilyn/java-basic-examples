package com.vergilyn.examples.usage.u0003;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GenericTypeResolverTests {

	@Test
	public void test(){
		List<Integer> source = null;

		// TODO 2022-03-22 如何获取 真实类型 `Integer.class`？
	}

	@Test
	public void sample(){
		List<Integer> integerList = new ArrayList<Integer>();
		integerList.add(1);

		// 如果`!= null`，那么可以随便取一个值。 例如`list.get(0)` 或 `map.values[0]`
		Class<? extends Integer> clazz = integerList.get(0).getClass();
		System.out.println();
		Assertions.assertEquals(Integer.class, clazz);
	}


	/**
	 * SEE: spring-data `org.springframework.core.GenericTypeResolver`
	 * 以下写法只能是在  class内（ArrayList.class 内）
	 */
	@Test
	public void error(){
		List<Integer> integerList = new ArrayList<Integer>();

		ParameterizedType parameterizedType = ((ParameterizedType) integerList.getClass().getGenericSuperclass());

		Type[] types = parameterizedType.getActualTypeArguments();
		System.out.println(types[0]);
		Assertions.assertEquals(Integer.class, types[0]);
	}
}
