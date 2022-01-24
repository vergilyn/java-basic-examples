package com.vergilyn.examples.usage.u0010.core;

import com.vergilyn.examples.usage.u0010.CustomizePrefixesLogger;
import com.vergilyn.examples.usage.u0010.Logger;
import lombok.extern.slf4j.Slf4j;

/**
 * 线程池复用会导致 thread-local 也被复用。某些实际使用场景可以用{@linkplain #appendFirstPrefix(String)} 规避！
 *
 * @author vergilyn
 * @since 2022-01-24
 *
 * @see org.apache.logging.slf4j.SLF4JLogger
 */
@Slf4j
public class ThreadLocalCustomizePrefixesLogger extends CustomizePrefixesLogger {
	private static final String classname = ThreadLocalCustomizePrefixesLogger.class.getName();

	/**
	 * XXX 2022-01-24，基于性能考虑以下取舍
	 * <p>
	 * 1) {@code ThreadLocal<String>} 和 {@code ThreadLocal<CopyOnWriteArraySet<String>>}。 <br/>
	 *  String: 性能更好且占用内存更少。 缺点：需要使用者保证不重复，例如不应该出现 `log-prefix = [id-123][id-123]` <br/>
	 *  CopyOnWriteArraySet: 一定程度避免了String可能出现的重复。  （没意义，还是可能出现 `log-prefix = [id-123][id:123]`）
	 * </p>
	 *
	 * <p>
	 * 2）是否构造函数时就创建 ThreadLocal 和 String/CopyOnWriteArraySet？ <br/>
	 * 如果创建，则一定程度浪费内存。如果延迟创建，则每次要if判断，降低了效率（虽然微乎其微）！
	 * （根本还是：牺牲空间换时间，还是牺牲时间换空间）
	 * </p>
	 */
	private static final ThreadLocal<StringBuffer> threadLocal = ThreadLocal.withInitial(StringBuffer::new);

	public ThreadLocalCustomizePrefixesLogger(Logger logger) {
		super(logger);
	}

	@Override
	public String getName() {
		return classname;
	}

	@Override
	protected String appendCustomizePrefixes(String msg) {
		return getAllPrefixes() + defaultValue(msg);
	}

	@Override
	public void appendFirstPrefix(String firstPrefix) {
		clearAllPrefix();
		appendPrefix(firstPrefix);
	}

	@Override
	public void appendPrefix(String prefix) {
		threadLocal.get().append(prefix);
	}

	@Override
	public void clearAllPrefix() {
		threadLocal.get().setLength(0);
	}

	@Override
	public String getAllPrefixes() {
		return defaultValue(threadLocal.get().toString());
	}

	private static String defaultValue(String str){
		return isEmpty(str) ? "" : str.trim();
	}

	private static boolean isEmpty(String str){
		return str == null || "".equals(str.trim());
	}
}
