package com.vergilyn.examples.jmh.concurrent;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * FIXME 2022-02-18
 * 并未理解那种写法才符合JMH，此写法的结果与 预期相反！
 *
 * @author vergilyn
 * @since 2022-02-17
 *
 * @see <a href="https://github.com/openjdk/jmh/blob/master/jmh-samples/src/main/java/org/openjdk/jmh/samples/JMHSample_26_BatchSize.java">JMHSample_26_BatchSize.java </a>
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, batchSize = AtomicIncrementOptJMH.BATCH_SIZE)
@Measurement(iterations = 5, batchSize = AtomicIncrementOptJMH.BATCH_SIZE)
@BenchmarkMode(Mode.SingleShotTime)
@Threads(Threads.MAX)
public class AtomicIncrementOptJMH {
	public static final int BATCH_SIZE = 1_000_000;

	private final AtomicLong atomicLong = new AtomicLong();
	private final LongAdder longAdder = new LongAdder();

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(AtomicIncrementOptJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.syncIterations(false)
				.build();

		new Runner(opt).run();
	}

	@Benchmark
	public void atomicLong() {
		atomicLong.incrementAndGet();
		print("atomicLong");
	}

	@Benchmark
	public void longAdder() {
		longAdder.increment();
		print("longAdder");
	}

	// @TearDown(Level.Iteration)
	public void tearDown(){
		atomicLong.set(0L);
		longAdder.reset();
	}

	private void print(String str){
		// Thread thread = Thread.currentThread();
		// System.out.printf("[name-%s][id-%s] %s\n", thread.getName(), thread.getId(), str);
	}
}
