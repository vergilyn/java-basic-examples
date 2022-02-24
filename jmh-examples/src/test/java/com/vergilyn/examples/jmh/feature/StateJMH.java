package com.vergilyn.examples.jmh.feature;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Scope.Benchmark: 该状态的意思是会在所有的Benchmark的工作线程中共享变量内容。
 * （所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；）
 *
 * <p>Scope.Group: 同一个Group的线程可以享有同样的变量
 *
 * <p>Scope.Thread: 每隔线程都享有一份变量的副本，线程之间对于变量的修改不会相互影响。（默认的State，每个测试线程分配一个实例；）
 *
 * @author vergilyn
 * @since 2022-02-24
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, batchSize = 2)
@Measurement(iterations = 5, batchSize = 4)
@BenchmarkMode(Mode.SingleShotTime)
@Threads(6)
public class StateJMH {

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(StateJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.build();

		new Runner(opt).run();
	}

	/* Scope.Thread >>> 31 次相同的 hashcode，31 = 2 * 2 + 5 * 4 + 7
	 *   6个线程，每个线程各执行 2*2次 warmup，和 5*4次 measurement。
	 *
	 * Scope.Benchmark >>>> 151 次相同的 hashcode， 151 = (2 * 2 * 6) + (5 * 4 * 6) + (2 + 5)
	 */
	@Benchmark
	public void method() {
		System.out.println("[Benchmark]this.hashcode >>>> " + this.hashCode());
	}

	@TearDown(Level.Iteration)
	public void tearDown(){
		System.out.println("[tearDown]this.hashcode >>>> " + this.hashCode());
	}
}
