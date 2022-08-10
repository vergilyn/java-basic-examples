package com.vergilyn.examples.nacos.event.feature;

import cn.hutool.core.thread.NamedThreadFactory;
import com.alibaba.nacos.common.notify.DefaultPublisher;
import com.alibaba.nacos.common.notify.DefaultSharePublisher;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.vergilyn.examples.nacos.event.AbstractNacosEventTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EventExecuteThreadTests extends AbstractNacosEventTests {

	/**
	 * nacos-event 支持由 {@link Subscriber#executor()} 来提供事件处理的线程池。
	 * <br/> 如果其返回 null，也不是有 push-event-thread去执行，而是有对应事件创建的{@link DefaultPublisher DefaultPublisher-Thread}
	 * 去执行事件处理。
	 *
	 * <p/><h3>默认执行线程</h3>
	 * 无论是 share（{@link DefaultSharePublisher}），亦或是 event-publisher（{@link DefaultPublisher}）。
	 * <b>它们都实现了 {@link Thread}</b>
	 *
	 * <p> 详见：{@link DefaultPublisher#init(Class, int)}
	 *   <br/> 其中会调用 {@link Thread#start()}，从而执行 {@link DefaultPublisher#run()}。
	 *   在`run()`中会通过{@link BlockingQueue#take()} 触发event的处理。
	 *
	 */
	@SneakyThrows
	@Test
	public void publisherEvent(){
		Executor subscribeProviderExecutor = Executors.newFixedThreadPool(2,
		                                                                  new NamedThreadFactory("vergilyn-subscirber-thread-", true));

		// 观察执行线程名称
		NotifyCenter.registerSubscriber(new Subscriber<CustomPublisherEvent>() {
			@Override
			public void onEvent(CustomPublisherEvent event) {
				printf("%s.time = %s \n", event.getClass().getSimpleName(), event.getTime());
			}

			@Override
			public Class<? extends Event> subscribeType() {
				return CustomPublisherEvent.class;
			}

			@Override
			public Executor executor() {
				return subscribeProviderExecutor;
			}
		});

		NotifyCenter.publishEvent(new CustomPublisherEvent(LocalTime.now()));

		TimeUnit.SECONDS.sleep(10);
	}

	private static class CustomPublisherEvent extends Event {
		private final LocalTime time;

		public CustomPublisherEvent(LocalTime time) {
			this.time = time;
		}

		public LocalTime getTime() {
			return time;
		}
	}
}
