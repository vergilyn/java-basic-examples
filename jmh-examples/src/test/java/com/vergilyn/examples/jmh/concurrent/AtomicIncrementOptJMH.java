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
 * <pre> BATCH_SIZE = 100_000
 * Benchmark                           Mode  Cnt  Score   Error  Units
 * AtomicIncrementOptJMH.atomicLong      ss    5  5.739 ± 6.512  ms/op
 * AtomicIncrementOptJMH.longAdder       ss    5  0.868 ± 0.110  ms/op
 * AtomicIncrementOptJMH.longAdderGet    ss    5  1.021 ± 0.406  ms/op
 * </pre>
 *
 * <pre> BATCH_SIZE = 1_000_000
 * Benchmark                           Mode  Cnt    Score   Error  Units
 * AtomicIncrementOptJMH.atomicLong      ss    5  104.546 ± 8.192  ms/op
 * AtomicIncrementOptJMH.longAdder       ss    5    8.753 ± 2.321  ms/op
 * AtomicIncrementOptJMH.longAdderGet    ss    5   10.321 ± 2.219  ms/op
 * </pre>
 *
 * 结论：如果是简单的 incr/get，确实更推荐 LongAdder。但如果需要`compareAndSet`之类的，还是用 AtomicLong。
 *
 * @author vergilyn
 * @since 2022-02-17
 *
 * @see org.openjdk.jmh.samples.JMHSample_17_SyncIterations
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, batchSize = AtomicIncrementOptJMH.BATCH_SIZE)
@Measurement(iterations = 5, batchSize = AtomicIncrementOptJMH.BATCH_SIZE)
@BenchmarkMode(Mode.SingleShotTime)
@Threads(Threads.MAX)
public class AtomicIncrementOptJMH {
	public static final int BATCH_SIZE = 1_000_000;

	// 声明成`static final`，保证所有 benchmark 并发调用`incr()`
	private static final AtomicLong ATOMIC_LONG = new AtomicLong();
	private static final LongAdder LONG_ADDER = new LongAdder();
	private static final LongAdder LONG_ADDER_GET = new LongAdder();

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(AtomicIncrementOptJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.syncIterations(true)
				.build();

		new Runner(opt).run();
	}

	@Benchmark
	public void atomicLong() {
		ATOMIC_LONG.incrementAndGet();
	}

	@Benchmark
	public void longAdder() {
		LONG_ADDER.increment();
	}

	@Benchmark
	public void longAdderGet() {
		LONG_ADDER_GET.increment();
		LONG_ADDER_GET.longValue();
	}

}
