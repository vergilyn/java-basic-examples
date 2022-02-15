package com.vergilyn.examples.usage.u0010;

import lombok.SneakyThrows;
import org.testng.annotations.Test;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * 总体来说，如果是简单的 log-prefix 需求，比较容易满足。
 * 但如果是{@linkplain #syncSubMethodOwnPrefix()}，过于复杂了，有点得不偿失的感觉。
 *
 * <br/> 对于线程池复用导致`ThreadLocal`无法满足期望，可以参考：
 * <a href="https://github.com/alibaba/transmittable-thread-local">alibaba/transmittable-thread-local, `README.md`</a>
 *
 * <p>2022-01-27: <br/>
 *   如果很灵活，意味着使用复杂，有点得不偿失。所以，按最简单约定：<br/>
 *   1. 如果添加到 ThreadLocal，那么之后的所有Logger 都会打印这些 log-prefix（不存在 某个节点 之后 不打印某个log-prefix）。 <br/>
 *   2. 直到 重新在 “入口”处主动调用`clear-log-prefix`，也可以以此避免线程池复用带来的问题。 <br/>
 *
 *   这样的话，实现 和 使用起来都相对简单很多。也满足实际的绝大多数基本场景。
 *   （其实不如直接`传参 log-prefix`，虽然对 方法签名 不是很友好，但简单好理解。）
 * </p>
 *
 * 2022-02-15：其核心思路就是 {@linkplain org.slf4j.MDC} 的思路，所以直接用 MDC 就行了。
 * 可以参考`logger-examples/logger-mdc-examples`
 *
 * @author vergilyn
 * @since 2022-01-24
 *
 * @see org.slf4j.MDC
 */
public class ThreadLocalCustomizePrefixesLoggerTestng {

	private static final CustomizePrefixesLogger logger = LoggerFactory.getLogger(
			ThreadLocalCustomizePrefixesLoggerTestng.class);

	@Test
	public void demo(){
		AtomicInteger index = new AtomicInteger();

		// “入口方法” 调用`#appendFirstPrefix`
		logger.appendFirstPrefix("[main-0]");

		for (int i = 0; i < 4; i++) {
			((Supplier<Object>) () -> {
				Integer idx = index.incrementAndGet();

				if (idx % 2 == 0){
					// “非入口方法” 如果需要追加log-prefix，则调用 `#appendPrefix`
					logger.appendPrefix("[idx-" + idx + "]");
				}
				return null;
			}).get();
		}

		logger.info("local-time: {}", LocalTime.now());
	}

	/**
	 * FIXME 2022-01-24:
	 * <p> 1. 线程池复用，导致 ThreadLocal 也被复用。<br/>
	 *   虽然 可以简单的通过 {@linkplain CustomizePrefixesLogger#appendFirstPrefix(String)} 来避免。
	 *   但是不够友好，需要明确找到 “入口方法”！
	 * </p>
	 *
	 * <p> 2. 需要手动将 prev-thread-log-prefix 带过来 <br/>
	 *    其实和`1.`类似，必须要找到“入口方法”。把 prev-thread-log-prefix 手动带过来！
	 * </p>
	 *
	 * 2022-01-24，个人实际场景下：
	 * 因为都是 spring-mvc 或者 dubbo-provider 之类的项目，绝大多数情况都可以知道 哪个方法是“入口方法”。
	 * 所有，这2个问题其实可以用通过以下`demo`的写法来避免。
	 */
	@SneakyThrows
	@Test(threadPoolSize = 2, invocationCount = 4)
	public void threadPoolReuse(){
		ExecutorService pool = Executors.newFixedThreadPool(3);

		AtomicInteger index = new AtomicInteger();

		// "入口方法"：clear-prev-log-prefix & set-current-log-prefix
		logger.appendFirstPrefix("[main-]" + ThreadLocalRandom.current().nextInt());

		String prevThreadLogPrefix = logger.getAllPrefixes();
		for (int i = 0; i < 4; i++) {
			pool.submit(() -> {
				int idx = index.incrementAndGet();
				// 记得把`prev-thread-log-prefix`带过来
				String prefix = prevThreadLogPrefix + "[thread-idx-" + idx + "]";

				// 1. 线程池复用，会导致 ThreadLocal 也被复用。
				// logger.appendPrefix(prefix);
				// 一定程度上可以规避 线程池复用的带来的 问题。 缺点：要保证是“入口代码”！
				logger.appendFirstPrefix(prefix);

				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
				}
				logger.info("{}", idx);
			});
		}

		TimeUnit.SECONDS.sleep(10);
	}

	/**
	 * 同一个线程，子方法自身独有的 log-prefix，怎么解决？<br/>
	 * 2022-01-24，如果要满足以下场景，感觉有点太复杂了，最后都不知道 log-prefix 到底是什么了！
	 *
	 * <pre>
	 *     public void main(){
	 *         logger.appendFirstPrefix("[main-0]");
	 *
	 *         subMethod();
	 *
	 *         // 1. 期望不把`[sub-1][sub-sub-1]`继续带着走！
	 *         // 2. 期望带着`[sub-sub-1]`，但不带着 `[sub-1]`。
	 *         //   例如 下订单，最开始是token，在 sub-sub-method 转换成 user-id，这之后的log都打印 user-id-prefix。
	 *         //    而 `sub-1` 可能只需要在 `subMethod` 及其 sub-sub-method 中打印区分即可。
	 *         logger.info(...);
	 *     }
	 *
	 *     public void subMethod(){
	 *         logger.appendPrefix("[sub-1]");
	 *
	 *         // ...  // 内部还可以循环此逻辑！例如 `[sub-sub-1]`
	 *
	 *         logger.info(...);  // `[main-0][sub-1][sub-sub-1]`
	 *
	 *     }
	 * </pre>
	 *
	 * <p> TODO 2022-01-24, 初步想法是：<br/>
	 *  1. 提供 logger.appendPrefix(msg, isRetain): isRetain, 是否保留，为了满足上述`2.`的情况 <br/>
	 *  备注，还是可以用 {@linkplain ThreadLocal<StringBuffer>}，例如`[idx-1$true$]`，`$true/false$`表示 isRetain。在需要的地方处理一下。
	 *  （避免用{@linkplain java.util.concurrent.CopyOnWriteArraySet}）
	 *
	 *  <br/>
	 *  2. 提供 logger.clear(msg) 和 logger.clearAllForce(msg): 默认只清除`msg`节点后 `isRetain=false`的，提供 force-clear-all （当前`msg`之后的全部，忽略 isRetain）
	 * </p>
	 *
	 * <p> 关于 “执行完成” 主动调用 clear-sub-log-prefix <br/>
	 *     “执行完成” 的位置其实不确定，比如 throw-exception退出，或者有很多`if(...){ return;}`。这样会导致调用`clear-sub-log-prefix`不友好。<br/>
	 *
	 * <br/> 解决思路：
	 *   可以通过 annotation-aop，但是会牺牲一定的性能（具体牺牲多少，要看去了解aop和看压测情况。但该场景下，应该影响程度还好...）
	 * </p>
	 */
	public void syncSubMethodOwnPrefix(){
		// TODO 2022-01-24，可以按javadoc的方案实现
	}
}
