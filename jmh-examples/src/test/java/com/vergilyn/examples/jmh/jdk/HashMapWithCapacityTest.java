package com.vergilyn.examples.jmh.jdk;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *   2020-01-14:
 *     针对该基准测试，以下代码可能互相影响导致结果差异较大。
 *     建议参考代码{@linkplain org.openjdk.jmh.samples.JMHSample_26_BatchSize}
 * </pre>
 *
 * @author vergilyn
 * @date 2020-01-13
 * @see <a href="https://mp.weixin.qq.com/s/JkbtjPnaWNQ57t7MSb1JlQ">Java 并发测试神器：基准测试神器-JMH</a>
 *
 * @see org.openjdk.jmh.annotations.Setup
 * @see org.openjdk.jmh.samples.JMHSample_26_BatchSize
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)  // 预热5次，每次在1s内持续调用benchmark方法
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)  // 测试5次，每次在1s内持续调用benchmark方法
public class HashMapWithCapacityTest {
    private static final List<User> LIST;
    public static final int CAPACITY = 1000;

    static {
        LIST = Stream.iterate(new User(1), user -> new User(user.id + 1))
                .limit(CAPACITY)
                .collect(Collectors.toList());
    }

    @Benchmark
    public void newHashMapWithCapacity() {
        Map<Integer, String> map = new HashMap<>(CAPACITY);
        putMap(map);

    }

    @Benchmark
    public void newHashMap() {
        Map<Integer, String> map = new HashMap<>();
        putMap(map);
    }

    private void putMap(Map<Integer, String> map){
        LIST.forEach(user -> map.put(user.id, user.name));

    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(HashMapWithCapacityTest.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    @Data
    @ToString
    @NoArgsConstructor
    private static class User{
        private int id;
        private String name;

        public User(int id) {
            this.id = id;
            this.name = String.format("name - %04d", id);
        }
    }
}
