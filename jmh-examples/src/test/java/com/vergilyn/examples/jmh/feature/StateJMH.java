package com.vergilyn.examples.jmh.feature;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * {@link Scope#Benchmark}: 该状态的意思是会在所有的Benchmark的工作线程中共享变量内容。
 * （所有测试线程共享一个实例，用于测试有状态实例在多线程共享下的性能；）
 *
 * <p>{@link Scope#Group}: 同一个Group的线程可以享有同样的变量
 *
 * <p>{@link Scope#Thread}: 每隔线程都享有一份变量的副本，线程之间对于变量的修改不会相互影响。（默认的State，每个测试线程分配一个实例；）
 *
 * @author vergilyn
 * @since 2022-02-24
 */
// @State(Scope.Benchmark)
@State(Scope.Thread)
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

	/**
	 *
	 * 1) 执行 {@code iterations = 2} 次预热；<br/>
	 * 2) 每次预热，执行 {@code (batchSize = 2) * (Threads = 6) = 12} 次调用 {@link Benchmark} 测试方法；
	 *
	 * <p> 即：
	 * <p> {@link Scope#Thread}：共 {@code 31 =  2 * 2 + 5 * 4 + (2 + 5)} 次相同的 hashcode。
	 * <br/> 解释：6个线程，每个线程执行 {@code 2 * 2} 次warmup、{@code 5 * 4} 次measurement、{@code 2 + 5} 次tearDown。
	 *
	 * <p> {@link Scope#Benchmark}: 共 {@code 151 = (2 * 2 * 6 = 24) + (5 * 4 * 6 = 120) + (2 + 5)} 次相同的 hashcode。
	 */
	@Benchmark
	public void method() {
		System.out.printf("[%s][Benchmark]this.hashcode >>>> %s\n", Thread.currentThread().getName(), this.hashCode());
	}

	@TearDown(Level.Iteration)
	public void tearDown(){
		System.out.printf("[%s][tearDown]this.hashcode >>>> %s\n", Thread.currentThread().getName(), this.hashCode());
	}
}
