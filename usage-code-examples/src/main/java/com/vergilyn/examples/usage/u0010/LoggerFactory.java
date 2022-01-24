package com.vergilyn.examples.usage.u0010;

import com.vergilyn.examples.usage.u0010.core.ThreadLocalCustomizePrefixesLogger;
import com.vergilyn.examples.usage.u0010.slf4j.Slf4jLoggerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Logger factory
 *
 * @author vergilyn
 * @since 2022-01-24
 *
 * @see <a href="https://github.com/apache/dubbo/blob/dubbo-3.0.5/dubbo-common/src/main/java/org/apache/dubbo/common/logger/LoggerFactory.java">
 *      dubbo-common, LoggerFactory.java</a>
 */
public class LoggerFactory {
    /**
     * key - classname, value - prefixesLogger
     */
    private static final ConcurrentMap<String, CustomizePrefixesLogger> LOGGERS = new ConcurrentHashMap<>();
    private static volatile LoggerAdapter LOGGER_ADAPTER;

    // search common-used logging frameworks
    static {
        String logger = System.getProperty("vergilyn.application.logger", "");
        switch (logger) {
            case "slf4j":
                setLoggerAdapter(new Slf4jLoggerAdapter());
                break;
            /* case "log4j":
                setLoggerAdapter(new Log4jLoggerAdapter());
                break;
            case "jdk":
                setLoggerAdapter(new JdkLoggerAdapter());
                break;
            case "log4j2":
                setLoggerAdapter(new Log4j2LoggerAdapter());
                break; */
            default:
                List<Class<? extends LoggerAdapter>> candidates = Arrays.asList(
                        Slf4jLoggerAdapter.class
                        /* , Log4jLoggerAdapter.class,
                        JdkLoggerAdapter.class,
                        Log4j2LoggerAdapter.class */
                );
                for (Class<? extends LoggerAdapter> clazz : candidates) {
                    try {
                        setLoggerAdapter(clazz.newInstance());
                        break;
                    } catch (Throwable ignored) {
                    }
                }
        }
    }

    private LoggerFactory() {
    }


    /**
     * Set logger provider
     *
     * <p>TODO 2022-01-24，这个的目的是什么？</p>
     *
     * @param loggerAdapter logger provider
     */
    public static void setLoggerAdapter(LoggerAdapter loggerAdapter) {
        if (loggerAdapter != null) {
            if (loggerAdapter == LOGGER_ADAPTER) {
                return;
            }
            loggerAdapter.getLogger(LoggerFactory.class.getName());
            LoggerFactory.LOGGER_ADAPTER = loggerAdapter;
            for (Map.Entry<String, CustomizePrefixesLogger> entry : LOGGERS.entrySet()) {
                entry.getValue().setLogger(LOGGER_ADAPTER.getLogger(entry.getKey()));
            }
        }
    }

    /**
     * Get logger provider
     *
     * @param key the returned logger will be named after clazz
     * @return logger
     */
    public static CustomizePrefixesLogger getLogger(Class<?> key) {
        return LOGGERS.computeIfAbsent(key.getName(), name -> new ThreadLocalCustomizePrefixesLogger(LOGGER_ADAPTER.getLogger(name)));
    }

    /**
     * Get logger provider
     *
     * @param key the returned logger will be named after key
     * @return logger provider
     */
    public static CustomizePrefixesLogger getLogger(String key) {
        return LOGGERS.computeIfAbsent(key, k -> new ThreadLocalCustomizePrefixesLogger(LOGGER_ADAPTER.getLogger(k)));
    }
}
