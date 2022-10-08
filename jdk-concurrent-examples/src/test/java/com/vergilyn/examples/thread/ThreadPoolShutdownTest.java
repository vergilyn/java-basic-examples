package com.vergilyn.examples.thread;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolShutdownTest {
	final ExecutorService _executor = Executors.newFixedThreadPool(5);

	/**
	 * <p> shutdown => 平缓关闭，等待所有已添加到线程池中的任务执行完在关闭。
	 * <p> shutdownNow => 立刻关闭，停止正在执行的任务（发送 interrupted信号），并返回队列中未执行的任务。
	 *
	 * @see ThreadInterruptMainTest
	 */
	@SneakyThrows
	@Test
	public void shutdownNow(){

		_executor.submit(new Runnable() {
			@Override
			public void run() {
				printf("thread#run while() BEGIN.\n");

				while (true){
					if (Thread.currentThread().isInterrupted()){
						printf("thread#run while() Interrupted.\n");
						break;
					}
				}

				printf("thread#run while() END.\n");
			}
		});

		TimeUnit.MILLISECONDS.sleep(3000);
		_executor.shutdownNow();

	}

	private void printf(String format, Object... args){
		String prefix = String.format("[%s][thread-%s]", LocalDateTime.now(), Thread.currentThread().getName());

		System.out.printf(prefix + format, args);
	}
}
