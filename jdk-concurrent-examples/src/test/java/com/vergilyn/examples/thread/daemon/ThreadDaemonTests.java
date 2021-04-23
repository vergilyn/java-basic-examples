package com.vergilyn.examples.thread.daemon;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * <ol>
 *   <li><a href="https://www.cnblogs.com/xiarongjin/p/8310144.html">守护线程(Daemon Thread)</a></li>
 * </ol>
 *
 * <pre>
 *   在Java中有两类线程：用户线程 (User Thread)、守护线程 (Daemon Thread)。
 *   所谓"守护线程"，是指在程序运行的时候在后台提供一种通用服务的线程，
 *   比如垃圾回收线程就是一个很称职的守护者，并且这种线程并不属于程序中不可或缺的部分。
 *   因此，当所有的非守护线程结束时，程序也就终止了，同时会杀死进程中的所有守护线程。
 *   反过来说，只要任何非守护线程还在运行，程序就不会终止。
 *
 *   用户线程和守护线程两者几乎没有区别，唯一的不同之处就在于虚拟机的离开：
 *   如果用户线程已经全部退出运行了，只剩下守护线程存在了，虚拟机也就退出了。
 *   因为没有了被守护者，守护线程也就没有工作可做了，也就没有继续运行程序的必要了。
 * </pre>
 *
 * @author vergilyn
 * @since 2021-04-21
 */
public class ThreadDaemonTests {


	@SneakyThrows
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void daemon(boolean daemon){
		ExecutorService pool = Executors.newFixedThreadPool(3, r -> {
			Thread thread = new Thread(r);
			thread.setName("thread-daemon-" + daemon);
			thread.setDaemon(daemon);
			return thread;
		});

		AtomicInteger index = new AtomicInteger(0);
		for (int i = 0; i < 3; i++) {
			pool.submit(() -> {
				Thread currentThread = Thread.currentThread();
				try {
					int number = index.incrementAndGet();
					TimeUnit.SECONDS.sleep(number * 2L);

					System.out.printf("[vergilyn] >>>> index:%d, name: %s, daemon: %b\n",
							number, currentThread.getName(), currentThread.isDaemon());
				} catch (InterruptedException e) {
				}
			});
		}

		//TimeUnit.SECONDS.sleep(10);
	}
}
