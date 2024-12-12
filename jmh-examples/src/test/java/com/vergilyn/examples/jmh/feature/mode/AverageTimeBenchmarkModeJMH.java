package com.vergilyn.examples.jmh.feature.mode;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * <h4>输出示例</h4>
 * <pre>
 * </pre>
 *
 * <h4>解释</h4>
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.MINUTES)
@Threads(3)
@Fork(1)
@Measurement(iterations = 4, time = 2, timeUnit = TimeUnit.MINUTES)
@BenchmarkMode(Mode.AverageTime)
public class AverageTimeBenchmarkModeJMH {

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
