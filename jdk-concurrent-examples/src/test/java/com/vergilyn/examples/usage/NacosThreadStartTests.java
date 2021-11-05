package com.vergilyn.examples.usage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * SEE: Nacos-2.0, `com.alibaba.nacos.common.notify.DefaultPublisher`
 * @author vergilyn
 * @since 2021-04-14
 */
public class NacosThreadStartTests {
	private static final String VERGILYN = "vergilyn";

	/**
	 * nacos sub-pub, {@linkplain DefaultPublisher#run()} 不会是因为nacos功能代码throws-ex。
	 * 所以，publisher 会一直运行。（除非jvm 或者 shutdown，以下测试代码没有实现shutdown逻辑）
	 */
	@SneakyThrows
	@Test
	public void test(){
		DefaultPublisher publisher = new DefaultPublisher();
		publisher.init();  // 内部调用 Thread.start()

		TimeUnit.SECONDS.sleep(2);

		for (int i = 1; i <= 6; i++) {
			publisher.queue.offer(i);
			TimeUnit.SECONDS.sleep(1);
		}

		// prevent jvm exit
		new Semaphore(0).acquire();
	}


	/**
	 * SEE: Nacos-2.0, `com.alibaba.nacos.common.notify.DefaultPublisher`
	 */
	class DefaultPublisher extends Thread {
		private volatile boolean initialized = false;
		private BlockingQueue<Integer> queue;

		public void init() {
			super.setDaemon(true);
			super.setName("nacos.publisher-" + VERGILYN);
			this.queue = new ArrayBlockingQueue<Integer>(10);
			start();
		}

		@Override
		public synchronized void start() {
			if (!initialized) {
				System.out.printf("[vergilyn] >>>> BEFORE #start() \n");
				super.start();
				System.out.printf("[vergilyn] >>>> AFTER #start() \n");

				initialized = true;
			}
		}

		@Override
		public void run() {
			openEventHandler();
		}

		void openEventHandler() {
			try {

				for (; ; ) {
					final Integer event = queue.take();
					receiveEvent(event);
				}
			} catch (Throwable ex) {
				System.out.printf("[vergilyn] >>>> Event listener exception : %s \n", ex.getMessage());
			}
		}

		void receiveEvent(Integer event) {
			System.out.printf("[vergilyn] >>>> event: %d \n", event);
			if (event > 5){
				// 会导致线程停止
				throw new RuntimeException("manual-throws exit thread-run");
			}
		}
	}
}
