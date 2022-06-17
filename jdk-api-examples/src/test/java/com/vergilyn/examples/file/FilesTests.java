package com.vergilyn.examples.file;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see java.nio.file.Files
 * @see Paths
 */
public class FilesTests {

	@SneakyThrows
	@Test
	public void test(){

		// D:\VergiLyn\Workspace Git\java-basic-examples\jdk-api-examples
		System.out.println(Paths.get("").toAbsolutePath());

		// D:\
		System.out.println(Paths.get("/").toAbsolutePath());

		// 特别注意 getResource 返回的是URL，会被URLEncoder，导致拼接file-path后无法正确获取！
		// 例如 " "会被转换成`%20`

		// file:/D:/VergiLyn/Workspace%20Git/java-basic-examples/jdk-api-examples/target/test-classes/com/vergilyn/examples/file/
		System.out.println(this.getClass().getResource(""));

		// file:/D:/VergiLyn/Workspace%20Git/java-basic-examples/jdk-api-examples/target/test-classes/
		System.out.println(this.getClass().getResource("/"));


	}
}
