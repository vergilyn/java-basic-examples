package com.vergilyn.examples.nacos.event;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * @author vergilyn
 * @since 2022-07-29
 */
public abstract class AbstractNacosEventTests {

    protected void sleepSafe(long millis){
        if (millis <= 0){
            return;
        }

        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        }catch (Exception ignored){

        }
    }

    protected void println(String msg){
        System.out.println(logPrefix() + msg);
    }

    protected void printf(String format, Object... args){
        System.out.printf(logPrefix() + format, args);
    }

    protected String logPrefix(){
        return String.format("[vergilyn][%s][%s] >>>> ", LocalTime.now(), Thread.currentThread().getName());
    }
}
