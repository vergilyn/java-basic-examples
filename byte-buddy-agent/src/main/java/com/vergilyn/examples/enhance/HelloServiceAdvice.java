package com.vergilyn.examples.enhance;


import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

// 定义增强器
class HelloServiceAdvice {

    // // 增强逻辑
    // @Advice.OnMethodEnter(suppress = Throwable.class)
    // public static void onEnter(@Advice.This Object thisRef,
    //                            @Advice.Origin String methodName,
    //                            @Advice.AllArguments Object[] args) {
    //     System.out.println("Entering method: " + methodName);
    //     System.out.println("Arguments: " + Arrays.toString(args));
    // }
    //
    // @Advice.OnMethodExit(onThrowable = Throwable.class, suppress = Throwable.class)
    // public static void onExit(@Advice.This Object thisRef,
    //                           @Advice.Origin String methodName,
    //                           @Advice.Return(readOnly = false) Object returnValue) {
    //     System.out.println("Exiting method: " + methodName);
    //     System.out.println("Return value: " + returnValue);
    // }

    @RuntimeType
    public static Object intercept(@Origin Method method,
                                   @SuperCall Callable<?> callable) throws Exception {

        System.out.println("[Agent] >>>> intercept: " + HelloServiceAdvice.class.getName());

        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println(method + " took " + (System.currentTimeMillis() - start));
        }

        return null;
    }
}