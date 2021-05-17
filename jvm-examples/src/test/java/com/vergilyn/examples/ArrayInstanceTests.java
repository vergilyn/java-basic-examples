package com.vergilyn.examples;

import java.lang.reflect.Array;

import org.junit.jupiter.api.Test;

/**
 * <a href="https://www.cnblogs.com/minghaiJ/p/11259318.html">Java中创建泛型数组</a>
 *
 * @author vergilyn
 * @since 2021-04-30
 */
public class ArrayInstanceTests {

	@Test
	public void arrayNewInstance(){
		
	}


	private <T> T[] newInstance(Class<T> clazz, int size){
		return (T[]) Array.newInstance(clazz.getComponentType(), size);
	}
}
