package com.vergilyn.examples.thread.threadlocal;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 目的：父线程 给 子线程 传递值。。
 *
 * <br/> 写法不友好，更多推荐：{@linkplain TransmittableThreadLocalTests}
 *
 * @author vergilyn
 * @since 2022-01-24
 */
public class InheritableThreadLocalTests extends AbstractThreadLocalTests {
	ExecutorService threadPool = Executors.newFixedThreadPool(1);

	private static ThreadLocal<StringBuffer> mainThreadLocal = new InheritableThreadLocal<StringBuffer>(){{
		set(new StringBuffer());
	}};

	@SneakyThrows
	@Test
	public void test(){
		ThreadContext threadContext = new ThreadContext();

		mainThreadLocal.get().append("[main]");

		threadPool.submit(new TestRunnable(threadContext));

		threadPool.submit(new TestRunnable(mainThreadLocal));

		TimeUnit.SECONDS.sleep(5);
	}

	public static class TestRunnable implements Runnable{
		private final ThreadContext threadContext;
		private final ThreadLocal<StringBuffer> threadLocal;

		/**
		 * 利用 `XxxContext` 解决代替 `InheritableThreadLocalTests.mainThreadLocal`
		 */
		public TestRunnable(ThreadContext threadContext) {
			this.threadContext = threadContext;
			this.threadLocal = threadContext.threadLocal;
		}

		/**
		 * 直接在 parent-thread 取出 ThreadLocal，然后通过构造函数传递给 子线程。
		 */
		public TestRunnable(ThreadLocal<StringBuffer> parentThreadLocal){
			this.threadContext = null;
			this.threadLocal = parentThreadLocal;
		}

		@Override
		public void run() {
			// XXX 2022-01-24 感觉不科学.... 万一有多个 main-thread 调用此runnable，怎么知道是`InheritableThreadLocalTests.mainThreadLocal`？
			mainThreadLocal.get().append("[sub-thread]");

			// 利用 `XxxContext`解决上述问题
			// 或者：直接在 main-thread 取出 ThreadLocal，通过 此Runnable的构造函数 传递。
			// threadLocal.get().append("[sub-thread]");

			System.out.println(mainThreadLocal.get());
		}
	}

	public static class ThreadContext {
		private final ThreadLocal<StringBuffer> threadLocal;

		public ThreadContext() {
			this.threadLocal = new InheritableThreadLocal<StringBuffer>(){{
				set(new StringBuffer());
			}};
		}
	}
}
