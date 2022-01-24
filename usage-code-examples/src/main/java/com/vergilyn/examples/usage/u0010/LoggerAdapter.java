package com.vergilyn.examples.usage.u0010;

/**
 * Logger provider
 *
 * @author vergilyn
 * @since 2022-01-24
 *
 * @see <a href="https://github.com/apache/dubbo/blob/dubbo-3.0.5/dubbo-common/src/main/java/org/apache/dubbo/common/logger/LoggerAdapter.java">
 *     dubbo-common, LoggerAdapter.java</a>
 */
public interface LoggerAdapter {
	/**
	 * Get a logger
	 *
	 * @param key the returned logger will be named after clazz
	 * @return logger
	 */
	Logger getLogger(Class<?> key);

	/**
	 * Get a logger
	 *
	 * @param key the returned logger will be named after key
	 * @return logger
	 */
	Logger getLogger(String key);
}
