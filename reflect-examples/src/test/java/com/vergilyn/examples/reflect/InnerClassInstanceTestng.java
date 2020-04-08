package com.vergilyn.examples.reflect;

import java.lang.reflect.Constructor;

import org.testng.annotations.Test;

/**
 * <a href="https://www.v2ex.com/t/585169">Java 的内部类反射，使用 newInstance 总是抛 NoSuchMethodException 异常呢？</a>
 * @author vergilyn
 * @date 2020-04-08
 */
public class InnerClassInstanceTestng {
    private static final ClassLoader CLASS_LOADER = InnerClassInstanceTestng.class.getClassLoader();
    private static final String OUTER_CLASS = "com.vergilyn.examples.reflect.OuterClass";
    // 备注：内部类的全限定名
    private static final String STATIC_INNER_CLASS = "com.vergilyn.examples.reflect.OuterClass$StaticInnerClass";
    private static final String NORMAL_INNER_CLASS = "com.vergilyn.examples.reflect.OuterClass$NormalInnerClass";

    @Test(description = "静态内部类 不会报错")
    public void staticInnerClass(){
        try {
            Class<?> clazz = Class.forName(STATIC_INNER_CLASS, true, CLASS_LOADER);
            OuterClass.StaticInnerClass instance = (OuterClass.StaticInnerClass) clazz.newInstance();

            System.out.println(instance);
            instance.print();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * `一般的内部类`会报错，主要是`class.newInstance()`报错：
     * <pre>
     *  java.lang.InstantiationException: com.vergilyn.examples.reflect.OuterClass$NormalInnerClass
     *     at java.lang.Class.newInstance(Class.java:427)
     *     at com.vergilyn.examples.reflect.InnerClassInstanceTestng.normalInnerClassError(InnerClassInstanceTestng.java:32)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     *     at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     *     at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     *     at java.lang.reflect.Method.invoke(Method.java:498)
     * Caused by: java.lang.NoSuchMethodException: com.vergilyn.examples.reflect.OuterClass$NormalInnerClass.<init>()
     * 	   at java.lang.Class.getConstructor0(Class.java:3082)
     * 	   at java.lang.Class.newInstance(Class.java:412)
     * 	   ... 28 more
     * </pre>
     */
    @Test(description = "一般内部类 通过反射实例化会报错")
    public void normalInnerClassError(){
        try {
            Class<?> clazz = Class.forName(NORMAL_INNER_CLASS, true, CLASS_LOADER);
            OuterClass.NormalInnerClass instance = (OuterClass.NormalInnerClass) clazz.newInstance();

            System.out.println(instance);

            instance.print();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * normal-inner-class 的无参构造函数 其实是有一个隐藏参数：外部类的指针`this`。
     * 所以，其实 normal-inner-class 实例化时，必须先实例化 outer-class，并传递 outer-class 的`this`。
     */
    @Test
    public void normalInnerClassRight(){
        try {
            // 1. 先要 实例化outer-class
            OuterClass outer = new OuterClass();

            Class<?> normalInnerClass = Class.forName(NORMAL_INNER_CLASS, true, CLASS_LOADER);

            // 2. 一般内部类 的无参构造函数 需要传递外部内的`this`（外部内的实例化对象）
            Constructor<?> constructor = normalInnerClass.getDeclaredConstructor(OuterClass.class);
            OuterClass.NormalInnerClass instance = (OuterClass.NormalInnerClass) constructor.newInstance(outer);

            System.out.println(instance);
            instance.print();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
