package com.vergilyn.examples.usage.logger.message.adapter;

import com.vergilyn.examples.usage.logger.message.ConcurrentHashMapUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO 2023-07-12 这样写与 dubbo3实现方式的区别是？
 *
 * 核心都是代理 logger。
 * 相比 dubbo的实现，只是把 `Slf4jMsgLogger & Slf4jMsgLoggerAdapter` 合并到了一起。
 * `MsgLoggerAdapterFactory.getLogger(org.slf4j.LoggerFactory.getLogger(key))`
 */
@SuppressWarnings("unchecked")
@Slf4j
public class MsgLoggerAdapterFactory {
    private static final ConcurrentMap<String, NormMsgLogger<?>> LOGGERS = new ConcurrentHashMap<>();

    public static NormMsgLogger<org.slf4j.Logger> getLoggerSlf4j(Class<?> clazz){
        return (NormMsgLogger<Logger>) ConcurrentHashMapUtils.computeIfAbsent(LOGGERS, clazz.getName(), name -> new Slf4jNormMsgLogger(org.slf4j.LoggerFactory.getLogger(name)));
    }

    public static NormMsgLogger<org.slf4j.Logger> getLogger(org.slf4j.Logger logger){
        return (NormMsgLogger<Logger>) ConcurrentHashMapUtils.computeIfAbsent(LOGGERS, logger.getName(), name -> new Slf4jNormMsgLogger(logger));
    }

    public static NormMsgLogger<org.apache.logging.log4j.Logger> getLogger(org.apache.logging.log4j.Logger logger){
        return (NormMsgLogger<org.apache.logging.log4j.Logger>) ConcurrentHashMapUtils.computeIfAbsent(LOGGERS, logger.getName(), name -> new Log4j2NormMsgLogger(logger));
    }

}
