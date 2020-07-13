package com.vergilyn.examples.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author vergilyn
 * @date 2020-07-07
 *
 * @see org.springframework.context.annotation.ConfigurationClassEnhancer
 */
public class FirstCglibMaintest {

    static class Target{
        public void a(){
            System.out.println("Target#a()");
        }
        public void b(){
            System.out.println("Target#b()");
        }
    }

    static class Interceptor implements net.sf.cglib.proxy.MethodInterceptor {
        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println("interceptor >>>> before");
            proxy.invokeSuper(obj, args);
            System.out.println("interceptor >>>> after");
            return obj;
        }
    }

    public static void main(String[] args) {
        // 设置这个属性，将代理类的字节码文件生成到F盘的code目录下
        // System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\temp");

        //实例化一个增强器，也就是cglib中的一个class generator
        Enhancer eh = new net.sf.cglib.proxy.Enhancer();
        eh.setSuperclass(Target.class);
        eh.setCallback(new Interceptor());

        /*
        // 设置拦截对象
        eh.setCallbacks(new Callback[]{new Interceptor(), NoOp.INSTANCE});

        // 指定需要代理的 method
        eh.setCallbackFilter(new CallbackFilter() {
            @Override
            public int accept(Method method) {
                return 0;
            }
        });*/

        Target t = (Target) eh.create();
        t.a();
        t.b();
    }
}
