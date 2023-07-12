package com.vergilyn.examples.usage.logger.message.adapter;

/**
 *
 * @author vergilyn
 * @since 2023-07-11
 *
 * @see org.slf4j.Logger
 */
interface MsgLogger<L> {

    public L getDelegate();
    public String getName();

    public void info(String format, Object... arguments);
    public void info(String msg, Throwable t);

    public void error(String format, Object... arguments);
    public void error(String msg, Throwable t);
}
