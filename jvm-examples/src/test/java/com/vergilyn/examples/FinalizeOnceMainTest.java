package com.vergilyn.examples;

import java.util.concurrent.TimeUnit;

import lombok.ToString;

/**
 * GC回收算法 - 可达性算法：
 * <p>
 *   The {@code finalize} method is never invoked more than once
 *   by a Java virtual machine for any given object.
 *
 * <p>
 *  finalize 方法只会被执行一次，如果第一次执行 finalize 方法此对象变成了可达确实不会回收
 * （如把当前对象的引用this赋值给某对象的类变量/成员变量，重新建立可达的引用），
 * 但如果对象再次被 GC，则会忽略 finalize 方法，对象会被回收！这一点切记！
 *
 * @author vergilyn
 * @date 2020-06-22
 */
public class FinalizeOnceMainTest {
    Garbage g1 = new Garbage(1);
    static Garbage g2 = new Garbage(2);
    static Garbage g3;

    public static void main(String[] args) throws InterruptedException {
        FinalizeOnceMainTest fo = new FinalizeOnceMainTest();
        fo.g1 = null;
        g2 = null;
        System.gc();  // 首次触发gc，调用`finalize` 为该对象重新建立可达的引用，该对象不被回收

        TimeUnit.SECONDS.sleep(2);  // 确保gc完成，否则可能出现 `g3 = g2 = null`

        g3 = g2;
        System.out.println("first GC, g3 >>>> " + g3);

        TimeUnit.SECONDS.sleep(2);

        g2 = null;
        g3 = null;
        System.gc();  // 再次调用gc，并不会再调用`finalize`

    }
}

@ToString
class Garbage {
    private int num = 0;

    Garbage(int num) {
        this.num = num;
    }

    /**
     * API - {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable {
        FinalizeOnceMainTest.g2 = this;  // 重新建立可达的引用
        System.out.println(this);
    }
}