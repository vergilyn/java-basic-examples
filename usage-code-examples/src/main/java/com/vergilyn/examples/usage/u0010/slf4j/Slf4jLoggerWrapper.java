package com.vergilyn.examples.usage.u0010.slf4j;

import com.vergilyn.examples.usage.u0010.Logger;
import com.vergilyn.examples.usage.u0010.core.ThreadLocalCustomizePrefixesLogger;
import org.slf4j.spi.LocationAwareLogger;

public class Slf4jLoggerWrapper implements Logger {
	private static final String classname = Slf4jLoggerWrapper.class.getName();
	private static final String FQCN = ThreadLocalCustomizePrefixesLogger.class.getName();

	private final org.slf4j.Logger logger;
	private final LocationAwareLogger locationAwareLogger;

	public Slf4jLoggerWrapper(org.slf4j.Logger logger) {
		this.locationAwareLogger = logger instanceof LocationAwareLogger ? (LocationAwareLogger) logger : null;
		this.logger = logger;
	}

	@Override
	public String getName() {
		return classname;
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String msg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, null);
			return;
		}
		logger.trace(msg);
	}

	@Override
	public void trace(String format, Object arg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, format, new Object[]{arg}, null);
			return;
		}
		logger.trace(format, arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, format, new Object[]{arg1, arg2}, null);
			return;
		}
		logger.trace(format, arg1, arg2);
	}

	@Override
	public void trace(String format, Object... arguments) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, format, arguments, null);
			return;
		}
		logger.trace(format, arguments);
	}

	@Override
	public void trace(String msg, Throwable t) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, t);
			return;
		}
		logger.trace(msg, t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, null);
			return;
		}
		logger.debug(msg);
	}

	@Override
	public void debug(String format, Object arg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, format, new Object[]{arg}, null);
			return;
		}
		logger.debug(format, arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, format, new Object[]{arg1, arg2}, null);
			return;
		}
		logger.debug(format, arg1, arg2);
	}

	@Override
	public void debug(String format, Object... arguments) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, format, arguments, null);
			return;
		}
		logger.debug(format, arguments);
	}

	@Override
	public void debug(String msg, Throwable t) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, t);
			return;
		}
		logger.debug(msg, t);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, null);
			return;
		}
		logger.info(msg);
	}

	@Override
	public void info(String format, Object arg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg}, null);
			return;
		}
		logger.info(format, arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, format, new Object[]{arg1, arg2}, null);
			return;
		}
		logger.info(format, arg1, arg2);
	}

	@Override
	public void info(String format, Object... arguments) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, format, arguments, null);
			return;
		}
		logger.info(format, arguments);
	}

	@Override
	public void info(String msg, Throwable t) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, t);
			return;
		}
		logger.info(msg, t);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
			return;
		}
		logger.warn(msg);
	}

	@Override
	public void warn(String format, Object arg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, format, new Object[]{format}, null);
			return;
		}
		logger.warn(format, arg);
	}

	@Override
	public void warn(String format, Object... arguments) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, format, arguments, null);
			return;
		}
		logger.warn(format, arguments);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, format, new Object[]{arg1, arg2}, null);
			return;
		}
		logger.warn(format, arg1, arg2);
	}

	@Override
	public void warn(String msg, Throwable t) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
			return;
		}
		logger.warn(msg, t);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
			return;
		}
		logger.warn(msg);
	}

	@Override
	public void error(String format, Object arg) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, format, new Object[]{arg}, null);
			return;
		}
		logger.warn(format, arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, format, new Object[]{arg1, arg2}, null);
			return;
		}
		logger.warn(format, arg1, arg2);
	}

	@Override
	public void error(String format, Object... arguments) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, format, arguments, null);
			return;
		}
		logger.warn(format, arguments);
	}

	@Override
	public void error(String msg, Throwable t) {
		if (locationAwareLogger != null) {
			locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, t);
			return;
		}
		logger.warn(msg, t);
	}
}
