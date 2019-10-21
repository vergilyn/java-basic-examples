package com.vergilyn.examples.netty;

import java.util.Timer;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @date 2019-10-21
 */
@Slf4j
public class TimerTaskTest {
    private static int index = 0;

    @Test
    public void nettyTimerTask() throws InterruptedException {
        io.netty.util.Timer timer = new HashedWheelTimer();

        io.netty.util.TimerTask timerTask = new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                log.info("...run...");

                // 可以理解成 递归，循环执行TimerTask
                timer.newTimeout(this, 0, TimeUnit.MILLISECONDS);
            }
        };

        // 第一次执行（只执行一次）
        timer.newTimeout(timerTask, 0, TimeUnit.MILLISECONDS);

        new Semaphore(0).acquire();  // 阻止方法退出
    }

    @Test(singleThreaded = true)
    public void jdkTimerTask() throws InterruptedException {
        java.util.Timer timer = new Timer("jdk-timer", true);

        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                log.info("...run...");
            }
        }, 0, 100);

        new Semaphore(0).acquire();  // 阻止方法退出
    }
}
