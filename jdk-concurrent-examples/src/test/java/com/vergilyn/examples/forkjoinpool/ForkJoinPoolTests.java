package com.vergilyn.examples.forkjoinpool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-09-09
 */
public class ForkJoinPoolTests extends AbstractForkJoinPoolTests{

	/**
	 * 利用 ForkJoin 进行 [start, end] 范围内的累加
	 *
	 * @see <a href="https://blog.csdn.net/tyrroo/article/details/81390202">Fork/Join框架基本使用</a>
	 */
	@SneakyThrows
	@Test
	public void helloworld(){
		int start = 1, end = 1000;
		int expected = IntStream.range(start, end + 1).sum();
		ForkJoinPool forkJoinPool = create();

		ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(new SummaryCounterForkJoinTask(start, end));

		Integer actual = forkJoinTask.get();

		System.out.printf("range [%d, %d], summary count >>>> expected: %d, actual: %d \n",
							start, end, expected, actual);

		Assertions.assertEquals(expected, actual);
	}

	static class SummaryCounterForkJoinTask extends RecursiveTask<Integer> {
		private static final int THRESHOLD_SECTION = 200;
		private final int start;
		private final int end;

		public SummaryCounterForkJoinTask(int start, int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		protected Integer compute() {
			printf("#compute(), start: %d, end: %d", start, end);

			// 区间内，进行累加
			if (end - start < THRESHOLD_SECTION){
				printf("sum(), start: %d, end: %d", start, end);
				return IntStream.range(start, end + 1).sum();
			}

			// 超过区间，拆分成更小的子任务
			int mid = (start + end) / 2;
			printf("split-task, start: %d, end: %d, mid: %d", start, end, mid);

			SummaryCounterForkJoinTask left = new SummaryCounterForkJoinTask(start, mid);
			left.fork();  // 执行任务

			SummaryCounterForkJoinTask right = new SummaryCounterForkJoinTask(mid + 1, end);
			right.fork();  // 执行任务

			/**
			 * {@link #invokeAll(ForkJoinTask[])}
			 */
			// invokeAll(left, right);

			// 等待任务执行完毕
			return left.join() + right.join();
		}
	}

	protected static void printf(String message, Object... args){
		System.out.printf(Thread.currentThread().getName() + " >>>> " + message, args).println();
	}
}
