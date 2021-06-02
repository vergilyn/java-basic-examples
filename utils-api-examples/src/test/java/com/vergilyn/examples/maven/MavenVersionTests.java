package com.vergilyn.examples.maven;

import java.util.jar.Manifest;

import org.junit.jupiter.api.Test;

/**
 * <blockquote>
 *     <a href="https://blog.csdn.net/10km/article/details/79013499">maven:读取程序版本号的三种方案</a>：<br/>
 *     1) `META-INF/../pom.properties`<br/>
 *     2) `MANIFEST.MF` {@linkplain Manifest}<br/>
 *     3) 通过`template-maven-plugin`生成`Version.java`<br/>
 * </blockquote>
 *
 * <p>
 * 方法`1 & 2`都需要将应用程序打成jar包才能读取版本信息。<br/>
 * 方法`3`可以在程序开发调试的时候获取到。
 * </p>
 *
 * @author vergilyn
 * @since 2021-06-02
 *
 * @see <a href="https://github.com/mojohaus/templating-maven-plugin">templating-maven-plugin</a>
 */
public class MavenVersionTests {

	@Test
	public void manifest(){
	}
}
