package com.vergilyn.examples.monitor.monitor;

import com.vergilyn.examples.monitor.pojo.OrderChannelEnum;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

public class MetricsMonitor {

	public static Counter getOrderCount(OrderChannelEnum orderChannel) {
		return Metrics.counter("vergilyn.order.count", "order.channel", orderChannel.name());
	}

	public static <E> BlockingQueue<E> getMessageGauge(BlockingQueue<E> messageQueue){
		return Metrics.gauge("vergilyn.message.gauge",
		                     Tags.of("vergilyn.message.gauge", "vergilyn.message.queue.size"), messageQueue,
		                     Collection::size);
	}

	public static Timer getMethodCost(Method method){
		return Metrics.timer("vergilyn.method.cost.time", "method.name", method.getName());
	}
}
