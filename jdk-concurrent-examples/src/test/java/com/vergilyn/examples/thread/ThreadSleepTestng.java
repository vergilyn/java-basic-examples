package com.vergilyn.examples.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * <p>1. sleep 不会释放锁</p>
 *
 * <p>
 *   2. sleep 可以被中断（interrupt-exception），
 *   也就是说如果一个线程正在sleep，如果另外的线程将他中断（调用interrupt方法），将会抛出异常，并且中断状态将会擦除。
 * </p>
 * @author vergilyn
 * @date 2020-03-01
 */
@Slf4j
public class ThreadSleepTestng {
    public static final Long SLEEP_MS = 2000L;
    private final Object lock = new Object();

    @Test(description = "sleep 不会释放锁", threadPoolSize = 2, invocationCount = 2)
    public void sleepNotReleaseLock(){
        log.info("invoke sleepNotReleaseLock()...");
        synchronized (lock){
            String name = Thread.currentThread().getName();

            log.info("{} acquire lock, begin sleep: {} ms", name, SLEEP_MS);
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

    }

    @Test(description = "sleep 可以被中断", singleThreaded = true)
    public void sleepCanInterrupted() throws InterruptedException {

        Thread thread = new Thread(() -> {
            Thread currentThread = Thread.currentThread();
            String name = currentThread.getName();

            log.info("Thread[{}]  begin sleep[{} ms]", name, SLEEP_MS);
            try {
                Thread.sleep(SLEEP_MS);
            } catch (InterruptedException e) {
                log.error("Thread[{}] interrupted. message: {}", name, e.getMessage());
            }

            /* 本线程被Interrupted唤醒，捕获`InterruptedException`
             * 但是catch中并未“中断”，所以以下代码正常运行。
             */
            log.info("Thread[{}]  exec finish. ", name);

        });

        thread.start();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            log.info("scheduled invoke >>>> thread.interrupt()");
            thread.interrupt();  // Just to set the interrupt flag

        }, SLEEP_MS - 500, TimeUnit.MILLISECONDS);


        preventMethodExit();
    }

    private void preventMethodExit(){
        try {
            new Semaphore(0).acquire();
        } catch (InterruptedException e) {
            // do nothing
        }

    }
}
