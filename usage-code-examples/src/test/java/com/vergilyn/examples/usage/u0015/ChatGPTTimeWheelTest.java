package com.vergilyn.examples.usage.u0015;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatGPTTimeWheelTest {

    /**
     * task是 <b>同步执行</b>。所以，如果某个task执行耗时过长，会导致之后的task延迟执行。
     *
     * <pre> 为什么时间轮执行任务时采用同步执行？chatGPT:
     *   时间轮执行任务时是同步的，是因为时间轮是一个单线程的调度器。
     *   时间轮中的任务都是按照添加的顺序依次执行的，每个任务执行完成后才会执行下一个任务。
     *   这样可以避免多个任务同时执行导致的线程安全问题，同时也可以避免任务之间的竞争和干扰。
     *
     *   另外，时间轮的主要作用是对定时任务进行调度和管理，而不是执行任务本身。
     *   因此，时间轮的执行效率并不是最重要的考虑因素。
     *   相反，时间轮的稳定性和可靠性更为重要，因此采用同步执行的方式可以保证时间轮的稳定性和可靠性。
     * </pre>
     */
    @SneakyThrows
    @Test
    void test(){
        TimeWheel timeWheel = new TimeWheel();
        timeWheel.start();

        timeWheel.addTask(buildTask("Task 1 executed", 1000), 1);
        timeWheel.addTask(buildTask("Task 2 executed", 2000), 2);
        timeWheel.addTask(buildTask("Task 3 executed", 3000), 3);

        TimeUnit.SECONDS.sleep(10);

        timeWheel.stop();
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

    /**
     * 时间轮由多个桶（Bucket）组成，每个桶包含一组定时任务（Task）。<br/>
     * 时间轮以固定的时间间隔（tickDuration）不断地轮转，每次轮转时，当前桶中的任务会被执行，并从桶中清除。<br/>
     * 当需要添加新的定时任务时，根据任务的延迟时间计算出需要添加到哪个桶中。<br/>
     */
    private static class TimeWheel {
        private static final int DEFAULT_TICKS_PER_WHEEL = 60;
        private static final int DEFAULT_TICK_DURATION = 1;

        private final int ticksPerWheel;
        private final int tickDuration;
        private final List<Bucket> buckets;
        private final ScheduledExecutorService executorService;
        private int currentTickIndex;

        public TimeWheel() {
            this(DEFAULT_TICKS_PER_WHEEL, DEFAULT_TICK_DURATION);
        }

        public TimeWheel(int ticksPerWheel, int tickDuration) {
            this.ticksPerWheel = ticksPerWheel;
            this.tickDuration = tickDuration;
            this.buckets = new ArrayList<>(ticksPerWheel);
            for (int i = 0; i < ticksPerWheel; i++) {
                buckets.add(new Bucket());
            }
            this.executorService = Executors.newSingleThreadScheduledExecutor();
            this.currentTickIndex = 0;
        }

        /**
         * 更推荐参考 {@link io.netty.util.HashedWheelTimer.Worker}，利用 {@link Queue#poll()} 实现此功能
         */
        public void start() {
            executorService.scheduleAtFixedRate(() -> {
                Bucket bucket = buckets.get(currentTickIndex);
                bucket.flush();
                currentTickIndex = (currentTickIndex + 1) % ticksPerWheel;
            }, tickDuration, tickDuration, TimeUnit.SECONDS);
        }

        public void stop() {
            executorService.shutdown();
        }

        public void addTask(BucketTask task, int delay) {
            int ticks = delay / tickDuration;
            int index = (currentTickIndex + ticks) % ticksPerWheel;
            Bucket bucket = buckets.get(index);
            bucket.addTask(task);
        }
    }

    private static class Bucket {
        private final List<BucketTask> tasks = new ArrayList<>();

        public void addTask(BucketTask task) {
            tasks.add(task);
        }

        public void flush() {
            tasks.forEach(BucketTask::run);
            tasks.clear();
        }
    }

    private static interface BucketTask {
        void run();
    }
}
