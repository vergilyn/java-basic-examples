package com.vergilyn.examples.jdk8.features.closeable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-03-29
 */
public class TryResourceCloseTests {

	/**
	 * <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">TryResourceCloseTests.html</a>
	 */
	@Test
	public void tryWithResource() throws IOException, URISyntaxException {
		ClassLoader classLoader = this.getClass().getClassLoader();
		URL resource = classLoader.getResource("");
		File file = new File(resource.toURI().getPath() + "/data.json");

		// 'is' Nullable
		try (FileInputStream is = null){

		}

		// Cannot assign a value to final variable 'is'
		try (FileInputStream is = new FileInputStream(file)){
			// is = new FileInputStream(file);
		}

	}
}
