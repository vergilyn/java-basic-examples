package com.vergilyn.examples.future;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;

import lombok.SneakyThrows;
import org.testng.annotations.Test;

/**
 * <a href="https://segmentfault.com/a/1190000019960031">Dubbo源码解析（四十八）异步化改造</a>
 * <p>
 * 起因：<br/>
 *   想理解 dubbo-filter 到底是 sync责任链，还是 async责任链。（结论：都有可能，具体看filter实现）<br/>
 *   然后就看到了上面文章，貌似 dubbo从2.7.x 才开始改造为 async。<br/>
 * <br/>
 * 题外话：<br/>
 *   dubbo 异步化 大力提倡 jdk1.8 的 CompletableFuture
 * </p>
 *
 * <blockquote>
 * dubbo原本的缺陷：<br/>
 * 1. Future只支持阻塞式的get()接口获取结果。因为future.get()会导致线程阻塞。<br/>
 * 2. Future接口无法实现自动回调，而自定义ResponseFuture虽支持callback回调但支持的异步场景有限，如不支持Future间的相互协调或组合等；<br/>
 * <br/>
 * 针对以上两个不足，CompletableFuture可以很好的解决它们：<br/>
 * 1. 针对第一点不足，因为CompletableFuture实现了CompletionStage和Future接口，
 * 所以它还是可以像以前一样通过阻塞或者轮询的方式获得结果。
 * 这一点就能保证阻塞式获得结果，也就是同步调用不会被抛弃。
 * 当然本身也不是很建议用get()这样阻塞的方式来获取结果。<br/>
 *
 * <br/>
 * 2. 针对第二点不足，首先是自动回调，CompletableFuture提供了良好的回调方法。比如下面四个方法有关计算结果完成时的处理：<br/>
 *   - {@linkplain java.util.concurrent.CompletableFuture#whenComplete(BiConsumer)} <br/>
 *   - {@linkplain java.util.concurrent.CompletableFuture#whenCompleteAsync(BiConsumer)} <br/>
 *   - {@linkplain java.util.concurrent.CompletableFuture#whenCompleteAsync(BiConsumer, Executor)} <br/>
 *   - {@linkplain java.util.concurrent.CompletableFuture#exceptionally(Function)} <br/>
 * </blockquote>
 *
 * @author vergilyn
 * @since 2021-12-30
 */
public class CompletableFutureDubboTestng {

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	@SneakyThrows
	@Test(invocationTimeOut = 10_000)
	public void invokeWhenComplete(){
		CompletableFuture<LocalTime> completableFuture = new CompletableFuture<>();

		// 在`whenComplete` 之前或之后 都可以.
		completableFuture.complete(LocalTime.now());
		sleepSafe(TimeUnit.SECONDS, 1);

		completableFuture.whenComplete((localTime, throwable) -> {
			print("whenComplete, " + localTime.toString());
		});

		TimeUnit.MINUTES.sleep(1);
	}


	/**
	 * 打印信息示例：
	 * <pre>
	 * [10:19:07.070][TestNG-method=whenComplete-1] >>>> wait async complete....begin
	 * [10:19:07.070][ForkJoinPool.commonPool-worker-1] >>>> `supplyAsync` begin.
	 * [10:19:07.071][TestNG-method=whenComplete-1] >>>> wait async complete....end
	 * [10:19:09.086][ForkJoinPool.commonPool-worker-1] >>>> `supplyAsync` end.
	 * [10:19:09.087][ForkJoinPool.commonPool-worker-1] >>>> whenComplete, value: 10:19:07.071
	 * </pre>
	 *
	 * 可知:
	 * 1. {@linkplain CompletableFuture#whenComplete(BiConsumer)} 也是async！
	 */
	@SneakyThrows
	@Test(invocationTimeOut = 10_000)
	public void whenComplete(){
		CompletableFuture<LocalTime> completableFuture = CompletableFuture.supplyAsync(() -> {
			print("`supplyAsync` begin.");

			LocalTime result = LocalTime.now();
			sleepSafe(TimeUnit.SECONDS, 2);

			print("`supplyAsync` end.");

			return result;
		});

		print("wait async complete....begin");
		// 1. `#whenComplete()` 是当某个任务执行完成后执行的回调方法
		// 2. `#whenComplete()` 也是 async-execute。 那`#whenCompleteAsync()`有是什么意思？
		completableFuture.whenComplete((localTime, throwable) -> {
			print("whenComplete, value: " + localTime);
		});
		print("wait async complete....end");

		TimeUnit.MINUTES.sleep(1);
	}

	/**
	 * 与`whenComplete`的区别：由谁来执行 BiConsumer。<br/>
	 * <a href="https://blog.csdn.net/leon_wzm/article/details/80560081">
	 *     CompletableFuture的async后缀函数与不带async的函数的区别</a>
	 *
	 * <pre>
	 * > future的whenComplete的内容由哪个线程来执行，取决于哪个线程X执行了f.complete()。
	 * > 但是当X线程执行了f.complete()的时候，whenComplete还没有被执行到的时候（就是事件还没有注册的时候），
	 * > 那么X线程就不会去同步执行whenComplete的回调了。
	 * > 这个时候哪个线程执行到了whenComplete的事件注册的时候，就由哪个线程自己来同步执行whenComplete的事件内容。
	 * >
	 * > 而whenCompleteAsync的场合，就简单很多。一句话就是线程池里面拿一个空的线程或者新启一个线程来执行回调。
	 * > 和执行f.complete的线程以及执行whenCompleteAsync的线程无关。
	 * </pre>
	 */
	@SneakyThrows
	@Test(invocationTimeOut = 10_000)
	public void whenCompleteCompare(){
		CompletableFuture<LocalTime> completableFuture = CompletableFuture.supplyAsync(() -> {
			print("`supplyAsync` begin.");

			LocalTime result = LocalTime.now();
			sleepSafe(TimeUnit.SECONDS, 2);

			print("`supplyAsync` end.");

			return result;
		});

		// 确保，由 其它线程执行`future.complete()`（并完成）。
		sleepSafe(TimeUnit.SECONDS, 3);

		print("wait async complete....begin");

		// `whenComplete`，则一定是 main-thread 打印"whenComplete..." （testng的main-thread类似 `TestNG-method=whenCompleteCompare-1` ）
		// `whenCompleteAsync`，则是由类似`ForkJoinPool.commonPool-worker-1`的子线程打印"whenComplete..."
		completableFuture.whenComplete((localTime, throwable) -> {
			print("whenComplete, value: " + localTime);
		});
		print("wait async complete....end");

		TimeUnit.MINUTES.sleep(1);
	}

	private void print(String str){
		System.out.printf("[%s][%s] >>>> %s \n", LocalTime.now(), Thread.currentThread().getName(), str);
	}

	private void sleepSafe(TimeUnit timeUnit, long timeout){
		try {
			timeUnit.sleep(timeout);
		} catch (InterruptedException e) {
		}
	}
}
