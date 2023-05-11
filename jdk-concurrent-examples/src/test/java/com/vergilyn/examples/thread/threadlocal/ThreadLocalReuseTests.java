package com.vergilyn.examples.thread.threadlocal;

import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.threadpool.TtlExecutors;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池复用 线程，{@linkplain InheritableThreadLocal} 与 {@linkplain com.alibaba.ttl.TransmittableThreadLocal} 区别
 * @author vergilyn
 * @since 2023-02-15
 */
public class ThreadLocalReuseTests extends AbstractThreadLocalTests {

	private final ExecutorService _singleExecutor = new ThreadPoolExecutor(1, 1,
	                                                                       60L, TimeUnit.SECONDS,
	                                                                       new LinkedBlockingQueue<Runnable>());

	@SneakyThrows
	private void execute(ThreadLocalContextWrapper wrapper, boolean isTTL){


		ExecutorService executor = !isTTL ? _singleExecutor : TtlExecutors.getTtlExecutorService(_singleExecutor);

		Runnable run1 = () -> {
			wrapper.get().append("[sub-1]");

			println(wrapper.get().toString());
		};
		executor.submit(!isTTL ? run1 : TtlRunnable.get(run1)).get();

		// 复用线程池中的线程
		Runnable run2 = () -> {
			wrapper.get().append("[sub-2]");

			println(wrapper.get().toString());
		};
		executor.submit(!isTTL ? run2 : TtlRunnable.get(run2)).get();

		TimeUnit.SECONDS.sleep(1);

		println(wrapper.get().toString());
	}


	/**
	 * <pre>
	 *    [thread-pool-1-thread-1] >>>> [sub-1]
	 *    [thread-pool-1-thread-1] >>>> [sub-1][sub-2]
	 *    [thread-main] >>>> [ThreadLocal][main]
	 * </pre>
	 *
	 * 1) 父子线程无法传递`ThreadLocal`的值；<br/>
	 * 2) 线程复用会保留`ThreadLocal`之前的值；
	 */
	@Test
	public void threadLocal(){
		ThreadLocalContext.set(new StringBuffer());

		ThreadLocalContext.get().append("[ThreadLocal][main]");

		execute(new ThreadLocalContextWrapper() {
			@Override
			public StringBuffer get() {
				return ThreadLocalContext.get();
			}

			@Override
			public void set(StringBuffer value) {
				ThreadLocalContext.set(value);
			}

			@Override
			public void clear() {
				ThreadLocalContext.clear();
			}
		}, false);
	}


	/**
	 * <pre>
	 *   [thread-pool-1-thread-1] >>>> [InheritableThreadLocal][main][sub-1]
	 *   [thread-pool-1-thread-1] >>>> [InheritableThreadLocal][main][sub-1][sub-2]
	 *   [thread-main] >>>> [InheritableThreadLocal][main][sub-1][sub-2]
	 * </pre>
	 *
	 * 1) 父子线程之间可以相互传递值；<br/>
	 * 2) 线程复用会保留`InheritableThreadLocal`之前的值；
	 */
	@Test
	public void inheritableThreadLocal(){
		InheritableThreadLocalContext.set(new StringBuffer());

		InheritableThreadLocalContext.get().append("[InheritableThreadLocal][main]");

		execute(new ThreadLocalContextWrapper() {
			@Override
			public StringBuffer get() {
				return InheritableThreadLocalContext.get();
			}

			@Override
			public void set(StringBuffer value) {
				InheritableThreadLocalContext.set(value);
			}

			@Override
			public void clear() {
				InheritableThreadLocalContext.clear();
			}
		}, false);
	}

	@SneakyThrows
	@Test
	public void transmittableThreadLocal(){
		TransmittableThreadLocalContext.set(new StringBuffer());

		TransmittableThreadLocalContext.get().append("[TransmittableThreadLocal][main]");


		ExecutorService executor = TtlExecutors.getTtlExecutorService(_singleExecutor);

		Runnable run1 = () -> {
			TransmittableThreadLocalContext.get().append("[sub-1]");

			println(TransmittableThreadLocalContext.get().toString());
		};
		executor.submit(TtlRunnable.get(run1)).get();

		// 复用线程池中的线程
		Runnable run2 = () -> {
			TransmittableThreadLocalContext.get().append("[sub-2]");

			println(TransmittableThreadLocalContext.get().toString());
		};
		executor.submit(TtlRunnable.get(run2)).get();

		TimeUnit.SECONDS.sleep(1);

		println(TransmittableThreadLocalContext.get().toString());
	}


}
