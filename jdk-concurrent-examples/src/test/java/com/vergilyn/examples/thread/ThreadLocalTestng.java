package com.vergilyn.examples.thread;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * <p>
 *   <a href="https://mp.weixin.qq.com/s/8Ql-5kaUtxiCWyHR6uPPBw">匠心零度 - 手撕面试题ThreadLocal！！！</a>
 *
 *   ThreadLocal用在什么地方？<br/>
 *   1. 多线程场景；<br/>
 *   2. 保存线程上下文信息，在任意需要的地方可以获取；（比如 spring 事务管理）<br/>
 *   3. 线程安全的，避免某些情况需要考虑线程安全必须同步带来的性能损失；<br/>
 *
 * </p>
 *
 * <p>
 *   alibaba 开发手册：<br/>
 *   1. 必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，如果不清理自定义的 ThreadLocal变量，
 *      可能会影响后续业务逻辑和造成内存泄露等问题。
 *      尽量在代理中使用try-finally块进行回收。<br/>
 *
 *   2. 【参考】ThreadLocal无法解决共享对象的更新问题，<code>ThreadLocal对象建议使用static修饰</code>。
 *      这个变量是针对一个线程内所有操作共享的，所以设置为 静态变量，
 *      所有此类实例共享此静态变量，也就是说在类第一次被使用时装载，只分配一块存储空间，所有此类的对象(只要是这个线程内定义的)都可以操控这个变量。
 * </p>
 * spring 源码中大量使用了 ThreadLocal
 * @author vergilyn
 * @date 2020-01-24
 */
@Slf4j
public class ThreadLocalTestng {
    /* ThreadLocal对象建议使用static修饰 */
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();


    /**
     * 从运行的结果可知，不同线程之间的 ThreadLocal.set() 互不影响。原因：<br/>
     *   {@linkplain Thread} 类中存在 成员变量{@linkplain Thread#threadLocals}(类型：{@linkplain ThreadLocal.ThreadLocalMap}) <br/>
     *   也就是说每个线程有一个自己的`ThreadLocalMap`，每个线程往这个ThreadLocals中读/写，所以是互相不会影响的。
     *   
     * @see ThreadLocal#set(Object) - {@linkplain ThreadLocal#set(Object)}会去获取{@linkplain Thread#threadLocals}
     */
    @Test(threadPoolSize = 2, invocationCount = 10)
    public void test01(){
        Integer integer = THREAD_LOCAL.get();
        if (integer == null){
            THREAD_LOCAL.set(1);
        }else {
            THREAD_LOCAL.set(integer + 1);
        }

        log.info("Thread: {}, value: {}", Thread.currentThread().getName(), THREAD_LOCAL.get());
    }

}
