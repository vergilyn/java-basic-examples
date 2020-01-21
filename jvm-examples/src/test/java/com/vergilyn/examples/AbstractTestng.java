package com.vergilyn.examples;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * @author VergiLyn
 * @date 2020-01-20
 */
@Slf4j
public abstract class AbstractTestng {
    protected int sleepCount = 0;

    protected void sleep(int minutes){
        sleep(minutes, "");
    }

    protected void sleep(int minutes, String desc)  {
        try {
            log.info("Thead Sleep[{}]: {} min, sleep begin >>>> {}", sleepCount++, minutes, desc);
            Thread.sleep(TimeUnit.MINUTES.toMillis(minutes));
            log.info("Thead Sleep[{}]: {} min, sleep end >>>> {}", sleepCount, minutes, desc);
        } catch (InterruptedException e) {
            log.error("Thread Sleep failure", e);
        }
    }

    @BeforeMethod
    protected void beforeMethod(){
        sleepCount = 0;

        sleep(1, "beforeMethod()");  // Thread.sleep()，为了方便观察VisualVM、JProfiler
    }

    @AfterMethod
    protected void afterMethod() {
        log.info("afterMethod() >>>> begin System.gc()");
        System.gc();
        sleep(1, "afterMethod() invoke `System.gc()` finish.");

        sleep(2, "await exit test-method()");
    }
}
