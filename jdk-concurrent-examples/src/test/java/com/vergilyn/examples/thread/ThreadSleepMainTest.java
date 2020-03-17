package com.vergilyn.examples.thread;

/**
 * @author vergilyn
 * @date 2020-03-01
 */
public class ThreadSleepMainTest {


    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        // testSleep();

        mainThread.interrupt();
        System.out.println(mainThread.isInterrupted());
        System.out.println(mainThread.isInterrupted());
        System.out.println(mainThread.isInterrupted());
        System.out.println(mainThread.isInterrupted());
    }

    /**
     * 线程休眠500毫秒，主线程50毫秒打印一次状态 <br/>
     * ps：sleep方法的调用结果为状态：TIMED_WAITING  <br/>
     * <a href="https://mp.weixin.qq.com/s/XJJ7iDtIL7gGtoR_RpO69A">你能说出多线程中 sleep、yield、join 的用法及 sleep与wait区别吗？</a> <br/>
     */
    private static void testSleep(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // do nothing
            }
        });
        thread.start();

        while (true){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // do nothing
            }

            System.out.println("线程状态" + thread.getState());

            if (Thread.State.TERMINATED.equals(thread.getState())){
                System.out.println("线程状态" + thread.getState());
                break;
            }
        }
    }
}
