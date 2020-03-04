package com.vergilyn.examples.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author vergilyn
 * @date 2020-03-01
 */
public class TheadJoinMainTest {


    public static void main(String[] args) {
        threadJoin();
    }

    /**
     * <p><a href="https://mp.weixin.qq.com/s/XJJ7iDtIL7gGtoR_RpO69A">https://mp.weixin.qq.com/s/XJJ7iDtIL7gGtoR_RpO69A</a>
     *   <br/>从结果可以看到，主线程总是在线程执行之后，才会执行。
     *   也就是主线程在等待我们创建的这个线程结束，结束了之后才会继续进行
     * </p>
     * 如果调整下顺序---> start 与 join的先后顺序，再次看下情况，可以发现顺序没有保障了。
     */
    private static void threadJoin(){
        // 一个线程，循环5次，每次sleep 1s，主线程中打印信息
        Thread thread = new Thread(() -> {
            int num = 5;
            while ((num--) > 0){
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // do nothing
                }

                System.out.println(Thread.currentThread().getName() + ": working");
            }
        });

        thread.start();  // CODE-01

        try {
            thread.join();  // CODE-02
        } catch (InterruptedException e) {
            // do nothing
        }

        System.out.println("main end");
    }
}
