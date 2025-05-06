package com.vergilyn.examples.forkjoinpool;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ForkJoinPoolClassLoaderTest {

    /**
     * 任务窃取（Work Stealing）是一种并发编程中的调度策略，它允许空闲的工作线程从其他忙碌的线程中“窃取”任务来执行。这种机制有助于提高多核处理器上的并行处理效率，并减少线程间的竞争和等待时间。
     * <p> 所以，例如{@link List#parallelStream()}中的部分任务，其执行线程是{@code main}，而不是{@code ForkJoinPool.commonPool-worker-x}
     */
    @Test
    void test_workStealing() {
        new ForkJoinWorkerThreadFactoryDelegate();

        List<Integer> list = Stream.iterate(1, i -> i + 1).limit(20).toList();
        List<Context> contexts = list.parallelStream().map(this::testMethod).toList();

        String mainThreadName = Thread.currentThread().getName();

        assertThat(contexts).anySatisfy(context -> {
            assertThat(context.getThreadName()).isEqualTo(mainThreadName);
        });
    }

    /**
     * 如何避免work-stealing？
     *
     */
    @SneakyThrows
    @Test
    void test_avoidWorkStealing() {

        String mainThreadName = Thread.currentThread().getName();
        int limit = 20;
        List<Integer> list = Stream.iterate(1, i -> i + 1).limit(limit).toList();

        CountDownLatch countDownLatch = new CountDownLatch(limit);

        List<Context> contexts = list.parallelStream().map(id -> {
            countDownLatch.countDown();
            return testMethod(id);
        }).toList();

        // 无法避免work-stealing
        // countDownLatch.await();

        // 无法避免work-stealing
        // new Semaphore(0).tryAcquire(2, TimeUnit.SECONDS);

        // assertThat(contexts).noneSatisfy(context -> {
        //     assertThat(context.getThreadName()).isEqualTo(mainThreadName);
        // });
    }

    private Context testMethod(Integer id) {

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Class<? extends ClassLoader> contextClassLoaderClass = contextClassLoader.getClass();

        String tname = Thread.currentThread().getName();

        String msg = String.format("[STARTUP][%02d][%s] >>>> ClassLoader: %s", id, tname, contextClassLoaderClass);
        System.out.println(msg);

        return new Context(id, tname, contextClassLoader);
    }

    @Data
    @NoArgsConstructor
    private static class Context {

        private Integer id;

        private String threadName;

        private ClassLoader contextClassLoader;

        public Context(Integer id, String threadName, ClassLoader contextClassLoader) {
            this.id = id;
            this.threadName = threadName;
            this.contextClassLoader = contextClassLoader;
        }
    }
}
