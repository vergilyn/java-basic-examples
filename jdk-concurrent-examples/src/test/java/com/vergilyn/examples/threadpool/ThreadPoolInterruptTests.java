package com.vergilyn.examples.threadpool;

import cn.hutool.core.thread.NamedThreadFactory;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>期望</h3>
 * 终止or取消 线程池中 指定的任务。
 *
 * <p> 2022-07-25，更推荐 <b>Future</b>的方式，简单易懂。
 *
 * @author vergilyn
 * @since 2022-07-25
 */
public class ThreadPoolInterruptTests {
	private static final AtomicInteger INDEX = new AtomicInteger(0);

	/**
	 * 代码相对简单易理解，只需要维护{@code `Map<String, Future<?>> taskManager`}。
	 *
	 * <p> 1. 正在执行的任务，调用{@link Future#cancel(boolean) `Fututure.cancel(true)`} 会打印{@link InterruptedException}
	 * <p> 2. 正在执行的任务，调用{@link Future#cancel(boolean) `Fututure.cancel(false)`} 会允许正在执行的任务完成。
	 * <p> 3. 等待执行的任务，不管是 true/false 都 <b>不会打印</b> {@link InterruptedException}。（可以终止任务的执行）
	 *
	 * @see Future#cancel(boolean)
	 */
	@SneakyThrows
	@ParameterizedTest
	@CsvSource({"2,4"})
	public void futureCancel(int threads, int taskNums){
		ExecutorService threadPool = Executors.newFixedThreadPool(threads, new NamedThreadFactory("vergilyn_thread_", false));

		// 包装一下，维护一个 map就可以了。
		Map<String, Future<?>> taskManager = Maps.newConcurrentMap();
		for (int i = 0; i < taskNums; i++) {
			int index = INDEX.getAndIncrement();

			Future<?> future = threadPool.submit(() -> {
				try {
					print(index, "sleep 2s begin");
					TimeUnit.SECONDS.sleep(2);
					print(index, "sleep 2s end");

				} catch (InterruptedException ie) {
					print(index, "InterruptedException, " + ie.getMessage());
				} catch (Exception e) {
					print(index, "Exception, " + e.getMessage());
				}
			});

			taskManager.put("task-" + index, future);
		}

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				// true: 正在执行的任务会强制终止，打印`InterruptedException`
				Future<?> future0 = taskManager.get("task-0");
				future0.cancel(true);

				// 等待执行的任务，不会打印`InterruptedException`.
				Future<?> future2 = taskManager.get("task-2");
				future2.cancel(false);
			}
		}, 1000);

		preventJunitExit(60);
	}

	/**
	 * 实现{@link ThreadFactory} 或者 扩展{@link ExecutorService}，维护 new-thread 的映射关系（例如 {@code `Map<String, Thread> threadManage`}），
	 * 从而获得期望的thread，然后调用{@link Thread#interrupt()}。
	 *
	 * <p> 参考：<a href="https://www.cnblogs.com/exmyth/p/16148521.html">终止线程池对应某个线程</a>
	 *
	 * <p> 个人：这种方式感觉相对麻烦，不如future方式来的简单易懂。
	 *
	 */
	@SneakyThrows
	@ParameterizedTest
	@CsvSource({"2,4"})
	public void threadFactory(int threads, int taskNums){
		ExecutorService threadPool = Executors.newFixedThreadPool(threads, new NamedThreadFactory("vergilyn_thread_", false){

		});

	}

	@SneakyThrows
	private void awaitTaskFinish(List<Future<?>> futures){
		for (Future<?> future : futures) {
			future.get();
		}
	}

	@SneakyThrows
	private void preventJunitExit(long sleepMs){
		TimeUnit.SECONDS.sleep(sleepMs);
	}

	private void print(Integer index, String msg){
		Thread currentThread = Thread.currentThread();
		String threadName = currentThread.getName();

		System.out.printf("[%d][%s]%s >>>> %s\n", index, LocalTime.now(), threadName, msg);
	}
}
