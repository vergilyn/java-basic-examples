package com.vergilyn.examples.jmh.collection;

import java.util.concurrent.TimeUnit;

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
 * <a href="https://segmentfault.com/a/1190000012688298">java性能优化笔记 —— for循环</a>：
 * <pre>
 * 1) 嵌套循环应该遵循“外小内大”的原则  （主要理解优化原理）
 * 2) 循环变量的实例化应该尽量放在循环外进行  （并不明显）
 * 3) 提取与循环无关的表达式
 * 4) 消除循环终止判断时的方法调用  优化写法  for(int i = 0; i < size; i++)，  不要写成 `i < list.size()`
 * 5) 捕获异常是很耗资源的，所以不要讲try catch放到循环内部。
 * </pre>
 *
 * @author vergilyn
 * @since 2021-08-09
 *
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
// Warmup: 2 iterations, 1000 ms each
@Warmup(iterations = 2, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
// Measurement: 5 iterations, 1000 ms each
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class CollectionIteratorJMH {
	private static final int small = 100;
	private static final int biggest = 100_000_000;

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(CollectionIteratorJMH.class.getSimpleName())
					.forks(1)  // Number of forks to use in the run
				.build();

		new Runner(opt).run();
	}

	/**
	 * 推荐写法：外小内大。（提升其实很小，主要是养成良好的书写习惯。）
	 * 原理：内层循环中`int j` 的实例化次数不同。（所以还可以把`i/j`在循环外定义）
	 */
	@Benchmark
	public void recommend(){
		for (int i = 0; i < small; i++) {
			for (int j = 0; j < biggest; j++) {

			}
		}
	}

	@Benchmark
	public void incorrect(){
		for (int i = 0; i < biggest; i++) {
			for (int j = 0; j < small; j++) {

			}
		}
	}


}
