package com.vergilyn.examples.usage.u0010.slf4j;

import com.vergilyn.examples.usage.u0010.Logger;
import com.vergilyn.examples.usage.u0010.LoggerAdapter;

public class Slf4jLoggerAdapter implements LoggerAdapter {

    @Override
    public Logger getLogger(String key) {
        return new Slf4jLoggerWrapper(org.slf4j.LoggerFactory.getLogger(key));
    }

    @Override
    public Logger getLogger(Class<?> key) {
        return new Slf4jLoggerWrapper(org.slf4j.LoggerFactory.getLogger(key));
    }

}
