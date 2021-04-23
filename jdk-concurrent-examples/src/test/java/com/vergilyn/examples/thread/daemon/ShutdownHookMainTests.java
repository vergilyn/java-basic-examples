package com.vergilyn.examples.thread.daemon;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author vergilyn
 * @since 2021-04-23
 */
public class ShutdownHookMainTests extends AbstractThreadTests {

	public static void main(String[] args) {
		ShutdownHookMainTests tests = new ShutdownHookMainTests();

		long threadTime = 4;
		long hookTime = 3;
		/* 当最后显示调用`System.exit(0)`时：
		 *   与 `daemon = true/false` 无关。
		 *   若`threadTime > hookTime`，hook结束后，未执行完成的线程会被强制舍弃。
		 *   拖`threadTime < hookTime`，因为jvm还未关闭，所以线程还是会正常运行。
		 */
		tests.testTemplate(true, threadTime);

		/*
		 * JDK提供了Java.Runtime.addShutdownHook(Thread hook)方法，
		 * 可以注册一个JVM关闭的钩子，这个钩子可以在一下几种场景中被调用：
		 *   1) 程序正常退出
		 *   2) 使用System.exit()
		 *   3) 终端使用Ctrl+C触发的中断
		 *   4) 系统关闭
		 *   5) OutOfMemory宕机
		 *   6) 使用Kill pid命令干掉进程（注：在使用kill -9 pid时，是不会被调用的）
		 */
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				System.out.println("shutdown hook: sleep 3s BEGIN");
				tests.safeSleep(hookTime, TimeUnit.SECONDS);
				System.out.println("shutdown hook: sleep 3s END");
			}
		});

		System.exit(0);
	}
}
