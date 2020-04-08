package com.vergilyn.examples.reflect;

/**
 * 包含: <br/>
 * - 一般内部类 {@linkplain NormalInnerClass} <br/>
 * - 静态内部类 {@linkplain StaticInnerClass} <br/>
 * @author vergilyn
 * @date 2020-04-08
 */
public class OuterClass {
    int i = 1;
    public void print(){
        System.out.println("outer-class");
    }

    public static class StaticInnerClass {
        int j = 2;

        public void print(){
            System.out.println("static-inner-class");
        }
    }

    public class NormalInnerClass {
        int k =3;

        public NormalInnerClass() {
        }

        public void print(){
            System.out.println("normal-inner-class");
        }
    }
}
