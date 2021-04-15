package com.vergilyn.examples.jvm.allocate;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.Test;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * <ul>
 *
 *     <li><a href="https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html">【美团】深入解析String#intern</a></li>
 *     <li><a href="https://javadoop.com/post/string">Java 字符串常量池介绍</a></li>
 *     <li><a href="https://www.cnblogs.com/fangfuhai/p/5500065.html">Java字符串池（String Pool）深度解析</a></li>
 *     <li><a href="https://www.cnblogs.com/tongkey/p/8587060.html">Java中String字符串常量池</a></li>
 *     <li>Nacos-2.0.0 `StringPool.class`</li>
 * </ul>
 * <p>
 * GC回收：字符串池中维护了共享的字符串对象，这些字符串不会被垃圾收集器回收。
 *
 * <blockquote>
 *     <a href="http://lovestblog.cn/blog/2016/11/06/string-intern/">JVM源码分析之String.intern()导致的YGC不断变长</a>：
 *     1) YGC 会扫描 StringTable(即StringPool)，但不会清理。（所以StingPool太大会导致YGC变慢）
 *     2) Full GC或者CMS GC过程会对StringTable做清理
 * </blockquote>
 * <p>
 * JVM控制StringPool参数：`-XX:StringTableSize`
 *
 * @author vergilyn
 * @since 2021-04-13
 */
public class JvmStringPoolTestng {

	/**
	 * <blockquote>
	 * jdk1.7,1.8下字符串常量池已经转移到堆中了，是堆中的一部分内容，jvm设计人员对intern()进行了一些修改，
	 * 当执行`s3.intern()`时，<b>jvm不再把s3对应的字面量复制一份到字符串常量池中，而是在字符串常量池中存储一份s3的引用</b>，
	 * 这个引用指向堆中的字面量，当运行到String s4 = "hellohello"时，
	 * 发现字符串常量池已经存在一个指向堆中该字面量的引用，则返回这个引用，而这个引用就是s3。所以s3==s4输出true。
	 * </blockquote>
	 *
	 * @see <a href="https://www.cnblogs.com/tongkey/p/8587060.html">Java中String字符串常量池</a>
	 */
	@Test
	public void jvm() {
		String s1 = new String("1");
		String intern1 = s1.intern();
		String s2 = "1";
		System.out.println(s1 == s2);
		System.out.println(intern1 == s2);

		String s3 = new String("2") + new String("2");
		String intern3 = s3.intern();
		String s4 = "22";
		System.out.println(s3 == s4);
	}

	/**
	 * -XX: PrintStringTableStatistics
	 */
	@SneakyThrows
	@Test
	public void printStringTableStatistics(){
		try {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			Process process = Runtime.getRuntime().exec("java -XX:+PrintStringTableStatistics");
			System.out.println(IOUtils.toString(process.getInputStream(), UTF_8));
			System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
