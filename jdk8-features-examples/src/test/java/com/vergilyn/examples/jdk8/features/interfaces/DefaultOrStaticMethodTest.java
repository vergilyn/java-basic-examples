package com.vergilyn.examples.jdk8.features.interfaces;

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

        // 接口中的静态方法，只能通过Interface-Classname.staticMethod()调用
        InterfaceA.staticMethod();

        // ERROR: Static method may be invoked on containing interface class only.
        // instance.staticMethod();

        new DefaultOrStaticMethodTest().staticMethod(); // 规范是Class.staticMethod(), 但可以通过实例调用类的静态方法。

        // default-method属于对象方法，只能通过实例调用
        instance.defaultMethod();

        // 若super-class与interface包含相同签名的方法，可以不重写该方法，但调用的是super-class的方法
        instance.write();

        // 多个接口中存在相同签名的方法，实现类必须override此方法。
        instance.print();
    }

    private interface InterfaceA{
        static void staticMethod(){
            System.out.println("Interface-A staticMethod! \r\n");
        }

        default void defaultMethod(){
            System.out.println("Interface-A defaultMethod! \r\n");
        }

        default void print(){
            System.out.print("Interface-A print!\t");
        }

        default void write(){
            System.out.print("Interface-A write!\t");
        }
    }

    private interface InterfaceB{

        default void print(){
            System.out.print("Interface-B print!\t");
        }
    }

    private abstract class ParentA{
        public void write(){
            System.out.print("Parent-A write!\t");
        }
    }

    private class InterfaceImpl extends ParentA implements InterfaceA, InterfaceB{

        // InterfaceA、InterfaceB中存在相同签名的default-method，必须被重写
        @Override
        public void print() {
            System.out.print("InterfaceImpl override print() >>>> ");
            InterfaceA.super.print();
            InterfaceB.super.print();
        }
    }

    private static void staticMethod(){
        System.out.println("DefaultOrStaticMethodTest staticMethod! \r\n");
    }
}
