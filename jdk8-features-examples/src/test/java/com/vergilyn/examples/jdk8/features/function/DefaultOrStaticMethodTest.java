package com.vergilyn.examples.jdk8.features.function;

import org.testng.annotations.Test;

/**
 * <a href="https://www.cnblogs.com/zhoading/p/8440633.html">JDK8新特性：接口的静态方法和默认方法</a>
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/19
 */
public class DefaultOrStaticMethodTest {

    @Test
    public void test(){
        InterfaceImpl instance = new InterfaceImpl();

        InterfaceA.staticMethod();
        instance.defaultMethod();    // default-method属于对象方法，只能通过实例调用
        // new InterfaceImpl().staticMethod(); // ERROR:
        new DefaultOrStaticMethodTest().staticMethod();

        instance.print();
    }

    private interface InterfaceA{
        static void staticMethod(){
            System.out.println("Interface-A staticMethod!");
        }

        default void defaultMethod(){
            System.out.println("Interface-A defaultMethod!");
        }

        default void print(){
            System.out.print("Interface-A print!\t");
        }
    }

    private interface InterfaceB{

        default void print(){
            System.out.print("Interface-B print!\t");
        }
    }


    private class InterfaceImpl implements InterfaceA, InterfaceB{

        // InterfaceA、InterfaceB中存在相同签名的default-method，必须被重写
        @Override
        public void print() {
            System.out.print("InterfaceImpl print >>>> ");
            InterfaceA.super.print();
            InterfaceB.super.print();
        }
    }

    private static void staticMethod(){
        System.out.println("DefaultOrStaticMethodTest staticMethod!");
    }
}
