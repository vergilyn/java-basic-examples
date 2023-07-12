package com.vergilyn.examples.usage.logger.message.dubbo;

/**
 * Logger provider
 *
 * @author vergilyn
 * @since 2023-07-11
 *
 * @see <a href="https://github.com/apache/dubbo/blob/dubbo-3.2.0/dubbo-common/src/main/java/org/apache/dubbo/common/logger/LoggerAdapter.java">
 *     dubbo-common, LoggerAdapter.java</a>
 */
public interface MsgLoggerAdapter {
	/**
	 * Get a logger
	 *
	 * @param key the returned logger will be named after clazz
	 * @return logger
	 */
	MsgLogger getMsgLogger(Class<?> key);

	/**
	 * Get a logger
	 *
	 * @param key the returned logger will be named after key
	 * @return logger
	 */
	MsgLogger getMsgLogger(String key);
}
