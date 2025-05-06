package com.vergilyn.examples.scheduled;

import cn.hutool.core.thread.NamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class RocketMQApiClient implements Runnable {

    private final AtomicInteger counter = new AtomicInteger();
    private final int shutdownCount;

    private final ScheduledExecutorService scheduledExecutorService;

    public RocketMQApiClient(int index) {
        this(index, Integer.MAX_VALUE);
    }

    public RocketMQApiClient(int index, int shutdownCount) {

        this.shutdownCount = shutdownCount;
        this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("rocketmq-api-client-" + index, true));

        scheduledExecutorService.scheduleAtFixedRate(this, 1, 3, TimeUnit.SECONDS);
    }

    public boolean createTopic(String topic) {
        println("#createTopic# topic: %s, count: %d", topic, counter.get());
        return true;
    }

    // @Override
    public void run() {
        println("#run# count: %d\n", counter.incrementAndGet());

        if (counter.get() > shutdownCount) {
            scheduledExecutorService.shutdown();
        }
    }

    private void println(String format, Object... args) {

        String prefix = String.format("[%s] >>>> ", Thread.currentThread().getName());

        System.out.printf(prefix + format, args).println();

    }
}
