package com.vergilyn.examples.monitor.feature.registry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

/**
 * 常用的几种 bind-registry 方式。
 *
 * @author vergilyn
 * @since 2022-08-10
 */
public class BindTests {

	@Test
	public void bind(){
		SimpleMeterRegistry simple = new SimpleMeterRegistry();
		Metrics.addRegistry(simple);

		Counter counter = Metrics.counter("counter", "tag-a", "a01", "tag-b", "b01");
		counter.increment();

		System.out.println(counter.measure());
		System.out.println(simple.getMetersAsString());
	}

	@Test
	public void bindRegistry(){
		SimpleMeterRegistry simple = new SimpleMeterRegistry();

		Counter counter = Counter.builder("counter")
				.baseUnit("%base%unit%")
				.description("desc")
				.tag("tag-a", "a01")
				.tag("tag-b", "b01")
				.register(simple);

		Metrics.addRegistry(simple);

		counter.increment();

		System.out.println(counter.measure());
		System.out.println(simple.getMetersAsString());
	}
}
