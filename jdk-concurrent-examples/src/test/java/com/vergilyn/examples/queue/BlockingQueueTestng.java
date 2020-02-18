package com.vergilyn.examples.queue;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * 在阅读seata源码看到，"io.seata.core.rpc.DefaultServerMessageListenerImpl.BatchLogRunnable"
 * @author vergilyn
 * @date 2020-02-18
 */
@Slf4j
public class BlockingQueueTestng {

    private BlockingQueue<String> strQueue = new LinkedBlockingDeque<>();

    @Test(description = "测试BlockingQueue#take()，看javadoc不难理解")
    public void take(){
        List<String> list = Lists.newArrayList();
        try {
            log.info("before >>>> list, {}", list);

            scheduledAddQueue();

            /* javadoc
             * Retrieves and removes the head of this queue, waiting if necessary
             * until an element becomes available.
             * (取回并删除此队列的头，如有必要，将等待直到有一个可用的元素)
             */
            list.add(strQueue.take());

            log.info("after >>>> list, {}", list);
        } catch (InterruptedException e) {
            log.info(e.getMessage(), e);
        }

        preventExit();
    }


    @Test(description = "测试BlockingQueue#drainTo()，看javadoc不难理解")
    public void drainTo(){
        List<String> list = Lists.newArrayList();
        int capacity = 10;
        initAddQueue(capacity);
        try {
            log.info("before >>>> queue.size = {}, list.size = {}, ", strQueue.size(), list.size());

            list.add(strQueue.take());
            strQueue.drainTo(list, 20);

            log.info("after >>>> queue.size = {}, list.size = {}, ", strQueue.size(), list.size());

            Assert.assertEquals(list.size(), 10);
        } catch (InterruptedException e) {
            log.info(e.getMessage(), e);
        }
    }

    private void initAddQueue(int capacity){
        int count = 0;
        LocalTime localTime = LocalTime.now();

        while (count < capacity){
            strQueue.add(localTime.plus(count, ChronoUnit.MILLIS).toString());

            count ++;
        }
    }

    private void scheduledAddQueue(){
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(() -> {
                    String str = LocalTime.now().toString();

                    if (log.isInfoEnabled()){
                        log.info("strQueue add: {}", str);
                    }

                    strQueue.add(str);
                }, 5, 5, TimeUnit.SECONDS);
    }

    private void preventExit(){
        try {
            new Semaphore(0).acquire();
        } catch (InterruptedException e) {
        }

    }

}
