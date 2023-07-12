package com.vergilyn.examples.usage.logger.message.dubbo.log4j2;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLogger;

public class Log4j2MsgLogger implements MsgLogger {

    private final org.apache.logging.log4j.Logger logger;

    public Log4j2MsgLogger(org.apache.logging.log4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(msg, t);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(msg, t);
    }
}
