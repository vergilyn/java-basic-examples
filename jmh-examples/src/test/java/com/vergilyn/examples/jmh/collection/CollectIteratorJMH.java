package com.vergilyn.examples.jmh.collection;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * for-i > for-each > iterator > stream/parallelStream
 * <p>
 *   1. 预热对stream影响蛮大，预热后stream已经很接近for-each（甚至优于for-each，不排除JMH测试方法存在问题）
 * </p>
 * @author vergilyn
 * @date 2020-05-08
 *
 * @see CollectIteratorTest
 * @see <a href="https://blog.csdn.net/sarielangel/article/details/83899377">java8 为stream().forEach效率正名</a>
 * @see <a href="https://blog.csdn.net/zhoufanyang_china/article/details/89888689">JVM预热</a>
 */
@State(Scope.Thread)
public class CollectIteratorJMH {

    private static final List<Integer> LIST = Stream.iterate(1, i -> i++)
                                                .limit(100_0000)
                                                .collect(Collectors.toList());

    private static final AtomicInteger FORI_COUNT = new AtomicInteger(0);
    private static final AtomicInteger FOREACH_COUNT = new AtomicInteger(0);
    private static final AtomicInteger ITERATOR_COUNT = new AtomicInteger(0);
    private static final AtomicInteger STREAM_COUNT = new AtomicInteger(0);
    private static final Options OPTIONS = new OptionsBuilder()
            .include(CollectIteratorJMH.class.getSimpleName())
            .forks(1)
            .threads(1)
            .warmupIterations(1)
            .warmupBatchSize(1)
            .measurementIterations(1)
            .measurementBatchSize(1)
            .mode(Mode.SingleShotTime)
            .timeUnit(TimeUnit.MICROSECONDS)
            .build();

    public static void main(String[] args) throws RunnerException {

        new Runner(OPTIONS).run();
    }

    // @TearDown
    public void tearDown(){
        int expected = (OPTIONS.getWarmupIterations().get() * OPTIONS.getWarmupBatchSize().get()
                + OPTIONS.getMeasurementIterations().get() * OPTIONS.getMeasurementBatchSize().get())
                * OPTIONS.getThreads().get() * LIST.size();

        if (FORI_COUNT.get() > 0){
            Assertions.assertEquals(expected, FORI_COUNT.get());
        }

        if (FOREACH_COUNT.get() > 0){
            Assertions.assertEquals(expected, FOREACH_COUNT.get());
        }

        if (ITERATOR_COUNT.get() > 0){
            Assertions.assertEquals(expected, ITERATOR_COUNT.get());
        }

        if (STREAM_COUNT.get() > 0){
            Assertions.assertEquals(expected, STREAM_COUNT.get());
        }
    }

    @Benchmark
    public void fori(){
        for (int i = 0, len = LIST.size(); i < len; i++){
            FORI_COUNT.incrementAndGet();
        }
    }

    @Benchmark
    public void foreach(){
        for (Integer i : LIST){
            FOREACH_COUNT.incrementAndGet();
        }
    }

    @Benchmark
    public void iterator(){
        Iterator<Integer> iterator = LIST.iterator();
        while (iterator.hasNext()){
            iterator.next();
            ITERATOR_COUNT.incrementAndGet();
        }
    }

    @Benchmark
    public void stream(){
        LIST.stream().forEach(integer -> {
            STREAM_COUNT.incrementAndGet();
        });
    }
}
