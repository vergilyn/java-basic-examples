package com.vergilyn.examples.usage.u0003;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * SEE: spring-data `org.springframework.core.GenericTypeResolver`
 *
 * @author vergilyn
 * @since 2021-04-24
 */
public class GenericTypeResolver {

	public static void main(String[] args){
		List<Integer> integerList = new ArrayList<Integer>();

		ParameterizedType parameterizedType = ((ParameterizedType) integerList.getClass().getGenericSuperclass());

		Type[] types = parameterizedType.getActualTypeArguments();

		System.out.println(types[0]);
	}
}
