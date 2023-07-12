package com.vergilyn.examples.usage.logger.message.dubbo;

import com.vergilyn.examples.usage.logger.message.ConcurrentHashMapUtils;
import com.vergilyn.examples.usage.logger.message.dubbo.log4j2.Log4j2MsgLoggerAdapter;
import com.vergilyn.examples.usage.logger.message.dubbo.slf4j.Slf4jMsgLoggerAdapter;
import com.vergilyn.examples.usage.logger.message.dubbo.support.NormMsgLogger;
import com.vergilyn.examples.usage.logger.message.dubbo.support.NormMsgLoggerContext;
import com.vergilyn.examples.usage.logger.message.dubbo.support.ThreadLocalMsgLoggerContextFactory;
import com.vergilyn.examples.usage.u0010.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Logger factory
 *
 * @author vergilyn
 * @since 2023-07-11
 *
 * @see <a href="https://github.com/apache/dubbo/blob/dubbo-3.2.0/dubbo-common/src/main/java/org/apache/dubbo/common/logger/LoggerFactory.java">
 *      dubbo-common, LoggerFactory.java</a>
 */
public class MsgLoggerFactory {
    private static final ConcurrentMap<String, NormMsgLogger> LOGGERS = new ConcurrentHashMap<>();
    private static volatile MsgLoggerAdapter loggerAdapter;
    public static MsgLoggerContextFactory CONTEXT_FACTORY = new ThreadLocalMsgLoggerContextFactory();

    static {
        String logger = System.getProperty("vergilyn.msg.logger", "");
        switch (logger) {
            case Slf4jMsgLoggerAdapter.NAME:
                setLoggerAdapter(new Slf4jMsgLoggerAdapter());
                break;
            case Log4j2MsgLoggerAdapter.NAME:
                setLoggerAdapter(new Log4j2MsgLoggerAdapter());
                break;
            default:
                List<Class<? extends MsgLoggerAdapter>> candidates = Arrays.asList(
                        Slf4jMsgLoggerAdapter.class,
                        Log4j2MsgLoggerAdapter.class
                );

                for (Class<? extends MsgLoggerAdapter> clazz : candidates) {
                    try {
                        setLoggerAdapter(clazz.newInstance());
                        break;
                    } catch (Throwable ignored) {
                    }
                }
        }
    }

    private MsgLoggerFactory() {
    }

    private static void setLoggerAdapter(MsgLoggerAdapter loggerAdapter) {
        if (loggerAdapter != null) {
            if (loggerAdapter == MsgLoggerFactory.loggerAdapter) {
                return;
            }
            loggerAdapter.getMsgLogger(LoggerFactory.class.getName());
            MsgLoggerFactory.loggerAdapter = loggerAdapter;
            for (Map.Entry<String, NormMsgLogger> entry : LOGGERS.entrySet()) {
                entry.getValue().setMsgLogger(MsgLoggerFactory.loggerAdapter.getMsgLogger(entry.getKey()));
            }
        }
    }

    public static MsgLogger getLogger(Class<?> key) {
        return ConcurrentHashMapUtils.computeIfAbsent(LOGGERS, key.getName(), name -> new NormMsgLogger(loggerAdapter.getMsgLogger(name)));
    }

    public static MsgLogger getLogger(String key) {
        return ConcurrentHashMapUtils.computeIfAbsent(LOGGERS, key, k -> new NormMsgLogger(loggerAdapter.getMsgLogger(k)));
    }

    public static NormMsgLoggerContext getContext(){
        return CONTEXT_FACTORY.getContext();
    }

    public static void setContext(NormMsgLoggerContext context){
        CONTEXT_FACTORY.setContext(context);
    }

    public static void clear(){
        CONTEXT_FACTORY.clear();
    }
}
