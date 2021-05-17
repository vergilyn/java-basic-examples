package com.vergilyn.examples.jmh.collection;

import java.util.ArrayList;
import java.util.List;
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
 * <pre>
 *   Benchmark                                 Mode  Cnt    Score     Error  Units
 *   ArrayListWithCapacityJMH.withCapacity       ss   10  442.180 ± 153.569  us/op
 *   ArrayListWithCapacityJMH.withoutCapacity    ss   10  789.920 ± 132.204  us/op
 * </pre>
 * @author vergilyn
 * @date 2020-01-13
 */
@State(Scope.Thread)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class ArrayListWithCapacityJMH {
    private static final int SIZE = 100_000;
    private static final String ELEMENT = "element";

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ArrayListWithCapacityJMH.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void withCapacity() {
        List<String> list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++){
            list.add(ELEMENT);
        }
    }

    @Benchmark
    public void withoutCapacity() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < SIZE; i++){
            list.add(ELEMENT);
        }
    }
}
