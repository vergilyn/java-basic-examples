package com.vergilyn.examples.forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

/**
 * @author vergilyn
 * @since 2021-09-09
 *
 */
public abstract class AbstractForkJoinPoolTests {

    /**
     * @see org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean#afterPropertiesSet()
     */
    protected ForkJoinPool create(){
        int parallelism = Math.min(0x7fff, Runtime.getRuntime().availableProcessors());

        ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = NamedForkJoinWorkerThread::new;

        Thread.UncaughtExceptionHandler handler = null;

        boolean asyncMode = false;

        return new ForkJoinPool(parallelism, threadFactory, handler, asyncMode);
    }

    protected ForkJoinPool commonPool(){
        return ForkJoinPool.commonPool();
    }

    protected void preventExit(){
    }

    protected static class NamedForkJoinWorkerThread extends ForkJoinWorkerThread {

        /**
         * Creates a ForkJoinWorkerThread operating in the given pool.
         *
         * @param pool the pool this thread works in
         *
         * @throws NullPointerException if pool is null
         */
        protected NamedForkJoinWorkerThread(ForkJoinPool pool) {
            super(pool);

            setName("vergilyn-fork-join-worker-thread-" + super.getPoolIndex());
        }
    }
}
