package com.vergilyn.examples.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 建议使用netty中的timer。
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/1/6
 */
@Slf4j
public class TimerTest {
    private Timer timer;
    private CountDownLatch latch;
    private Semaphore semaphore;
    private ScheduledExecutorService singleExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(10);

    @BeforeMethod
    public void before(){
        timer = new Timer();
        latch = new CountDownLatch(4);
        semaphore = new Semaphore(0);
    }

    @AfterMethod
    public void after(){
        try {
            System.out.println("await end...");
            latch.await();
            System.out.println("execute end...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void circle(){
        java.util.TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                log.info("timer task running...");
                latch.countDown();
            }
        };

        // 延迟1s开始执行, 之后每隔2s执行1次.
        timer.scheduleAtFixedRate(timerTask, 1000, 2000); // 暂时不理解fixed的什么
//        timer.schedule(timerTask, 1000, 2000);
    }

    @Test
    public void once(){
        java.util.TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                log.info("timer task running...");
                latch.countDown();
            }
        };

        timer.schedule(timerTask, 1000); // 延迟1s执行1次.
    }

    /**
     * schedule() 更注重间隔, 假设理论执行时间点是: 5、10、15、20, 由于某些原因第一次是在8s执行, 那么下一次会在13s执行.
     * @see <a href="http://blog.csdn.net/xieyuooo/article/details/8607220">Timer与TimerTask的真正原理&使用介绍</a>
     * @see <a href="https://www.jianshu.com/p/d732707ff194">Java定时任务调度详解</a>: 细看schedule() 与 scheduleAtFixedRate() 的区别.
     */
    @Test
    public void schedule(){
        latch = new CountDownLatch(0);
        java.util.TimerTask timerTask = createTimer(4, TimeUnit.SECONDS);

        log.info("schedule() begin...");
        timer.schedule(timerTask, 1000, 2000);

        try {
            semaphore.tryAcquire(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * scheduleAtFixedRate(): 更注重频率, 保证执行次数的稳定.
     */
    @Test
    public void scheduleAtFixedRate(){
        latch = new CountDownLatch(0);
        java.util.TimerTask timerTask = createTimer(4, TimeUnit.SECONDS);

        log.info("scheduleAtFixedRate() begin...");
        timer.scheduleAtFixedRate(timerTask, 1000, 2000);
        try {
            semaphore.tryAcquire(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private TimerTask createTimer(long timeout, TimeUnit timeUnit){
         return new TimerTask() {
            @Override
            public void run() {
                try {
                    log.info("timer task running... scheduledExecutionTime = {} ", this.scheduledExecutionTime());
                    semaphore.tryAcquire(timeout, timeUnit);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
