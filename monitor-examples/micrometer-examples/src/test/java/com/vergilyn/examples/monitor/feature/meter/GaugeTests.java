package com.vergilyn.examples.monitor.feature.meter;

import io.micrometer.core.instrument.ImmutableTag;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GaugeTests {

	/**
	 *
	 * Gauge（仪表）适合用于监测有自然上界的事件或者任务，<br/>
	 * 而Counter一般使用于无自然上界的事件或者任务的监测，所以像HTTP请求总量计数应该使用Counter而非Gauge。
	 *
	 * <p> gauge 使用场景
	 * <br/> 1. 有自然(物理)上界的浮动值的监测，例如物理内存、集合、映射、数值等。
	 * <br/> 2. 有逻辑上界的浮动值的监测，例如积压的消息、（线程池中）积压的任务等，其实本质也是集合或者映射的监测。
	 * <br/> 3. 例如油量，档位，转速，速度。这些值会上下浮动。
	 *   <b>并且不会累积计算</b>。（昨天最高速度是100公里/小时，今天最高速度是80公里每小时，这两个数值加起来没意义）
	 *
	 * @see com.alibaba.nacos.config.server.monitor.MetricsMonitor
	 */
	@Test
	public void nacos(){
		AtomicInteger getConfig = new AtomicInteger();

		List<Tag> tags = new ArrayList<Tag>();
		tags.add(new ImmutableTag("module", "config"));
		tags.add(new ImmutableTag("name", "getConfig"));

		Metrics.gauge("nacos_monitor", tags, getConfig);

		getConfig.incrementAndGet();

		System.out.println(Metrics.globalRegistry);
	}
}
