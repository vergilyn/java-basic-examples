package com.vergilyn.examples.guava.util.concurrent;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.TimeLimiter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.*;

public class TimeLimiterTests {

	/**
	 * <b> Imposes a time limit on method calls.（对方法调用施加时间限制。）</b>
	 * <p> 1. 只能代理 接口。
	 * <p> 2. 内部的调用 都是用 `ExecutorService`，并同步等待结果 `Future`。
	 * <p> 3. {@linkplain SimpleTimeLimiter#callWithTimeout} 和 {@linkplain SimpleTimeLimiter#runWithTimeout}
	 *   区别貌似只在 是否有返回值。
	 *
	 * <p> <h3>备注</h3>
	 * <p> 1. 源码参考 {@linkplain SimpleTimeLimiter#newProxy(Object, Class, long, TimeUnit)}
	 *   <br/> 其实质就是
	 *   `java.lang.reflect.Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class<?>[] {interfaceType}, handler)`。
	 *   <br/> 其默认调用的 {@linkplain SimpleTimeLimiter#callWithTimeout(Callable, long, TimeUnit)}
	 *
	 *
	 */
	@Test
	public void test(){
		ExecutorService executor = Executors.newFixedThreadPool(2);

		TimeLimiter timeLimiter = SimpleTimeLimiter.create(executor);

		TestService target = new TestServiceImpl();


		int timeoutDurationMs = 100;
		TestService proxy = timeLimiter.newProxy(target, TestService.class,
		                                         timeoutDurationMs, TimeUnit.MILLISECONDS);

		// 未超过指定的时间限制，正常返回。
		Assertions.assertThat(proxy.someMethod(timeoutDurationMs / 2)).isNotNull();

		// 超过指定的时间限制，会抛出异常。
		Throwable throwable = Assertions.catchThrowable(() -> proxy.someMethod(timeoutDurationMs * 2));
		Assertions.assertThat(throwable)
				.isInstanceOfAny(
						com.google.common.util.concurrent.UncheckedTimeoutException.class,
						java.util.concurrent.TimeoutException.class
				);



	}


	@Test
	public void call() throws ExecutionException, InterruptedException, TimeoutException {
		TimeLimiter timeLimiter = SimpleTimeLimiter.create(Executors.newSingleThreadExecutor());

		TestService target = new TestServiceImpl();

		LocalTime localTime = timeLimiter.callWithTimeout(new Callable<LocalTime>() {
			@Override
			public LocalTime call() throws Exception {
				return target.someMethod(10 * 1000);
			}
		}, 1, TimeUnit.MINUTES);

		System.out.println(localTime);
	}

	public static interface TestService {

		LocalTime someMethod(int timeoutMs);
	}

	public static class TestServiceImpl implements TestService{

		@Override
		public LocalTime someMethod(int timeoutMs){
			try {
				TimeUnit.MILLISECONDS.sleep(timeoutMs);
				System.out.println("[vergilyn] `#someMethod(...)` completed.");
				return LocalTime.now();
			}catch (Exception ignore){
				return null;
			}
		}
	}


}
