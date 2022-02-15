package com.vergilyn.examples.jmh.collection;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 场景：
 * <pre>
 * ArrayList 按天 <b>正序</b> 遍历并 {@linkplain java.util.Collection#add(Object)}，但最后要<b>倒序</b>显示。
 *
 * 方式一：`add(0, obj)`：但貌似性能比`add(obj)`低（看源码）
 *
 * 方式二：先`add(obj)`，然后再 {@linkplain java.util.Collections#reverse(List)}
 *
 * 利用JMH 对比2种方法的性能，数据量 400, 40_000, 1_000_000
 * </pre>
 *
 * <pre> limit = LIMIT_4_000;
 * Benchmark                        Mode  Cnt  Score   Error  Units
 * CollectOrderByDescJMH.methodOne  avgt    5  1.403 ± 0.075  ms/op
 * CollectOrderByDescJMH.methodTwo  avgt    5  0.083 ± 0.001  ms/op
 * </pre>
 *
 * <pre> limit = LIMIT_40_000;
 * Benchmark                        Mode  Cnt    Score   Error  Units
 * CollectOrderByDescJMH.methodOne  avgt    5  142.435 ± 5.471  ms/op
 * CollectOrderByDescJMH.methodTwo  avgt    5    0.854 ± 0.033  ms/op
 * </pre>
 *
 * 结论：`方式二`在 执行速度上 都比`方式一`更快。
 *
 * @author vergilyn
 * @since 2022-02-11
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
// Warmup: 2 iterations, 1000 ms each
@Warmup(iterations = 2, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
// Measurement: 5 iterations, 1000 ms each
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
public class CollectOrderByDescJMH {
	private static final int LIMIT_4_000 = 4_000;
	private static final int LIMIT_40_000 = 40_000;
	private static final int LIMIT_1_000_000 = 1_000_000;

	private final int limit = LIMIT_4_000;
	private final LocalDate now = LocalDate.now();

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(CollectOrderByDescJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.build();

		new Runner(opt).run();
	}

	@Benchmark
	public void methodOne() {
		List<TestBean> result = Lists.newArrayListWithCapacity(limit);

		LocalDate value = now;
		for (int i = 0; i < limit; i++) {
			result.add(0, new TestBean(value));
			value = value.minusDays(1);
		}
	}

	@Benchmark
	public void methodTwo() {
		List<TestBean> result = Lists.newArrayListWithCapacity(limit);

		LocalDate value = now;
		for (int i = 0; i < limit; i++) {
			result.add(new TestBean(value));
			value = value.minusDays(1);
		}

		Collections.reverse(result);
	}

	@Getter
	@Setter
	private static class TestBean {
		private LocalDate localDate;

		public TestBean(LocalDate localDate) {
			this.localDate = localDate;
		}
	}
}
