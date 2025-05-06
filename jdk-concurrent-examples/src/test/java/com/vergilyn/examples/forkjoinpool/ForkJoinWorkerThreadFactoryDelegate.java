package com.vergilyn.examples.forkjoinpool;

import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public class ForkJoinWorkerThreadFactoryDelegate {

    private static final ForkJoinPool.ForkJoinWorkerThreadFactory delegate;

    static {
        System.out.println("[ZMN] 通过反射修改 `ForkJoinPool.defaultForkJoinWorkerThreadFactory`");

        // 初始化 ForkJoinPool.defaultForkJoinWorkerThreadFactory
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

        delegate = ForkJoinPool.defaultForkJoinWorkerThreadFactory;

        // 通过反射修改代理对象
        ForkJoinPool.ForkJoinWorkerThreadFactory o = (ForkJoinPool.ForkJoinWorkerThreadFactory) Proxy.newProxyInstance(
                ForkJoinPool.class.getClassLoader(),
                new Class[]{ForkJoinPool.ForkJoinWorkerThreadFactory.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method,
                                         Object[] args) throws Throwable {

                        Object result = method.invoke(delegate, args);
                        if (result instanceof ForkJoinWorkerThread fjwt) {
                            fjwt.setContextClassLoader(Thread.currentThread().getContextClassLoader());
                            return fjwt;
                        }

                        return result;
                    }
                });

        try {
            ReflectionTestUtils.setField(ForkJoinPool.class, "defaultForkJoinWorkerThreadFactory", o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
