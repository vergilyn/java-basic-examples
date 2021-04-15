package com.vergilyn.examples.jvm.allocate;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Nacos-2.0.0: `ConfigChangeBatchListenRequestHandler.class -> StringPool.class`
 * 中`StringPool`的意义，重点阅读：
 *   1) <a href="https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html">
 *       【美团】深入解析String#intern</a><br/>
 *   2) <a href="http://lovestblog.cn/blog/2016/11/06/string-intern/">
 *       JVM源码分析之String.intern()导致的YGC不断变长</a><br/>
 *   3) <a href="https://javadoop.com/post/string">Java 字符串常量池介绍</a><br/>
 *
 * <p>
 *   现在个人理解：Nacos.StringPool的目的就是实现与`String.intern()`类似的功能：减少内存占用、
 * </p>
 * @author vergilyn
 * @since 2021-04-13
 */
public class NacosStringPoolTestng {

	@Test
	public void validNacos(){
		RequestDto dto = new RequestDto("vergilyn");

		String str1 = dto.getName();
		String str2 = dto.getName();

		assertTrue(str1 == str2);
	}

	class RequestDto {
		private String name;

		public RequestDto(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
