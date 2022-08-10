package com.vergilyn.examples.thread;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class RunableRunMethodTests {

	/**
	 * 直接调用{@link Runnable#run()}也是 current-thread 执行！
	 *
	 * <pre>
	 * 调用start()方法，<b>会启动新的线程</b>，并且会让一个线程进入就绪状态，在调度系统分配时间片后，
	 * start()会进行相应的准备工作，然后执行run()方法内的内容。
	 *
	 * 而如果直接调用run()方法，只是在主线程运行run()方法，无法起到多线程的效果。
	 * </pre>
	 */
	@Test
	public void runnable(){
		Runnable runnable = new Runnable(){
			@SneakyThrows
			@Override
			public void run() {
				TimeUnit.SECONDS.sleep(2);
				System.out.println("Runnable#run(): " + Thread.currentThread().getName());
			}
		};

		runnable.run();
	}

}
