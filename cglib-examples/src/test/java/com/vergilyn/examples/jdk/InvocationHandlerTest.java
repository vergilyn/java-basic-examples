package com.vergilyn.examples.jdk;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author vergilyn
 * @since 2021-02-24
 */
public class InvocationHandlerTest {

	private static interface Hello {
		void say();
	}

	private static class HelloImpl implements Hello {
		private final Integer index;

		public HelloImpl(Integer index) {
			this.index = index;
		}

		@Override
		public void say(){
			System.out.printf("Hello[%d] >>>> say() \n", this.index);
		}
	}

	/**
	 * see: {@code org.mybatis.spring.SqlSessionTemplate.SqlSessionInterceptor}
	 *
	 * @see com.google.common.util.concurrent.SimpleTimeLimiter
	 */
	@Test
	public void proxyMethod(){
		Hello hello1 = new HelloImpl(1);
		Hello hello2 = new HelloImpl(2);

		AtomicReference<Object> reference = new AtomicReference<>();
		// 只能是 interfaces
		Hello proxyInstance = (Hello)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { Hello.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("HelloInterceptor >>>> proxy: " + proxy.getClass().getName());

						System.out.println("HelloInterceptor >>>> say() before");

						reference.set(proxy);
						// `proxy` 指的是 代理对象`proxyInstance`。而不是 被代理的对象，例如 `hello1 / hello2`，
						// 所以不能这么写，会导致递归无限调用。
						// Object result = method.invoke(proxy, args);
						
						Object result = method.invoke(hello1, args);

						System.out.println("HelloInterceptor >>>> say() after");

						return result;
					}
				});

		proxyInstance.say();

		System.out.printf(" >>>> `proxy == proxyInstance` is %b \n", reference.get() == proxyInstance);

	}
}
