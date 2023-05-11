package com.vergilyn.examples.thread.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractThreadLocalTests {


	protected static void println(String format){
		printf(format);
		System.out.println();
	}

	protected static void printf(String format, Object... args){
		String prefix = String.format("[%s][thread-%s] >>>> ",
		                              LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSSSSS")),
		                              Thread.currentThread().getName());

		System.out.printf(prefix + format, args);
	}

	protected static interface ThreadLocalContextWrapper {
		StringBuffer get();

		void set(StringBuffer value);

		void clear();
	}


	protected static class ThreadLocalContext {
		private static final ThreadLocal<StringBuffer> _context = ThreadLocal.withInitial(StringBuffer::new);


		public static StringBuffer get() {
			return _context.get();
		}

		public static void set(StringBuffer value) {
			_context.set(value);
		}

		public static void clear() {
			_context.remove();
		}
	}

	protected static class InheritableThreadLocalContext {
		private static final ThreadLocal<StringBuffer> _context = new InheritableThreadLocal<>();

		public static StringBuffer get() {
			return _context.get();
		}

		public static void set(StringBuffer value) {
			_context.set(value);
		}

		public static void clear() {
			_context.remove();
		}
	}

	protected static class TransmittableThreadLocalContext {
		private static final ThreadLocal<StringBuffer> _context = new TransmittableThreadLocal<>();

		public static StringBuffer get() {
			return _context.get();
		}

		public static void set(StringBuffer value) {
			_context.set(value);
		}

		public static void clear() {
			_context.remove();
		}
	}
}
