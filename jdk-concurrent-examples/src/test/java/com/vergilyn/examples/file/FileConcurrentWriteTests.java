package com.vergilyn.examples.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

public class FileConcurrentWriteTests {
	private static final File FILE;
	static {
		String filepath = "";
		// `this.getClass().getResource(FILENAME).toURI()`得到的是 `/target/test-class/...` 中的文件。
		filepath += System.getProperty("user.dir");
		filepath += File.separator + "src" + File.separator + "test" + File.separator + "java";
		filepath += File.separator + FileConcurrentWriteTests.class.getPackage().getName().replace('.', File.separatorChar);
		filepath += File.separator + "file-concurrent.txt";

		FILE = new File(filepath);

		// 清空文件内容
		try (final RandomAccessFile randomAccessFile = new RandomAccessFile(FILE, "rws")){
			randomAccessFile.setLength(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 多线程/多进程 同时对一个文件进行读写。例如，同时向文件（最后开始）追加字符串。
	 *
	 * <br/>
	 * FIXME 有并发问题，会<b>覆盖</b>写（因为 seek时 length并发读取错误。）
	 *   根据实际场景再看怎么解决（1. redission 2. lock 之类的。或者功能上规避并发写）
	 *
	 * @see java.nio.channels.FileLock `randomAccessFile.getChannel().tryLock()`
	 */
	@SneakyThrows
	@Test
	public void test(){
		ExecutorService threadPool = Executors.newFixedThreadPool(2);

		AtomicInteger index = new AtomicInteger(0);
		for (int i = 0; i < 50; i++) {
			// e.g. `[0]14:49:33.330,3587`
			threadPool.submit(() -> write("[" + index.getAndIncrement() + "]"
					                              + LocalTime.now()
					                              + "," + RandomUtils.nextInt(0, 20)));
		}

		TimeUnit.SECONDS.sleep(10);
	}

	@SneakyThrows
	protected void write(String append){
		// rw代表读取和写入，s代表了同步方式，也就是同步锁。这种方式打开的文件，就是独占方式的。
		try(RandomAccessFile randomAccessFile = new RandomAccessFile(FILE, "rws")){
			randomAccessFile.seek(randomAccessFile.length());
			randomAccessFile.write(append.getBytes(StandardCharsets.UTF_8));
			randomAccessFile.write("\n".getBytes(StandardCharsets.UTF_8));
		}
	}
}
