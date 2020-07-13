package com.vergilyn.examples.concurrent.atomic;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

import org.testng.annotations.Test;

/**
 * netty 中的使用：
 * <pre>
 *   // org.apache.dubbo.remoting.transport.netty4.NettyClient#doConnect()
 *   ChannelFuture future = bootstrap.connect(...);
 *   boolean ret = future.awaitUninterruptibly(timeout, MILLISECONDS);
 *
 *   // io.netty.util.concurrent.DefaultPromise
 *   private static final AtomicReferenceFieldUpdater<DefaultPromise, Object> RESULT_UPDATER =
 *             AtomicReferenceFieldUpdater.newUpdater(DefaultPromise.class, Object.class, "result");
 *   private volatile Object result;
 *
 * </pre>
 *
 *
 * @author vergilyn
 * @date 2020-07-13
 *
 */
public class AtomicReferenceFieldUpdaterTestng {

    private static final AtomicReferenceFieldUpdater<AtomicReferenceFieldUpdaterTestng, String> RESULT_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(AtomicReferenceFieldUpdaterTestng.class, String.class, "result");

    // Caused by: java.lang.IllegalArgumentException: Must be volatile type
    private volatile String result;

    @Test(invocationCount = 4, threadPoolSize = 2)
    public void test(){
        boolean bool = RESULT_UPDATER.compareAndSet(this, null, "vergilyn");

        System.out.printf("[%s] >>>> bool: %b, result: %s", Thread.currentThread().getName(), bool, result);

    }

}
