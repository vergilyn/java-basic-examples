package com.vergilyn.examples.monitor.feature.registry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;

public class CompositeRegistriesTests {

	@Test
	public void composite(){
		CompositeMeterRegistry composite = new CompositeMeterRegistry();

		Counter compositeCounter = composite.counter("counter");

		// 无效操作，也不会报错。
		// 参考: `CompositeCounter#increment() -> children's == empty.`
		compositeCounter.increment();

		// counter计数器注册到simple registry
		SimpleMeterRegistry simple = new SimpleMeterRegistry();
		composite.add(simple);

		// incr 成功，此时 `count = 1`。
		compositeCounter.increment();

		System.out.println(compositeCounter.count());
		System.out.println(simple.getMetersAsString());
	}

}
