package com.vergilyn.examples.queue;

import java.time.LocalTime;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.*;

import lombok.SneakyThrows;
import org.testng.annotations.Test;

/**
 * usage:
 *   <ol>
 *       <li>{@linkplain ScheduledThreadPoolExecutor.ScheduledFutureTask}</li>
 *       <li>{@linkplain TimerQueue.DelayedTimer}</li>
 *   </ol>
 * @author vergilyn
 * @since 2021-04-15
 */
@SuppressWarnings("JavadocReference")
public class DelayedQueueTestng {

	@SneakyThrows
	@Test
	public void test(){
		DelayQueue<TimesDelayed> delayQueue = new DelayQueue();

		System.out.printf("[vergilyn] >>>> BEFORE #add(), %s \n", LocalTime.now());
		delayQueue.add(new TimesDelayed(1));  // delay: 1s
		delayQueue.add(new TimesDelayed(2));  // delay: 2s + 1s

		do {
			// Retrieves and removes the head of this delayQueue,
			// waiting if necessary until an element with an expired delay is available on this delayQueue.
			delayQueue.take();
			System.out.printf("[vergilyn] >>>> #take(), %s \n", LocalTime.now());

		} while (!delayQueue.isEmpty());

	}

	/**
	 * @see TimerQueue.DelayedTimer
	 */
	class TimesDelayed implements Delayed {
		private int times = 0;

		public TimesDelayed(int times) {
			this.times = times;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			return unit.convert(times--, TimeUnit.SECONDS);
		}

		@Override
		public int compareTo(Delayed other) {
			if (other == this) {
				return 0;
			}
			if (other instanceof TimesDelayed){
				TimesDelayed x = (TimesDelayed) other;
				return this.times - x.times;
			}

			long d = this.getDelay(TimeUnit.SECONDS) - other.getDelay(TimeUnit.SECONDS);
			return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
		}
	}
}
