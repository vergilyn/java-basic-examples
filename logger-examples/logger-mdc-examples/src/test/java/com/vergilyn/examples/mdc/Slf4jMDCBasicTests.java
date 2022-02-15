package com.vergilyn.examples.mdc;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author vergilyn
 * @since 2022-02-15
 *
 * @see MDC
 */
@Slf4j
public class Slf4jMDCBasicTests {

	@Test
	public void basic(){
		MDC.put("username", "vergilyn");
		log.debug("debug");
	}

	/**
	 * <pre>
	 *   When exiting a thread make sure to call NDC.remove() / MDC.clear() !
	 *   > [logback日志与MDC机制](https://blog.csdn.net/iteye_19607/article/details/82677252)
	 *   > 在使用MDC时需要注意一些问题，这些问题通常也是ThreadLocal引起的，比如我们需要在线程退出之前清除（clear）MDC中的数据；
	 *   > 在线程池中使用MDC时，那么需要在子线程退出之前清除数据；可以调用MDC.clear()方法。
	 *
	 *   个人：相对来说“退出方法” 比 “进入方法” 更难判断
	 * </pre>
	 *
	 * 本项目中，{@linkplain org.slf4j.MDC}对应的是 {@linkplain ch.qos.logback.classic.util.LogbackMDCAdapter}，
	 * 并没有实现 "子线程继承父线程的MDC"机制。 （{@linkplain org.apache.log4j.MDC} 实现了该机制）
	 */
	@Test
	@SneakyThrows
	public void multiThread(){
		ExecutorService pool = Executors.newFixedThreadPool(3);

		MDC.put("main", ThreadLocalRandom.current().nextInt() + "");
		Map<String, String> parentMDC = MDC.getCopyOfContextMap();

		AtomicInteger index = new AtomicInteger();
		for (int i = 0; i < 4; i++) {
			pool.submit(() -> {
				int idx = index.incrementAndGet();
				// 继承父线程的MDC
				MDC.setContextMap(parentMDC);
				MDC.put("child-thread", "thread-idx-" + idx + "");

				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
				}
				log.info("{}", idx);
			});
		}

		TimeUnit.SECONDS.sleep(10);
	}
}
