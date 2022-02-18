package com.vergilyn.examples.jmh.concurrent;

import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * {@linkplain AtomicLong} VS {@linkplain LongAdder} VS {@linkplain LongAccumulator}：<br/>
 * <a href="https://blog.csdn.net/li_w_ch/article/details/112538295">区别</a> <br/>
 * 1) 貌似  LongAccumulator/LongAdder  优于 AtomicLong <br/>
 * 2) LongAccumulator 比 LongAdder  功能更丰富 <br/>
 * 3) <blockquote>
 *  使用AtomicLong时，在高并发下大量线程会同时去竞争更新同一个原子变量，
 *  但是由于同时只有一个线程的CAS操作会成功，这就造成了大量线程竞争失败后，会通过无限循环不断进行自旋尝试CAS的操作，而这会白白浪费CPU资源。
 *
 *  JDK8新增了一个原子性递增或者递减类LongAdder用来克服在高并发下使用AtomicLong的缺点。
 *  它通过把一个变量分解为多个变量，让同样多的线程去竞争多个资源。
 * </blockquote>
 *
 * <pre> IS_GET = false
 * Benchmark                      Mode  Cnt    Score    Error  Units
 * AtomicIncrementJMH.atomicLong    ss    5  225.294 ± 22.065  ms/op
 * AtomicIncrementJMH.longAdder     ss    5  219.989 ± 26.243  ms/op
 * </pre>
 *
 * <pre> IS_GET = true, {@linkplain LongAdder#longValue()} 相对更耗时（因为内部是 求和，而{@linkplain AtomicLong}只是得到成员变量 ）！
 * Benchmark                      Mode  Cnt    Score    Error  Units
 * AtomicIncrementJMH.atomicLong    ss    5  221.578 ± 42.453  ms/op
 * AtomicIncrementJMH.longAdder     ss    5  224.166 ± 31.467  ms/op
 * （几乎是相近）
 * </pre>
 *
 * <p>备注：
 * <p>1. <a href="https://www.jianshu.com/p/381f9b39c941">AtomicLong和LongAdder的区别</a>
 * <blockquote>{@linkplain LongAdder#longValue()}
 *     当计数的时候，将base和各个cell元素里面的值进行叠加，从而得到计算总数的目的。
 *     这里的问题是 <b>在计数的同时如果修改cell元素，有可能导致计数的结果不准确。</b>
 * </blockquote>
 *
 * <p>个人结论：
 * 1) 可能是JMH代码写法不正确，并未感觉 `LongAdder#incr` 在业务代码中提升巨大（在业务代码中的 10ms差 真无关重要） <br/>
 * 2) 如果需要 get-value，几乎无差别。反而{@linkplain LongAdder#longValue()}更耗时，<b>且结果可能不准确</b>！ <br/>
 * 3) 按教程解释：当只需要 add/incr之类时，可以选择 LongAdder。但需要 compareAndSet 时，选择 AtomicLong
 *
 * @author vergilyn
 * @since 2022-02-17
 *
 * @see AtomicIncrementOptJMH
 * @deprecated 这种测试方法的“干扰因素”太多，并不是单纯的对比 `AtomicLong & LongAdder`
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, batchSize = 10)
@Measurement(iterations = 5, batchSize = 10)
@BenchmarkMode(Mode.SingleShotTime)
public class AtomicIncrementJMH {
	private static final boolean IS_GET = true;
	private static final int iterations = 100_000;

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(AtomicIncrementJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.build();

		new Runner(opt).run();

	}

	// 这种写法“干扰因素”太多了...
	@SneakyThrows
	private static void concurrentExecute(Handler handler){
		CountDownLatch countDownLatch = new CountDownLatch(iterations);
		ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

		for (int i = 0; i < iterations; i++) {
			threadPool.submit(() -> {
				try {
					handler.execute();
				}finally {
					countDownLatch.countDown();
				}
			});
		}

		threadPool.shutdown();
		countDownLatch.await();

		if (IS_GET){
			Assertions.assertThat(handler.get()).isEqualTo(iterations);
		}
	}

	@Benchmark
	public void atomicLong() {
		AtomicLong atomicLong = new AtomicLong();

		concurrentExecute(new Handler() {
			@Override
			public void execute() {
				atomicLong.incrementAndGet();
			}

			@Override
			public long get() {
				return atomicLong.get();
			}
		});

	}

	@Benchmark
	public void longAdder() {
		LongAdder longAdder = new LongAdder();

		concurrentExecute(new Handler() {
			@Override
			public void execute() {
				longAdder.increment();
			}

			@Override
			public long get() {
				return longAdder.longValue();
			}
		});

	}

	/**
	 * 内部实现与 {@linkplain LongAdder} 类似，但功能更丰富
	 */
	public void LongAccumulator(){
		// ...
	}

	private static interface Handler {
		void execute();

		long get();
	}
}
