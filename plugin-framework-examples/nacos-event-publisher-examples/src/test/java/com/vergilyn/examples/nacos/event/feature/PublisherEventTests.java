package com.vergilyn.examples.nacos.event.feature;

import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.vergilyn.examples.nacos.event.AbstractNacosEventTests;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class PublisherEventTests extends AbstractNacosEventTests {

	/**
	 * 1. 默认全局 `queue-max-size = {@link NotifyCenter#ringBufferSize}`，可以单独指定。
	 *
	 * @see NotifyCenter#registerToPublisher(Class, int)
	 */
	@SneakyThrows
	@Test
	public void publisherEvent(){
		// 非必须，只是可以通过此方式指定 `queue-max-size`
		// NotifyCenter.registerToPublisher(CustomSlowEvent.class, 2);

		NotifyCenter.registerSubscriber(new Subscriber<CustomPublisherEvent>() {
			@Override
			public void onEvent(CustomPublisherEvent event) {
				printf("%s.time = %s \n", event.getClass().getSimpleName(), event.getTime());
			}

			@Override
			public Class<? extends Event> subscribeType() {
				return CustomPublisherEvent.class;
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
