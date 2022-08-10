package com.vergilyn.examples.monitor.feature.registry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

public class GlobalRegistryTests {

	@Test
	public void globalRegistry(){
		SimpleMeterRegistry simple = new SimpleMeterRegistry();

		// 等价于 `Metrics.globalRegistry.add(simple);`
		Metrics.addRegistry(simple);

		// tags -> key=value
		Counter counter = Metrics.counter("counter", "tag-a", "a01", "tag-b", "b01");
		counter.increment();

		System.out.println(counter.measure());
		System.out.println(simple.getMetersAsString());
	}
}
