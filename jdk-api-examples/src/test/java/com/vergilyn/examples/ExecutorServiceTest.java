package com.vergilyn.examples;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @date 2019/2/15
 */
public class ExecutorServiceTest {

    private ExecutorService cachedThreadPool;
    private ExecutorService fixedThreadPool;
    private ExecutorService scheduledThreadPool;
    private ExecutorService singleThreadExecutor;

    @BeforeTest
    public void before(){
        // 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
        cachedThreadPool = Executors.newCachedThreadPool();

        //newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
        fixedThreadPool = Executors.newFixedThreadPool(4);

        //newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
        scheduledThreadPool = Executors.newScheduledThreadPool(4);

        //newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }

    /**
     *  fixedThreadPool = Executors.newFixedThreadPool(4);
     *  等价于
     *  <pre>
     *      new ThreadPoolExecutor(nThreads, nThreads,
     *                                       0L, TimeUnit.MILLISECONDS,
     *                                       new LinkedBlockingQueue<Runnable>());
     *  </pre>
     *  因为LinkedBlockingQueue是链表阻塞队列，符合FIFO。
     * @throws Throwable
     */
    @Test
    public void future() throws Throwable {
        List<PartResult> taskResults = Lists.newArrayList();
        List<Future<PartResult>> futures = Lists.newArrayList();

        for (int i = 0; i < 20; i++){
            Task task = new Task(i, "task - " + i, i);
            futures.add(fixedThreadPool.submit(task));
        }

        // 已提交的任务会继续执行，但不会接受新任务
        // 如果已经关闭，则没有其他效果。
        fixedThreadPool.shutdown();

        // 等待所有任务执行完
        fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        for (Future<PartResult> future : futures) {
            try {
                PartResult tr = future.get();
                taskResults.add(tr);
            } catch (ExecutionException e) {
                throw e.getCause();
            }
        }

        taskResults.sort(Comparator.comparingInt(PartResult::getIndex));

        taskResults.forEach(System.out::println);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class PartResult{
        private int index;
        private boolean isSuccess;
        private long executeTime;
        private Exception exception;
    }

    @Data
    @AllArgsConstructor
    static class Task implements Callable<PartResult> {
        private int id;
        private String name;
        private int partIndex;

        @Override
        public PartResult call() throws Exception {

            boolean isSuccess = RandomUtils.nextBoolean();
            long millis = RandomUtils.nextLong(1, 10) * 1000;
            Thread.sleep(millis);

            System.out.printf("ThreadId[%s] >>>> id: %d, name: %s, partIndex: %d, millis: %s \r\n", Thread.currentThread().getId(), id, name, partIndex, millis);
            return new PartResult(partIndex, isSuccess, millis, null);
        }
    }
}
