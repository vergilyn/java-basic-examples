package com.vergilyn.examples.usage.logger.message.dubbo.slf4j;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLogger;

public class Slf4jMsgLogger implements MsgLogger {

    private final org.slf4j.Logger logger;

    public Slf4jMsgLogger(org.slf4j.Logger logger) {
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
        logger.warn(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.warn(msg, t);
    }
}
