package com.vergilyn.examples;

import java.util.Map;

import org.testng.annotations.Test;
import org.testng.collections.Maps;

/**
 * try-catch-finally的return执行顺序。<br/>
 * <a href="https://mp.weixin.qq.com/s/TFmFL9aFht71bz8cxjgw3g">Java finally语句到底是在return之前还是之后执行？</a>
 * <p>
 * 1. finally语句在return语句执行之后return返回之前执行的；<br/>
 * 2. finally块中的return语句会覆盖try块中的return返回；<br/>
 * 3. 如果finally语句中没有return语句覆盖返回值，那么原来的返回值可能因为finally里的修改而改变也可能不变；<br/>
 * 4. try块里的return语句在异常的情况下不会被执行，这样具体返回哪个看情况；<br/>
 * 5. 当发生异常后，catch中的return执行情况与未发生异常时try中return的执行情况完全一样；<br/>
 * 6. try、catch、finally中均有return，并且发生异常，最后执行的return会覆盖之前的 <br/>
 * </p>
 * 总结：简单粗暴一句话，如果多个return被执行，那么后执行的return覆盖之前的（即最后的return是最后执行的return）
 * @author vergilyn
 * @date 2020-02-04
 */
public class TryCatchFinallyReturnTestng {

    /**
     * 通过{@linkplain #test11()}、{@linkplain #test12()}验证：finally语句在return语句执行之后return返回之前执行的
     */
    @Test
    public void test10() {
        System.out.println(test11());
        /* 输出结果：
            try block
            finally block
            b>25, b = 100
            100
         */

        System.out.println("=====================");

        System.out.println(test12());
        /* 输出结果：
            try block
            return statement
            finally block
            after return
         */
    }

    /**
     * finally语句在return语句执行之后return返回之前执行的
     */
    private int test11() {
        int b = 20;

        try {
            System.out.println("try block");
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
        }

        return b;
    }

    /**
     * 加强验证{@linkplain #test11()}
     */
    private String test12() {
        try {
            System.out.println("try block");

            return test13();
        } finally {
            System.out.println("finally block");
        }
    }

    private String test13() {
        System.out.println("return statement");

        return "after return";
    }

    /**
     * 通过{@linkplain #test21()}验证：finally块中的return语句会覆盖try块中的return返回
     */
    @Test
    public void test20() {
        System.out.println(test21());
        /* 输出结果：
            try block
            finally block
            b>25, b = 100
            200
         */

    }

    private int test21() {
        int b = 20;

        try {
            System.out.println("try block");
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }

            return 200;  // IDEA warning: 'return' inside 'finally' block
        }

        // return b;  // 因为finally存在return，导致此代码不可达，编译器报错，所以需要注释。
    }

    /**
     * 通过{@linkplain #test31()}、{@lin}验证： 如果finally语句中没有return语句覆盖返回值，那么原来的返回值可能因为finally里的修改而改变也可能不变 <br/>
     * 原因：<br/>
     * 简单来说就是：Java中只有传值没有传址，所以方法中的`b=150`、`map=null`对外不可见。
     */
    @Test
    public void test30() {
        System.out.println(test31()); // 不改变的情况
        /* 输出结果：
            try block
            finally block
            b>25, b = 100
            100
         */

        System.out.println("===============");

        System.out.println(test32());  // 改变的情况
        /* 输出结果：
            {KEY=FINALLY}
         */
    }

    private int test31() {
        int b = 20;

        try {
            System.out.println("try block");
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
            b = 150;
        }

        return 2000;
    }

    private Map<String, String> test32() {
        Map<String, String> map = Maps.newHashMap();
        map.put("KEY", "INIT");

        try {
            map.put("KEY", "TRY");
            return map;
        } catch (Exception e) {
            map.put("KEY", "CATCH");
        } finally {
            map.put("KEY", "FINALLY");
            map = null;
        }

        return map;
    }

    /**
     * 通过{@linkplain #test41()}验证：try块里的return语句在异常的情况下不会被执行，最后具体返回哪个看情况 <br/>
     */
    @Test
    public void test40() {
        System.out.println(test41());
        /* 输出结果：
            try block
            catch block
            finally block
            b>25, b = 35
            10086
         */
    }

    private int test41() {
        int b = 20;
        try {
            System.out.println("try block");
            b = b / 0;
            return b += 80;
        } catch (Exception e) {
            b += 15;
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
            b += 50;
        }

        return 10086;
    }


    /**
     * 通过{@linkplain #test51()}验证：当发生异常后，catch中的return执行情况与未发生异常时try中return的执行情况完全一样 <br/>
     */
    @Test
    public void test50() {
        System.out.println(test51());
        /* 输出结果：
            try block
            catch block
            finally block
            b>25, b = 35
            35
         */
    }

    private int test51() {
        int b = 20;

        try {
            System.out.println("try block");
            b = b /0;
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
            return b += 15;
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
            b += 50;
        }

        //return b;
    }

    /**
     * 通过{@linkplain #test61()}验证：try、catch、finally中都是return，并且发生异常 <br/>
     */
    @Test
    public void test60() {
        System.out.println(test61());
        /* 输出结果：
            try block
            catch block
            finally block
            b>25, b = 35
            105
         */
    }

    private int test61() {
        int b = 20;

        try {
            System.out.println("try block");
            b = b /0;
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
            return b += 15;
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
            b += 50;
            return b += 20;
        }

    }

}
