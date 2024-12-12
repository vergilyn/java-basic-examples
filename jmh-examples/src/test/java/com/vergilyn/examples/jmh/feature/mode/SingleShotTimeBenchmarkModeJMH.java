package com.vergilyn.examples.jmh.feature.mode;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * <h4>输出示例</h4>
 * <pre>
 * # Run progress: 0.00% complete, ETA 00:00:00
 * # Fork: 1 of 1
 * # Warmup Iteration   1: 2013.441 ms/op
 * # Warmup Iteration   2: 2008.330 ms/op
 * Iteration   1: 4051.812 ms/op
 * Iteration   2: 4075.388 ms/op
 *
 * # Run complete. Total time: 00:00:16
 *
 * Benchmark                                   Mode  Cnt     Score   Error  Units
 * SingleShotTimeBenchmarkModeJMH.measureName    ss    2  4063.600          ms/op
 * </pre>
 *
 * <h4>解释</h4>
 * 输出结果为 每次执行{@code Iteration} 操作的平均耗时。（只统计 {@link Benchmark}，不包括 {@link TearDown} 等。） <br/>
 *
 * 1) 得到的 <b>并不benchmark方法的平均耗时</b>，而是总共耗时！！！
 *
 * <p> （不同理解都是相同的结果）
 * <p> 理解1: 每轮次 {@code iterations}中，每个线程执行 {@link Benchmark} 的平均耗时，即 (batchSize = 2) * (benchmark = 1s) * (threads = 3) / (threads = 3) = 2s ）
 * <p> 理解2：测量阶段两次迭代的总时间，每次迭代包含四个样本的执行时间。正式的测量阶段也会执行两次，每次包含四个样本（即批处理大小为4）。
 *
 * 例如：
 * <br/> {@code Warmup}: 每次iterations的执行耗时 = {@code (batchSize = 2) * 1s = 2s}
 * <br/> {@code Measurement}: 每次iterations的执行耗时 = {@code (batchSize = 4) * 1s = 4s}
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, batchSize = 2)
@Threads(3)
@Fork(1)
@Measurement(iterations = 2, batchSize = 4)
@BenchmarkMode(Mode.SingleShotTime)
public class SingleShotTimeBenchmarkModeJMH {

    @Benchmark
    public void measureName(Blackhole bh) {
        sleepSafe(1_000);
    }

    @TearDown
    public void tearDown() {
        sleepSafe(2_000);
    }

    private static void sleepSafe(long timeoutMs) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeoutMs);
        } catch (Exception ignored) {
        }
    }
}
