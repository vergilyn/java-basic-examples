package com.vergilyn.examples.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.testng.annotations.Test;

/**
 * <a href="https://mp.weixin.qq.com/s/1BaQA2cfwVdiL7fy8ROmxw">线程池：治理线程的法宝</a>
 * @author vergilyn
 * @date 2020-02-04
 */
public class ThreadPoolTestng {

    @Test
    public void test(){
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
    }
}
