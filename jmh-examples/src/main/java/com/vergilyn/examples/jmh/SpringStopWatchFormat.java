package com.vergilyn.examples.jmh;

import org.springframework.util.StopWatch;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

public class SpringStopWatchFormat {

	/**
	 * `guava.Stopwatch` 的风格输出 elapsed-time。
	 * （也可以统一用 `ms`输出。）
	 *
	 * @see org.springframework.util.StopWatch#toString()
	 * @see com.google.common.base.Stopwatch#toString()
	 */
	public static String toString(StopWatch stopWatch){
		if (stopWatch == null){
			return "";
		}

		long totalTimeNanos = stopWatch.getTotalTimeNanos();
		StringBuilder sb = new StringBuilder();
		sb.append("running time = ").append(guavaFormat(totalTimeNanos));

		StopWatch.TaskInfo[] taskInfos = stopWatch.getTaskInfo();
		if (taskInfos.length == 0){
			sb.append("; No task info kept");
		}else {
			for (StopWatch.TaskInfo task : taskInfos) {
				sb.append("; [").append(task.getTaskName()).append("] took ").append(guavaFormat(task.getTimeNanos()));
				long percent = Math.round(100.0 * task.getTimeNanos() / totalTimeNanos);
				sb.append(" = ").append(percent).append("%");
			}
		}
		return sb.toString();
	}

	/**
	 * @see com.google.common.base.Stopwatch#toString()
	 */
	public static String guavaFormat(long nanos){
		TimeUnit unit = chooseUnit(nanos);

		double value = (double) nanos / NANOSECONDS.convert(1, unit);

		return formatCompact4Digits(value) + " " + abbreviate(unit);
	}

	private static String formatCompact4Digits(double value) {
		return String.format(Locale.ROOT, "%.4g", value);
	}

	private static TimeUnit chooseUnit(long nanos) {
		if (DAYS.convert(nanos, NANOSECONDS) > 0) {
			return DAYS;
		}
		if (HOURS.convert(nanos, NANOSECONDS) > 0) {
			return HOURS;
		}
		if (MINUTES.convert(nanos, NANOSECONDS) > 0) {
			return MINUTES;
		}
		if (SECONDS.convert(nanos, NANOSECONDS) > 0) {
			return SECONDS;
		}
		if (MILLISECONDS.convert(nanos, NANOSECONDS) > 0) {
			return MILLISECONDS;
		}
		if (MICROSECONDS.convert(nanos, NANOSECONDS) > 0) {
			return MICROSECONDS;
		}
		return NANOSECONDS;
	}

	private static String abbreviate(TimeUnit unit) {
		switch (unit) {
			case NANOSECONDS:
				return "ns";
			case MICROSECONDS:
				return "\u03bcs"; // μs
			case MILLISECONDS:
				return "ms";
			case SECONDS:
				return "s";
			case MINUTES:
				return "min";
			case HOURS:
				return "h";
			case DAYS:
				return "d";
			default:
				throw new AssertionError();
		}
	}
}
