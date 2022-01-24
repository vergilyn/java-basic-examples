package com.vergilyn.examples.usage.u0010;

/**
 * Standard Logging Levels as an enumeration for use internally. This enum is used as a parameter in any public APIs.
 *
 * @author vergilyn
 * @since 2022-01-24
 *
 * @see org.apache.logging.log4j.spi.StandardLevel
 * @see org.springframework.boot.logging.LogLevel
 */
public enum LogLevelEnum {

	TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF

}
