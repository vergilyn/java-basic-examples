package com.vergilyn.examples.concurrent;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.alibaba.fastjson.JSON;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/2/7
 */
public class ConcurrentLinkedQueueTest {

    private final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

    @BeforeMethod
    public void before(){
        queue.add("1");
        queue.add("2");
        queue.add("3");
        queue.add("4");
    }

    @Test
    public void test(){
        String poll = queue.poll();
        System.out.println("ConcurrentLinkedQueue.poll(): " + poll + ", current: " + JSON.toJSONString(queue));
    }

}
