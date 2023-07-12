package com.vergilyn.examples.usage.logger.message;

import org.junit.jupiter.api.condition.JRE;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class ConcurrentHashMapUtils {

    /**
     * A temporary workaround for Java 8 ConcurrentHashMap#computeIfAbsent specific performance issue: JDK-8161372.</br>
     *
     * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
     */
    public static <K, V> V computeIfAbsent(ConcurrentMap<K, V> map, K key, Function<? super K, ? extends V> func) {
        if (JRE.JAVA_8.isCurrentVersion()) {
            V v = map.get(key);
            if (null == v) {
                v = map.computeIfAbsent(key, func);
            }
            return v;
        } else {
            return map.computeIfAbsent(key, func);
        }
    }

}
