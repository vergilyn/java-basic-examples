package com.vergilyn.examples.jmh.concurrent;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 *
 * <a href="https://www.jianshu.com/p/89dfe990295c">多线程下ThreadLocalRandom用法</a>：
 * 多线程下，性能比 {@linkplain java.util.Random} 或 {@linkplain RandomUtils} 要好一点。
 *
 * 原因：`Random`内部回CASA竞争获取新的seed，而ThreadLocalRandom不会有竞争。
 * 并且每次调用`ThreadLocalRandom#current()`<b>不会产生多个`ThreadLocalRandom`实例</b>。
 *
 * @author vergilyn
 * @since 2021-11-15
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
// Warmup: 2 iterations, 1000 ms each
@Warmup(iterations = 10, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
// Measurement: 5 iterations, 1000 ms each
@Measurement(iterations = 30, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class RandomJMH {

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(RandomJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.threads(100)
				.build();

		new Runner(opt).run();
	}

	@Benchmark
	public void random() {
		final int nextInt = RandomUtils.nextInt();
		// print("random", nextInt);
	}

	@Benchmark
	public void threadLocalRandom() {
		// 多线程时，非主线程必须调用1次 `ThreadLocalRandom.current()`获取新的seed。
		// 否则，得到的`nextInt`是一样的。
		final int nextInt = ThreadLocalRandom.current().nextInt();
		// print("threadLocalRandom", nextInt);
	}

	private static void print(String flag, Object value){
		System.out.printf("[%s][%s] >>>> %s", flag, Thread.currentThread().getName(), value);
	}
}
