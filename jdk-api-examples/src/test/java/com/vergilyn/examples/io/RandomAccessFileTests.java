package com.vergilyn.examples.io;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RandomAccessFileTests {

	private static final File FILE;
	static {
		String filepath = "";
		// `this.getClass().getResource(FILENAME).toURI()`得到的是 `/target/test-class/...` 中的文件。
		filepath += System.getProperty("user.dir");
		filepath += File.separator + "src" + File.separator + "test" + File.separator + "java";
		filepath += File.separator + RandomAccessFileTests.class.getPackage().getName().replace('.', File.separatorChar);
		filepath += File.separator + "random-access-file.txt";

		FILE = new File(filepath);
	}

	/**
	 * rwd: 只会在cache满，或者调用`RandomAccessFile.close()`的时候才会执行内容同步操作。<br/>
	 * rws: 在"rwd"的基础上对内容同步的要求更加严苛，每write修改一个byte都会直接修改到磁盘中。<br/>
	 *
	 * @see java.io.RandomAccessFile#RandomAccessFile(File, String)
	 */
	@Test
	public void accessMode(){
	}

	/**
	 * 同时 读写相同文件.
	 *
	 * @see <a href="https://www.jianshu.com/p/68c95fb1a225">JAVA 文件锁 FileLock</a>
	 * @see <a href="https://blog.csdn.net/j3t9z7h/article/details/104744021/">Java中如何锁文件</a>
	 */
	@SneakyThrows
	@ParameterizedTest
	@ValueSource(strings = {"1", "2"})
	public void syncRW(String mark){
		try(RandomAccessFile randomAccessFile = new RandomAccessFile(FILE, "rw")) {
			// lock之前，1/2 都可以调用
			System.out.println(mark + " >>>> before-lock length: " + randomAccessFile.length());

			// `1` 成功； `2` throw "java.nio.channels.OverlappingFileLockException"
			FileLock lock = randomAccessFile.getChannel().tryLock();

			System.out.println(mark + " >>>> after-lock length: " + randomAccessFile.length());
			System.out.println(mark + " >>>> content: " + new String(randomAccessFile.readLine().getBytes(StandardCharsets.UTF_8)));

			// 释放后，`2`可以正常执行
			// lock.release();
		}
	}
}
