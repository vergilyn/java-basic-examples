package com.vergilyn.examples.thread.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ThreadLocal 透传问题：
 *   1. 父线程 -> 子线程
 *   2. 子线程 -> 父线程
 *
 * @author vergilyn
 * @since 2022-09-09
 */
public class ArgumentPassThroughTests extends AbstractThreadLocalTests {
	private ExecutorService executor;

	@BeforeEach
	public void beforeEach(){
		executor = Executors.newFixedThreadPool(3);
	}

	/**
	 * 父子之间 互相不共享
	 */
	@SneakyThrows
	@Test
	public void threadLocal(){

		ThreadLocal<StringBuffer> threadLocal = ThreadLocal.withInitial(StringBuffer::new);
		StringBuffer main = threadLocal.get();

		main.append("[main]");

		executor.submit(() -> {
			threadLocal.get().append("[sub]");

			println(threadLocal.get().toString());

			// false
			println("`main == sub`: " + (main == threadLocal.get()));
		}).get();

		println(threadLocal.get().toString());
	}

	/**
	 * 允许：父 -> 子 -> 父
	 */
	@SneakyThrows
	@Test
	public void inheritableThreadLocal(){
		ThreadLocal<StringBuffer> inheritableThreadLocal = new InheritableThreadLocal<>();
		inheritableThreadLocal.set(new StringBuffer());

		StringBuffer main = inheritableThreadLocal.get();

		main.append("[main]");

		executor.submit(() -> {
			// [vergilyn][thread-pool-1-thread-1] >>>> [main][sub]
			inheritableThreadLocal.get().append("[sub]");

			println(inheritableThreadLocal.get().toString());

			// [vergilyn][thread-pool-1-thread-1] >>>> `main == sub`: true
			println("`main == sub`: " + (main == inheritableThreadLocal.get()));
		}).get();

		// [vergilyn][thread-main] >>>> [main][sub]
		println(inheritableThreadLocal.get().toString());
	}

	/**
	 * 允许：父 -> 子 -> 父
	 */
	@SneakyThrows
	@Test
	public void transmittableThreadLocal(){
		ThreadLocal<StringBuffer> threadLocal = TransmittableThreadLocal.withInitial(StringBuffer::new);
		threadLocal.get().append("[main]");

		executor.submit(() -> {
			threadLocal.get().append("[sub]");

			// [vergilyn][thread-pool-1-thread-1] >>>> [main][sub]
			println(threadLocal.get().toString());
		}).get();

		// [vergilyn][thread-main] >>>> [main][sub]
		println(threadLocal.get().toString());
	}

}
