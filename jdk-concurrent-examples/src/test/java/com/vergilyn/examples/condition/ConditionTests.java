package com.vergilyn.examples.condition;

import java.time.LocalTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-12
 *
 * @see com.vergilyn.examples.usage.ListenerExecutorTests
 * @see java.util.concurrent.ArrayBlockingQueue
 * @see LinkedBlockingQueue#take()
 */
public class ConditionTests {
	private final ReentrantLock _takeLock = new ReentrantLock();

	// condition 一般都依赖于lock
	private final Condition _condition = _takeLock.newCondition();
	private final Semaphore _semaphore = new Semaphore(0);

	@SneakyThrows
	@Test
	public void demo(){

		ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(2);

		ScheduledFuture<?> scheduledFuture = pool.scheduleAtFixedRate(new Thread("producer") {
			@Override
			public void run() {
				lock();
				try {
					_semaphore.tryAcquire(2, TimeUnit.SECONDS);
					_condition.signal();
					System.out.printf("[vergilyn][%s] >>>> #signal(): %s \n",
							Thread.currentThread().getId(), LocalTime.now());
				} catch (InterruptedException e) {
				} finally {
					unlock();
				}
			}
		}, 1, 1, TimeUnit.SECONDS);

		pool.schedule(new Thread("consumer"){
			@Override
			public void run() {
				lock();
				try {
					_semaphore.release();
					// await(): The lock associated with this Condition is atomically released
					_condition.await();
					scheduledFuture.cancel(true);
					System.out.printf("[vergilyn][%s] >>>> consumer, #await(): %s \n",
							Thread.currentThread().getId(), LocalTime.now());
					System.exit(1);
				} catch (InterruptedException e) {
				} finally {
					unlock();
				}
			}
		}, 2, TimeUnit.SECONDS);

		// prevent jvm exit.
		new Semaphore(0).acquire();
	}

	private void unlock() {
		System.out.printf("[vergilyn][%d] >>>> BEFORE unlock() \n", Thread.currentThread().getId());
		_takeLock.unlock();
		System.out.printf("[vergilyn][%d] >>>> AFTER unlock() \n", Thread.currentThread().getId());
	}

	private void lock() {
		System.out.printf("[vergilyn][%d] >>>> BEFORE lock() \n", Thread.currentThread().getId());
		_takeLock.lock();
		System.out.printf("[vergilyn][%d] >>>> AFTER lock() \n", Thread.currentThread().getId());
	}

}
