package com.vergilyn.examples.format;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 *
 * @author vergilyn
 * @since 2021-11-25
 *
 * @see com.google.common.base.Stopwatch#toString()
 */
public class TimeUnitFormat {

	public static String of(long amount, TemporalUnit unit){
		return ofNanos(Duration.of(amount, unit).toNanos());
	}

	public static String ofMills(long mills){
		return ofNanos(mills * 1000_000);
	}

	public static String ofNanos(long nanos){
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
