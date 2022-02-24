package com.vergilyn.examples.jmh.feature;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author vergilyn
 * @since 2022-02-21
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
// 2次预热，每次调用`batchSize * threads`次的benchmark（由于是`mode = SingleShotTime`）
// 或者：threads 个线程，各自执行 warmup&measurement。 总共执行 `threads * (warmup.it * batchSize + measure.it * batchSize)`
@Warmup(iterations = 2, batchSize = 2)
@Measurement(iterations = 3, batchSize = 3)
@BenchmarkMode(Mode.SingleShotTime)
@Threads(4)
public class ConcurrentThreadJMH {

	private static final AtomicInteger index = new AtomicInteger();
	private final AtomicLong atomicLong = new AtomicLong();
	private long longValue = 0L;

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(ConcurrentThreadJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.syncIterations(true)
				.build();

		new Runner(opt).run();
	}

	@Benchmark
	public void benchmark() {
		longValue ++;
		atomicLong.getAndIncrement();
	}

	/*
	 * 每个线程执行`batchSize`，即每次iteration执行 `threads * batchSize` 次！
	 * 所以输出结果例如：
	 * # Warmup Iteration   1:
	 * [index-3][instance-933517735][thread-14]tearDown >>>> long: 1, atomic: 1
	 * [index-2][instance-163257132][thread-13]tearDown >>>> long: 1, atomic: 1
	 * [index-6][instance-163257132][thread-13]tearDown >>>> long: 2, atomic: 2
	 * [index-1][instance-925427880][thread-15]tearDown >>>> long: 1, atomic: 1
	 * [index-4][instance-1666784009][thread-16]tearDown >>>> long: 1, atomic: 1
	 * [index-7][instance-925427880][thread-15]tearDown >>>> long: 2, atomic: 2
	 * [index-5][instance-933517735][thread-14]tearDown >>>> long: 2, atomic: 2
	 * [index-8][instance-1666784009][thread-16]tearDown >>>> long: 2, atomic: 2
	 *
	 * 1) 因为是`@State(Scope.Thread)`，每个线程都有独立的`instance`。（例如"thread-13"对应 "instance-163257132" ）
	 * 2) 因为是 `SingleShotTime + @Threads(4) + @Warmup(it, batchSize)`，所以每次 iteration 执行 `Threads * batchSize` 次！
	 */
	@TearDown(Level.Invocation)
	public void tearDown(){
		System.out.printf("[index-%d][instance-%s][thread-%s]tearDown >>>> long: %d, atomic: %d \n",
		                  index.incrementAndGet(), this.hashCode(), Thread.currentThread().getId(),
		                  longValue, atomicLong.get());
	}

	@TearDown(Level.Iteration)
	public void reset(){
		index.set(0);
	}
}
