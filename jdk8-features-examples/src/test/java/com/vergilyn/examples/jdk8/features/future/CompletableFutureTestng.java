package com.vergilyn.examples.jdk8.features.future;

import org.testng.annotations.Test;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author vergilyn
 * @since 2021-01-29
 */
public class CompletableFutureTestng {

	@Test
	public void test(){
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "helloworld");
	}
}
