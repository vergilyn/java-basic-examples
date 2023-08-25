package com.vergilyn.examples.usage.u0015;

import cn.hutool.core.thread.NamedThreadFactory;
import io.netty.util.HashedWheelTimer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 时间轮
 * <pre>
 *   实现参考：
 *     - netty-common, {@link io.netty.util.HashedWheelTimer}
 *     - micrometer-core, {@link io.micrometer.core.instrument.distribution.TimeWindowMax}
 *
 *   使用示例参考：
 *     - dubbo
 *     - dynamic-tp
 * </pre>
 *
 * @author vergilyn
 * @since 2023-05-08
 */
public class HashedWheelTimerTest {

    final HashedWheelTimer _timer = new HashedWheelTimer(
            new NamedThreadFactory("test-HashedWheelTimer", true),
            1,
            TimeUnit.SECONDS,
            12
    );

    @SneakyThrows
    @Test
    void basic(){
        int times = 10;
        CountDownLatch countDownLatch = new CountDownLatch(times);
        AtomicInteger index = new AtomicInteger(0);

        while (index.get() < times) {
            int idx = index.incrementAndGet();

            _timer.newTimeout(timeout -> {
                countDownLatch.countDown();
                System.out.printf("[%d] end: %s \n", idx, LocalTime.now());
            }, idx, TimeUnit.SECONDS);
        }

        countDownLatch.await();
    }

    /**
     * 假设有4个待执行任务，分别对应 bucket 1~4，且每个任务执行耗时依次是 1s、2s、3s、4s。
     *
     * <p> Q1. 验证：由于 {@code `bucket-2`}执行时间超过了 tickDuration，是否会跳过 {@code `bucket-3`} 执行？
     * <br/> A. 不会跳过{@code `bucket-3`}，还是会按 `1~4`依次执行。
     *
     * <p> Q2. 验证：bucket中的task是同步执行，还是异步执行？
     * <br/> A. 同步执行。
     * 结合`Q1`，由于{@code `bucket-2`}执行时间已经超过 tickDuration。
     * 所以在 {@code `bucket-2`}的全部task执行完成后，会立即执行 {@code `bucket-3`} 中的task。
     *
     * <p> 由上可知，{@link HashedWheelTimer}实现的只是 <b>简单的单时间轮</b>。
     * 更复杂的时间轮参考：<a href="https://blog.csdn.net/qianshangding0708/article/details/123173390">时间轮（TimingWheel）算法总结</a>
     *
     * @see HashedWheelTimer.Worker#run()
     * @see HashedWheelTimer.HashedWheelBucket#expireTimeouts(long)
     */
    @SneakyThrows
    @Test
    void validTaskSyncExecutor(){
        int times = 4;
        CountDownLatch countDownLatch = new CountDownLatch(times);
        AtomicInteger index = new AtomicInteger(0);

        while (index.get() < times) {
            int idx = index.incrementAndGet();

            _timer.newTimeout(timeout -> {
                try {
                    LocalTime begin = LocalTime.now();
                    TimeUnit.SECONDS.sleep(idx);
                    System.out.printf("[%d] begin: %s, end: %s \n", idx, begin, LocalTime.now());
                }finally {
                    countDownLatch.countDown();
                }

            }, idx, TimeUnit.SECONDS);
        }

        countDownLatch.await();

    }
}
