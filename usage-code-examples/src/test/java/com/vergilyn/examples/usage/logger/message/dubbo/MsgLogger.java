package com.vergilyn.examples.usage.logger.message.dubbo;

/**
 *
 * @author vergilyn
 * @since 2023-07-11
 *
 * @see org.slf4j.Logger
 */
public interface MsgLogger {


    public void info(String format, Object... arguments);
    public void info(String msg, Throwable t);

    public void error(String format, Object... arguments);
    public void error(String msg, Throwable t);
}
