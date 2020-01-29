package com.vergilyn.examples;

/**
 * <a href="https://mp.weixin.qq.com/s/bIk15c23PDUjMmpZ0njgKQ">Java : 对象不再使用时，为什么要赋值为 null? </a>
 * <br/>
 * 文中提到了JVM关于`局部变量`的一个“bug”（JDK1.8依然存在）：<br/>
 *   如果`code-01 / code-02`被注释（或不存在），那么`placeHolder`并不会被GC。<br/>
 *   博客中的推断：“代码在离开if后，虽然已经离开了placeHolder的作用域，但在此之后，没有任何对运行时栈的读写，placeHolder所在的索引还没有被其他变量重用，所以GC判断其为存活。”<br/>
 * @author VergiLyn
 * @date 2020-01-19
 */
public class LocalVariableBugMainTest {

    public static void main(String[] args) {
        test();

        System.gc();  // 注释code-01、code-02 后，此处再次GC可以回收`placeHolder`
    }

    private static void test(){
        if (true) {
            byte[] placeHolder = new byte[64 * 1024 * 1024];
            System.out.println(placeHolder.length / 1024);
            // placeHolder = null;  // code-01
        }
        // int a = 1;  // code-02
        System.gc();
    }
}
