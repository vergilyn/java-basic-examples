package com.vergilyn.examples.usage.logger.message.dubbo.slf4j;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLogger;
import com.vergilyn.examples.usage.logger.message.dubbo.MsgLoggerAdapter;

public class Slf4jMsgLoggerAdapter implements MsgLoggerAdapter {
    public static final String NAME = "slf4j";

    @Override
    public MsgLogger getMsgLogger(Class<?> key) {
        return new Slf4jMsgLogger(org.slf4j.LoggerFactory.getLogger(key));
    }

    @Override
    public MsgLogger getMsgLogger(String key) {
        return new Slf4jMsgLogger(org.slf4j.LoggerFactory.getLogger(key));
    }
}
