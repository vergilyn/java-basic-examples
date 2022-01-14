package com.vergilyn.examples.usage.u0002.core;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.vergilyn.examples.usage.u0002.FlushFailureHandler;
import com.vergilyn.examples.usage.u0002.FullIntervalHandlerContext;
import com.vergilyn.examples.usage.u0002.FullIntervalHandlerTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultFullIntervalHandlerTemplate<T> implements FullIntervalHandlerTemplate<T> {

	private final FullIntervalHandlerContext<T> context;
	private final InnerFlushThread innerFlushThread;
	private boolean isShutdown = false;

	private final BlockingQueue<Object> flushBell = new ArrayBlockingQueue<Object>(1);
	private final Object bellItem = new Object();

	public DefaultFullIntervalHandlerTemplate(FullIntervalHandlerContext<T> context) {
		this.context = context;

		// 2022-01-14 >>>>
		//   例如 RocketMQ 消费者push-mode拉取message，其实用的就是`Executors.newScheduledThreadPool(...)`
		this.innerFlushThread = new InnerFlushThread(context.getStorageClass().getName());
		this.innerFlushThread.start();

		// TODO 2022-01-14 如果是spring，可以基于 `DisposableBean#destroy()` 实现
		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	@Override
	public boolean add(T data){
		checkWritable();
		boolean rs = context.storage().add(data);
		checkThreshold();
		return rs;
	}

	@Override
	public boolean addAll(Collection<T> data){
		checkWritable();
		boolean rs = context.storage().addAll(data);
		checkThreshold();
		return rs;
	}

	@Override
	public void manualNotifyFlush(int limit) {
		syncFlush(limit);
	}

	protected void checkWritable(){
		if (isShutdown){
			throw new RuntimeException();
		}
	}

	protected void checkThreshold(){
		if (context.storage().size() >= context.getThreshold()){
			notifyFlush();
		}
	}

	protected void notifyFlush(){
		flushBell.offer(bellItem);
	}

	/**
	 * VFIXME 2021-04-24 扩容处理堆积的数据。
	 * @param limit
	 */
	protected synchronized void syncFlush(int limit){
		List<T> data = context.storage().poll(limit);
		if (data == null || data.isEmpty()){
			return;
		}

		boolean isSuccess;
		Throwable throwable = null;
		try {
			isSuccess = context.handler().flush(data);
		}catch (Throwable t){
			isSuccess = false;
			throwable = t;
		}

		checkThreshold();

		FlushFailureHandler<T> flushFailureHandler = context.failureHandler();
		if (!isSuccess && flushFailureHandler != null){
			flushFailureHandler.flushFailure(data, throwable);
		}
	}

	public void shutdown(){
		isShutdown = true;

		/* 假设 shutdown 前InnerFlushThread正在执行 flush-data-A，shutdown 触发执行`flushData(-1) B`
		 * A 执行耗时大于 B。尽量保证A执行完成，且先于B执行。
		 */
		try {
			this.innerFlushThread.join();
		} catch (InterruptedException e) {
			log.error("[vergilyn] Waits for \"flush-data\" thread run completed error, StorageClass = {}",
					context.getStorageClass().getName(), e);
		}

		syncFlush(-1);
	}

	private class InnerFlushThread extends Thread{
		public InnerFlushThread(String name) {
			setName(name);
			setDaemon(false);
		}

		@Override
		public void run() {
			while (!isShutdown) {
				try {
					flushBell.poll(context.getIntervalMs(), TimeUnit.MILLISECONDS);
					syncFlush(context.getThreshold());
				} catch (Exception e) {
					log.error("[vergilyn] flush-data error, StorageClass = {}",
							context.getStorageClass().getName(), e);
				}
			}
		}
	}
}
