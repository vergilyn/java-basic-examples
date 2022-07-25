package com.vergilyn.examples.jdk8.features.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;

public class OptionalTests {

	@Test
	public void testFeature(){
		// `param = 1`
		Integer param = 1;

		// false 才会执行 `getDefault`。通过class可知，三元表达式其实是 if-else
		int result = param != null ? param : getDefault("三元表达式");

		// 不推荐此写法，因为：始终会调用 `getDefault("Optional#orElse()")`
		int orElse = Optional.ofNullable(param).orElse(getDefault("Optional#orElse()"));

		// 利用 函数接口，只有触发else时才会调用 `getDefault("Optional#orElseGet()")`
		int orElseGet = Optional.ofNullable(param).orElseGet(() -> getDefault("Optional#orElseGet()"));
	}

	private int getDefault(String mark){
		System.out.println("invoke `#getDefault()` method >>>> " + mark);
		return 0;
	}
}
