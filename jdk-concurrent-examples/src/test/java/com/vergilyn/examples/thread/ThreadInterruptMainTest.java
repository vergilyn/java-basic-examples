package com.vergilyn.examples.thread;

/**
 * <p>1. Thread中interrupted()方法和isInterrupted()方法区别 <br/>
 *   <a href="https://blog.csdn.net/zhuyong7/article/details/80852884">Thread中interrupted()方法和isInterrupted()方法区别总结</a>
 * </p>
 *
 * <p>
 *   2. 若`interrupt = true`，
 *   调用await()、join()、sleep()等方法时会抛出InterruptedException, 并清除标识。
 * </p>
 *
 * <p>
 *   3. catch InterruptedException后，若不throw/return，那么会继续执行后面的方法。
 *   （相当于用interrupt唤醒了当前线程）
 * </p>
 * @author vergilyn
 * @date 2020-03-01
 */
public class ThreadInterruptMainTest {

    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
           while (true){
               try {
                   System.out.println(Thread.currentThread().getName() + " -> state: "
                           + Thread.currentThread().getState());
                   Thread.sleep(500);
               } catch (InterruptedException e) {
                   // do nothing
               }
           }
        });
        thread.start();

        // Thread.interrupted(), 静态方法 <=> currentThread().isInterrupted(true)，操作的永远是 current-thread
        /* 以下结果`false -> true -> false`可以理解。
         * `after[2]`是因为调用`Thread.interrupted()`会reset-flag
         */
        System.out.println("invoke Thread.interrupt() before >>>> " + Thread.interrupted());  // false
        Thread.currentThread().interrupt();
        System.out.println("invoke Thread.interrupt() after[1]>>>> " + Thread.interrupted());  // true
        // System.out.println("invoke Thread.interrupt() after[2]>>>> " + Thread.interrupted());  // false

        System.out.println();

        // 实例方法 -> isInterrupted(false)
        System.out.println("invoke thread.interrupt() before >>>> " + thread.isInterrupted());  // false
        thread.interrupt();
        System.out.println("invoke thread.interrupt() after[1]>>>> " + thread.isInterrupted());  // true
        System.out.println("invoke thread.interrupt() after[2]>>>> " + thread.isInterrupted());  // true

    }

    /**
     * 标记为中断, 之后的代码一样会执行, 除非调用await()、join()、sleep()等方法会检测中断状态,抛出异常.
     * <pre>
     *    boolean bool = Thread.interrupted(); // 返回当前线程中断标识, 并清除标识;
     Thread.currentThread().interrupt();  // 标记线程: 中断
     boolean bool2 = Thread.currentThread().isInterrupted();// 返回当前线程中断标识; (不清除标识)
     * </pre>
     *
     * @see <a href="https://www.cnblogs.com/mmm950410/p/6121217.html">isInterrupted()方法和Thread.interrupted()方法判断中断状态的区别</a>
     * @see <a href="http://blog.csdn.net/qq_26562641/article/details/51698481">Thread.interrupted()方法的陷阱</a>
     * @see <a href="http://blog.csdn.net/gtuu0123/article/details/6040105">java线程中的interrupt,isInterrupt,interrupted方法</a>
     */
    public void test2(){

        System.out.println(Thread.interrupted());  // -> false

        Thread.currentThread().interrupt();  // 标记中断, 之后代码依旧执行

        System.out.println(Thread.interrupted());  // -> true, 并清除标识

        System.out.println(Thread.interrupted());  // -> false, 因为标识被清除

        Thread.currentThread().interrupt();

        System.out.println(Thread.currentThread().isInterrupted());  // -> true
        System.out.println(Thread.currentThread().isInterrupted());  // -> true
        System.out.println(Thread.currentThread().isInterrupted());  // -> true, 代码依旧执行

        try {
            Thread.sleep(10 * 1000);  // isInterrupted = true, 调用await()、join()、sleep()等方法时会抛出InterruptedException, 并清除标识
        } catch (InterruptedException e) {
            System.out.println("sleep()... interrupted = " + Thread.currentThread().isInterrupted());  // -> false, 因为被sleep()清除了标识.
        }

    }
}
