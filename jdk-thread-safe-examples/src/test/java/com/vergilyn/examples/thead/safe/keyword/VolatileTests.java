package com.vergilyn.examples.thead.safe.keyword;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-06-03
 */
class VolatileTests {

	/**
	 * FIXME 2021-06-04 网上的测试“可见性”代码基本都是这样的，但其实“value-thread”是存在执行completed的可能的
	 * （最简单的就是while中加sleep，其实加其它代码也可以）
	 *
	 * volatile的"可见性"：
	 * <blockquote>
	 * 　　当被volatile修饰时，它会保证修改的值会<b>立即被更新到主存</b>，当有其他线程需要读取时，它会去内存中读取新值。
	 *    <br/>
	 * 　　而<b>普通变量</b>不能保证可见性，因为普通共享变量被修改之后，<b>什么时候被写入主存是不确定的</b>，
	 *    当其他线程去读取时，此时内存中可能还是原来的旧值，因此无法保证可见性。
	 * </blockquote>
	 *
	 * <p>个人理解：<br/>
	 *   volatile的可见性可以理解成“及时共享”，普通变量可用理解成“延迟共享”（当写回主内存时，就跟volatile一致了）。
	 * </p>
	 */
	@SneakyThrows
	@Test
	public void visibility(){
		Properties properties = new Properties();
		ExecutorService pool = Executors.newFixedThreadPool(3);

		pool.execute(() -> {
			printf("`set-thread` running");

			safeSleep(TimeUnit.SECONDS, 2);

			properties.value = 1;
			properties.volatileValue = 1;

			printf("`set-thread` completed: %s", JSON.toJSON(properties));
		});

		// 错误的理解：由于`value`不可见，所以该线程一直执行`while`
		// 当jvm将`properties.value`写回“主内存”时，`value-thread`就可以跳出while。
		pool.execute(() -> {
			printf("`value-thread` running");

			// 如果while中有例如sleep，当再次被唤醒时，会同步主存的数据，所以可以执行completed。
			// 参考：工作内存 和 主内存 的同步时机。
			while (properties.value <= 0){
			};
			printf("`value-thread` completed, value = " + properties.value);
		});

		// 由于 volatile的可见性，所以该线程能正常执行完毕
		pool.execute(() -> {
			printf("`volatileValue-thread` running");
			while (properties.volatileValue <= 0){
			};
			printf("`volatileValue` completed, volatileValue = " + properties.volatileValue);
		});


		TimeUnit.MINUTES.sleep(1);
	}

	@Data
	static class Properties {
		public volatile Integer volatileValue = 0;
		public Integer value = 0;
	}

	private void printf(String format, Object... args){
		String prefix = String.format("[%s][%s] >>>> ", LocalTime.now(), Thread.currentThread().getName());
		System.out.printf(prefix + format, args).println();
	}

	private void safeSleep(TimeUnit unit, int timeout){
		try {
			new Semaphore(0).tryAcquire(timeout, unit);
		} catch (InterruptedException e) {
		}
	}
}
