package com.vergilyn.examples.thread.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a href="https://github.com/alibaba/transmittable-thread-local">alibaba/transmittable-thread-local</a> 介绍：<br/>
 * 主要是解决 因为 线程池复用线程，导致 ThreadLocal 也被复用的问题!
 *
 * @author vergilyn
 * @since 2022-01-24
 *
 */
public class TransmittableThreadLocalTests {
	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	@SneakyThrows
	@Test
	public void threadLocal(){
		ThreadLocalRunnable.THREAD_LOCAL = ThreadLocal.withInitial(StringBuffer::new);

		AtomicInteger index = new AtomicInteger();

		for (int i = 0; i < 4; i++) {
			executorService.submit(new ThreadLocalRunnable(index.getAndIncrement()));
		}

		TimeUnit.SECONDS.sleep(5);
	}

	/**
	 * 使用demo参考：<a href="https://github.com/alibaba/transmittable-thread-local">alibaba/transmittable-thread-local, `README.md`</a>
	 */
	@SneakyThrows
	@Test
	public void transmittableThreadLocal(){
		ThreadLocalRunnable.THREAD_LOCAL = TransmittableThreadLocal.withInitial(StringBuffer::new);

		// Executor ttlExecutor = TtlExecutors.getTtlExecutor(executorService);

		AtomicInteger index = new AtomicInteger();
		for (int i = 0; i < 4; i++) {
			executorService.execute(TtlRunnable.get(new ThreadLocalRunnable(index.getAndIncrement())));
		}

		TimeUnit.SECONDS.sleep(5);
	}

	public static class ThreadLocalRunnable implements Runnable {
		public static ThreadLocal<StringBuffer> THREAD_LOCAL;
		private final Integer idx;

		public ThreadLocalRunnable(Integer idx) {
			this.idx = idx;
		}

		@Override
		public void run() {
			THREAD_LOCAL.get().append(idx);

			System.out.printf("[thread-%s][idx-%s] >>>> thread-local: %s \n",
			                   Thread.currentThread().getName(), idx, THREAD_LOCAL.get().toString());
		}
	}

}
