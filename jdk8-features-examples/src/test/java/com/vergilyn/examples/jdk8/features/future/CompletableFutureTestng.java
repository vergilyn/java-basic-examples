package com.vergilyn.examples.jdk8.features.future;

import java.util.concurrent.CompletableFuture;

import org.testng.annotations.Test;

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
