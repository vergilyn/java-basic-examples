package com.vergilyn.examples.jmh.collection;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

/**
 * recommend: <code>{@linkplain #partition()}</code>
 *
 * 执行速度上，{@linkplain #partition()}比{@linkplain #stream()}快了10倍。
 * @author vergilyn
 * @date 2021-01-22
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS) // 输出结果的时间粒度为微秒
@State(Scope.Thread) // 每个测试线程一个实例
public class ListSplitJMH {

	public static final int MAX_SIZE = 100_091;
	public static final int SECTION_SIZE = 200;

	private final Supplier<List<Integer>> supplier = () -> Stream.iterate(0, i -> ++i)
																.limit(MAX_SIZE)
																.collect(Collectors.toList());

	@Benchmark
	public List<List<Integer>> stream(){
		List<Integer> source = supplier.get();

		int i = 0;
		List<List<Integer>> result = Lists.newArrayList();
		while (i < MAX_SIZE){
			result.add(source.stream().skip(i).limit(SECTION_SIZE).collect(Collectors.toList()));
			i += SECTION_SIZE;
		}

		return result;
	}

	@Benchmark
	public List<List<Integer>> partition(){
		List<Integer> source = supplier.get();
		return Lists.partition(source, SECTION_SIZE);
	}

	public static void main(String[] args) throws RunnerException {
		// 使用一个单独进程执行测试，执行5遍warm-up，然后执行5遍测试
		Options opt = new OptionsBuilder()
				.include(ListSplitJMH.class.getSimpleName())
				.forks(1)   // 执行线程数
				.warmupIterations(10)    // 预热次数
				.warmupTime(TimeValue.seconds(1))
				.measurementIterations(10)   // 测试次数
				.measurementTime(TimeValue.seconds(1))
				.build();

		new Runner(opt).run();
	}
}
