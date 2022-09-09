package com.vergilyn.examples.jdk;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
		@Override
		public void say(){
			System.out.println("Hello >>>> say()");
		}
	}

	/**
	 * see: {@code org.mybatis.spring.SqlSessionTemplate.SqlSessionInterceptor}
	 *
	 * @see com.google.common.util.concurrent.SimpleTimeLimiter
	 */
	@Test
	public void proxyMethod(){
		Hello hello = new HelloImpl();

		// 只能是 interfaces
		Hello proxyInstance = (Hello)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { Hello.class },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						System.out.println("HelloInterceptor >>>> proxy: " + proxy.getClass().getName());

						System.out.println("HelloInterceptor >>>> say() before");

						// `hello`真实对象
						Object result = method.invoke(hello, args);

						System.out.println("HelloInterceptor >>>> say() after");

						return result;
					}
				});

		proxyInstance.say();
	}
}
