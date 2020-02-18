package com.vergilyn.examples.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author VergiLyn
 * @date 2020-01-13
 * 
 */
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)  // 预热5次，每次间隔1s
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)  // 测试5次，每次间隔1s
public class ArrayListWithCapacityJMH {
    private static final int SIZE = 1000;
    private static final String ELEMENT = "element";

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ArrayListWithCapacityJMH.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 5000)
    @Measurement(iterations = 5, batchSize = 5000)
    @BenchmarkMode(Mode.SingleShotTime)
    public List<String> withCapacity() {
        List<String> list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++){
            list.add(ELEMENT);
        }
        return list;
    }

    @Benchmark
    @Warmup(iterations = 5, batchSize = 5000)
    @Measurement(iterations = 5, batchSize = 5000)
    @BenchmarkMode(Mode.SingleShotTime)
    public List<String> withoutCapacity() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++){
            list.add(ELEMENT);
        }
        return list;
    }
}
