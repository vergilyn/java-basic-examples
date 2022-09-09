package com.vergilyn.examples.thread.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlRunnable;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <a href="https://github.com/alibaba/transmittable-thread-local">alibaba/transmittable-thread-local</a> 介绍：<br/>
 * 主要是解决 因为 线程池复用线程，导致 ThreadLocal 也被复用的问题!
 *
 * @author vergilyn
 * @since 2022-01-24
 *
 */
public class TransmittableThreadLocalTests {
	private ExecutorService executorService;
	public static ThreadLocal<StringBuffer> CONTEXT;

	@BeforeEach
	public void beforeEach(){
		executorService = Executors.newFixedThreadPool(3);
	}

	/**
	 * 因为会复用线程，所以其实会有 threads个 thread-local's。
	 * thread-local 在相同线程中被复用！
	 */
	@SneakyThrows
	@Test
	public void threadLocal(){
		CONTEXT = ThreadLocal.withInitial(StringBuffer::new);
		CONTEXT.get().append("[MAIN]");

		for (int i = 0; i < 8; i++) {
			executorService.submit(new ThreadLocalRunnable(i));
		}

		TimeUnit.SECONDS.sleep(2);
	}

	/**
	 * 使用demo参考：<a href="https://github.com/alibaba/transmittable-thread-local">alibaba/transmittable-thread-local, `README.md`</a>
	 */
	@SneakyThrows
	@Test
	public void parentToChild(){
		CONTEXT = TransmittableThreadLocal.withInitial(StringBuffer::new);
		CONTEXT.get().append("[MAIN]");

		for (int i = 0; i < 8; i++) {
			executorService.execute(TtlRunnable.get(new ThreadLocalRunnable(i)));
		}

		TimeUnit.SECONDS.sleep(2);
	}


	public static class ThreadLocalRunnable implements Runnable {
		private final Integer idx;

		public ThreadLocalRunnable(Integer idx) {
			this.idx = idx;
		}

		@Override
		public void run() {
			CONTEXT.get().append(idx);

			// CONTEXT.get().append("a");

			System.out.printf("[thread-%s][idx-%s] >>>> thread-local: %s \n",
			                   Thread.currentThread().getName(), idx, CONTEXT.get().toString());
		}
	}

}
