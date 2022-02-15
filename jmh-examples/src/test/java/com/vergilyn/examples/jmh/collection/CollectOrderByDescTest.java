package com.vergilyn.examples.jmh.collection;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.RepeatedTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * 场景：
 * <pre>
 * ArrayList 按天 <b>正序</b> 遍历并 {@linkplain java.util.Collection#add(Object)}，但最后要<b>倒序</b>显示。
 *
 * 方式一：`add(0, obj)`：但貌似性能比`add(obj)`低（看源码）
 *
 * 方式二：先`add(obj)`，然后再 {@linkplain Collections#reverse(List)}
 *
 * 利用JMH 对比2种方法的性能，数据量 400, 40_000, 1_000_000
 * </pre>
 *
 * @author vergilyn
 * @since 2022-02-11
 */
public class CollectOrderByDescTest {
	private static final int LIMIT_4_000 = 4_000;
	private static final int LIMIT_40_000 = 40_000;
	private static final int LIMIT_1_000_000 = 1_000_000;

	private final int limit = LIMIT_4_000;
	private final LocalDate now = LocalDate.now();

	/**
	 * <pre> limit = LIMIT_4_000;
	 * methodOne >>>> cost: 6.352 ms
	 * methodOne >>>> cost: 2.324 ms
	 * methodOne >>>> cost: 1.751 ms
	 * methodOne >>>> cost: 1.626 ms
	 * methodOne >>>> cost: 1.756 ms
	 * methodOne >>>> cost: 1.606 ms
	 * methodOne >>>> cost: 1.768 ms
	 * methodOne >>>> cost: 1.608 ms
	 * methodOne >>>> cost: 1.641 ms
	 * methodOne >>>> cost: 1.606 ms
	 * </pre>
	 */
	@RepeatedTest(10)
	public void methodOne() {
		Stopwatch stopwatch = Stopwatch.createStarted();

		List<TestBean> result = Lists.newArrayListWithCapacity(limit);
		LocalDate value = now;
		for (int i = 0; i < limit; i++) {
			result.add(0, new TestBean(value));
			value = value.plusDays(1);
		}


		System.out.println("methodOne >>>> cost: " + stopwatch.stop());
	}

	/**
	 * <pre> limit = LIMIT_4_000;
	 * methodTwo >>>> cost: 7.421 ms  // 首次有误差，其实大概率都比`methodOne`要少。
	 * methodTwo >>>> cost: 5.140 ms
	 * methodTwo >>>> cost: 951.5 μs
	 * methodTwo >>>> cost: 564.8 μs
	 * methodTwo >>>> cost: 380.6 μs
	 * methodTwo >>>> cost: 349.2 μs
	 * methodTwo >>>> cost: 332.7 μs
	 * methodTwo >>>> cost: 340.4 μs
	 * methodTwo >>>> cost: 342.9 μs
	 * methodTwo >>>> cost: 344.2 μs
	 * methodTwo >>>> cost: 342.5 μs
	 * </pre>
	 */
	@RepeatedTest(10)
	public void methodTwo() {
		Stopwatch stopwatch = Stopwatch.createStarted();

		List<TestBean> result = Lists.newArrayListWithCapacity(limit);
		LocalDate value = now;
		for (int i = 0; i < limit; i++) {
			result.add(new TestBean(value));
			value = value.plusDays(1);
		}
		Collections.reverse(result);

		System.out.println("methodTwo >>>> cost: " + stopwatch.stop());
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
