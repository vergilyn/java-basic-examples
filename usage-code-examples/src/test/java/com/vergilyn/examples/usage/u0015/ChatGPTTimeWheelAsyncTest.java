package com.vergilyn.examples.usage.u0015;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatGPTTimeWheelAsyncTest {

    /**
     * {@link ChatGPTTimeWheelTest} 中，task 是 <b>同步执行</b> 的，这会导致task
     */
    @Test
    @SneakyThrows
    void testAsyncTimeWheel() {
        AsyncTimeWheel asyncTimeWheel = new AsyncTimeWheel();
        asyncTimeWheel.start();

        asyncTimeWheel.addTask(buildTask("Task 1 executed", 1000), 1);
        asyncTimeWheel.addTask(buildTask("Task 2 executed", 2000), 2);
        asyncTimeWheel.addTask(buildTask("Task 3 executed", 3000), 3);

        TimeUnit.SECONDS.sleep(10);

        asyncTimeWheel.stop();
    }

    private BucketTask buildTask(String msg, long sleepMs){
        return new BucketTask() {
            @Override
            public void run() {
                System.out.printf("[%s] %s. sleepMs: %d ms\n", LocalTime.now(), msg, sleepMs);
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepMs);
                } catch (InterruptedException ignored) {
                }
            }
        };
    }

    private static class AsyncTimeWheel {

        private static final int DEFAULT_TICKS_PER_WHEEL = 60;
        private static final int DEFAULT_TICK_DURATION = 1;

        private final int ticksPerWheel;
        private final int tickDuration;
        private final List<Bucket> buckets;
        private final ScheduledExecutorService executorService;
        private final ExecutorService taskExecutor;
        private int currentTickIndex;

        public AsyncTimeWheel() {
            this(DEFAULT_TICKS_PER_WHEEL, DEFAULT_TICK_DURATION);
        }

        public AsyncTimeWheel(int ticksPerWheel, int tickDuration) {
            this.ticksPerWheel = ticksPerWheel;
            this.tickDuration = tickDuration;

            this.executorService = Executors.newSingleThreadScheduledExecutor();
            this.taskExecutor = Executors.newFixedThreadPool(10);
            this.currentTickIndex = 0;

            this.buckets = new ArrayList<>(ticksPerWheel);
            for (int i = 0; i < ticksPerWheel; i++) {
                buckets.add(new Bucket(this.taskExecutor));
            }
        }

        public void start() {
            executorService.scheduleAtFixedRate(() -> {
                Bucket bucket = buckets.get(currentTickIndex);
                bucket.flush();
                currentTickIndex = (currentTickIndex + 1) % ticksPerWheel;
            }, tickDuration, tickDuration, TimeUnit.SECONDS);
        }

        public void stop() {
            executorService.shutdown();
            taskExecutor.shutdown();
        }

        public void addTask(BucketTask task, int delay) {
            int ticks = delay / tickDuration;
            int index = (currentTickIndex + ticks) % ticksPerWheel;
            Bucket bucket = buckets.get(index);
            bucket.addTask(task);
        }

    }

    private static class Bucket {
        private final ExecutorService taskExecutor;
        private final List<BucketTask> tasks;

        public Bucket(ExecutorService taskExecutor) {
            this.taskExecutor = taskExecutor;
            this.tasks = new ArrayList<>();
        }

        public void addTask(BucketTask task) {
            tasks.add(task);
        }

        public void flush() {
            tasks.forEach(task -> taskExecutor.submit(task::run));
            tasks.clear();
        }
    }

    private static interface BucketTask {
        void run();
    }


}
