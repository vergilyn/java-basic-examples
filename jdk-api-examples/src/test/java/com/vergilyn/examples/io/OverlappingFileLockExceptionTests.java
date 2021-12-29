package com.vergilyn.examples.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * 复现 dubbo-3.0.4 的一个bug “Failed to save registry cache file after retrying 3 times....”
 * （jdk11 抛出的，jdk8 也会）
 *
 * 2021-12-01 >>>>
 * 造成原因，dubbo-3.0.4确实是多线程调用 tryLock，
 * 但是 dubbo-2.7.6 是单线程调用。参考`dubbo.AbstractRegistry#registryCacheExecutor`
 *
 * @author vergilyn
 * @since 2021-12-01
 */
@SuppressWarnings("JavadocReference")
public class OverlappingFileLockExceptionTests {

	/**
	 * 2021-12-01，网上说的大概就是就是 多线程 同时去获取file-lock
	 *
	 * @see OverlappingFileLockException
	 */
	@SneakyThrows
	@Test
	public void test(){
		ExecutorService pool = Executors.newFixedThreadPool(4);

		pool.submit(new DubboAbstractRegistry());
		pool.submit(new DubboAbstractRegistry());

		TimeUnit.SECONDS.sleep(10);
	}


	@Slf4j
	private static class DubboAbstractRegistry implements Runnable {
		private String absolutePath = "D:/dubbo-cache.lock";

		/**
		 * @see org.apache.dubbo.registry.support.AbstractRegistry#doSaveProperties
		 */
		@Override
		public void run() {
			String threadName = Thread.currentThread().getName();

			File lockfile = new File(absolutePath);

			try {
				if (!lockfile.exists()) {
					lockfile.createNewFile();
				}

				try (RandomAccessFile raf = new RandomAccessFile(lockfile, "rw");
						FileChannel channel = raf.getChannel()) {

					// 这个会抛出"OverlappingFileLockException"
					FileLock lock = channel.tryLock();
					if (lock == null) {
						throw new IOException("Can not lock the registry cache file " + absolutePath
								                      + ", ignore and retry later, maybe multi java process use the file, please config: dubbo.registry.file=xxx.properties");
					}

					// 持锁一段时间
					TimeUnit.SECONDS.sleep(1);

					System.out.printf("[vergilyn] >>>> %s finish.\n", threadName);
				}
			}catch (Throwable t){
				log.error("{}", threadName, t);
			}

		}
	}

}
