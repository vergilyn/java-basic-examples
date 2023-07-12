package com.vergilyn.examples.usage.logger.message.dubbo.log4j2;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLogger;
import com.vergilyn.examples.usage.logger.message.dubbo.MsgLoggerAdapter;
import org.apache.logging.log4j.LogManager;

public class Log4j2MsgLoggerAdapter implements MsgLoggerAdapter {
    public static final String NAME = "log4j2";

    @Override
    public MsgLogger getMsgLogger(Class<?> key) {
        return new Log4j2MsgLogger(LogManager.getLogger(key));
    }

    @Override
    public MsgLogger getMsgLogger(String key) {
        return new Log4j2MsgLogger(LogManager.getLogger(key));
    }
}
