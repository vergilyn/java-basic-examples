package com.vergilyn.examples.thread.daemon;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * @author vergilyn
 * @since 2021-04-23
 */
public abstract class AbstractThreadTests {

    public void testTemplate(boolean daemon, long sleep) {
        System.out.printf("[%b]Thread create BEFORE \n", daemon);
        Thread thread = build(daemon, sleep);
        thread.start();
        System.out.printf("[%b]Thread create AFTER \n", daemon);
    }

    public Thread build(boolean daemon, long sleep){
        Thread thread = new Thread() {
            @Override
            public void run() {
                System.out.printf("[%s][%b]thread BEGIN \n", LocalTime.now(), isDaemon());
                safeSleep(sleep, TimeUnit.SECONDS);
                System.out.printf("[%s][%b]thread END \n", LocalTime.now(), isDaemon());
            }
        };
        thread.setDaemon(daemon);
        return thread;
    }

    public void safeSleep(long timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException e) {
        }
    }
}
