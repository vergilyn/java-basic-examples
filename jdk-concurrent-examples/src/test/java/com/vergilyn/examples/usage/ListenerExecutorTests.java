package com.vergilyn.examples.usage;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * SEE: Nacos-2.0.0 `com.alibaba.nacos.common.remote.client.RpcClient#start()`
 *
 * <p>
 * 1) `queue.take()`底层实现：{@linkplain Condition#await()} -> {@linkplain AbstractQueuedSynchronizer.ConditionObject#await()}
 *
 * @author vergilyn
 * @since 2021-04-09
 */
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ListenerExecutorTests {

	private final LinkedBlockingQueue<VergilynEvent> queue = new LinkedBlockingQueue<>(10);
	private ExecutorService executor;

	@BeforeAll
	public void beforeAll(){
		executor = new ScheduledThreadPoolExecutor(1, r -> {
			Thread t = new Thread(r);
			t.setName("vergilyn-listener-executor");
			t.setDaemon(true);
			return t;
		});

		executor.submit((Runnable) () -> {
			while (true){
				try {
					/* take():
					 *   Retrieves and removes the head of this queue,
					 *   waiting if necessary until an element becomes available.
					 */
					VergilynEvent take = queue.take();
					log.info("[vergilyn] >>>> size: {}, {}", queue.size(), take);
				}catch (Exception e){
					// do-nothing
				}
			}
		});
	}

	@SneakyThrows
	@Test
	public void triggerEvent(){
		queue.add(new VergilynEvent(LocalTime.now().toString()));
		queue.offer(new VergilynEvent(LocalTime.now().toString()));

		TimeUnit.SECONDS.sleep(2);

		queue.offer(new VergilynEvent(LocalTime.now().toString()));

		TimeUnit.SECONDS.sleep(2);

		queue.add(new VergilynEvent(LocalTime.now().toString()));
	}

	class VergilynEvent {
		private final String str;

		public VergilynEvent(String str) {
			this.str = str;
		}
		@Override
		public String toString() {
			return this.str;
		}
	}
}
