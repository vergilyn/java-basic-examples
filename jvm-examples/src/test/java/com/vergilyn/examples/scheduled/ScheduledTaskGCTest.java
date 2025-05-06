package com.vergilyn.examples.scheduled;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

class ScheduledTaskGCTest {

    @SneakyThrows
    @Test
    void test_scheduledTaskGC(){
        mockInvoke(1);

        TimeUnit.SECONDS.sleep(30);

        System.gc();
        System.out.println("》》》》》》》》》 after invoke gc 《《《《《《《《《《");

        TimeUnit.MINUTES.sleep(3);
    }

    private void mockInvoke(Integer index) {
        RocketMQApiClient client = new RocketMQApiClient(index);
        client.createTopic("topic-" + index);

        // return client;
    }
}
