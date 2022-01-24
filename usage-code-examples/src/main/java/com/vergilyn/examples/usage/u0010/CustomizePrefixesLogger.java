package com.vergilyn.examples.usage.u0010;

public abstract class CustomizePrefixesLogger implements Logger {

	public abstract void appendFirstPrefix(String firstPrefix);

	public abstract void appendPrefix(String prefix);

	public abstract void clearAllPrefix();

	public abstract String getAllPrefixes();

	protected abstract String appendCustomizePrefixes(String originMsg);

	protected Logger logger;

	public CustomizePrefixesLogger(Logger logger) {
		this.logger = logger;
	}

	protected void setLogger(Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public void trace(String msg) {
		logger.trace(appendCustomizePrefixes(msg));
	}

	@Override
	public void trace(String format, Object arg) {
		logger.trace(appendCustomizePrefixes(format), arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		logger.trace(appendCustomizePrefixes(format), arg1, arg2);
	}

	@Override
	public void trace(String format, Object... arguments) {
		logger.trace(appendCustomizePrefixes(format), arguments);
	}

	@Override
	public void trace(String msg, Throwable t) {
		logger.trace(appendCustomizePrefixes(msg), t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void debug(String msg) {
		logger.debug(appendCustomizePrefixes(msg));
	}

	@Override
	public void debug(String format, Object arg) {
		logger.debug(appendCustomizePrefixes(format), arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(appendCustomizePrefixes(format), arg1, arg2);
	}

	@Override
	public void debug(String format, Object... arguments) {
		logger.debug(appendCustomizePrefixes(format), arguments);
	}

	@Override
	public void debug(String msg, Throwable t) {
		logger.debug(appendCustomizePrefixes(msg), t);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public void info(String msg) {
		logger.info(appendCustomizePrefixes(msg));
	}

	@Override
	public void info(String format, Object arg) {
		logger.info(appendCustomizePrefixes(format), arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		logger.info(appendCustomizePrefixes(format), arg1, arg2);
	}

	@Override
	public void info(String format, Object... arguments) {
		logger.info(appendCustomizePrefixes(format), arguments);
	}

	@Override
	public void info(String msg, Throwable t) {
		logger.info(appendCustomizePrefixes(msg), t);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public void warn(String msg) {
		logger.warn(appendCustomizePrefixes(msg), msg);
	}

	@Override
	public void warn(String format, Object arg) {
		logger.warn(appendCustomizePrefixes(format), arg);
	}

	@Override
	public void warn(String format, Object... arguments) {
		logger.warn(appendCustomizePrefixes(format), arguments);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(appendCustomizePrefixes(format), arg1, arg2);
	}

	@Override
	public void warn(String msg, Throwable t) {
		logger.warn(appendCustomizePrefixes(msg), t);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void error(String msg) {
		logger.error(appendCustomizePrefixes(msg));
	}

	@Override
	public void error(String format, Object arg) {
		logger.error(appendCustomizePrefixes(format), arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		logger.error(appendCustomizePrefixes(format), arg1, arg2);
	}

	@Override
	public void error(String format, Object... arguments) {
		logger.error(appendCustomizePrefixes(format), arguments);
	}

	@Override
	public void error(String msg, Throwable t) {
		logger.error(appendCustomizePrefixes(msg), t);
	}

}
