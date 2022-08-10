package com.vergilyn.examples.nacos.event.feature;

import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.SlowEvent;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.vergilyn.examples.nacos.event.AbstractNacosEventTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class SlowEventTests extends AbstractNacosEventTests {

	@SneakyThrows
	@Test
	public void slowEvent(){
		NotifyCenter.registerSubscriber(new Subscriber<CustomSlowEvent>() {
			@Override
			public void onEvent(CustomSlowEvent event) {
				sleepSafe(2000);
				printf("%s.time = %s \n", event.getClass().getSimpleName(), ((CustomSlowEvent) event).time);
			}

			@Override
			public Class<? extends Event> subscribeType() {
				return CustomSlowEvent.class;
			}

			/**
			 * 如果返回值不为空，则会用返回的线程池执行。否则 current-thread 执行。
			 *
			 * @see com.alibaba.nacos.common.notify.DefaultPublisher#notifySubscriber(Subscriber, Event)
			 */
			@Override
			public Executor executor() {
				return super.executor();
			}
		});

		NotifyCenter.publishEvent(new CustomSlowEvent(LocalTime.now()));

		TimeUnit.HOURS.sleep(5);
	}

	private static class CustomSlowEvent extends SlowEvent {
		private final LocalTime time;

		public CustomSlowEvent(LocalTime time) {
			this.time = time;
		}

		public LocalTime getTime() {
			return time;
		}
	}
}
