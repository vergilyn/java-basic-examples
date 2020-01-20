package com.vergilyn.examples;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * 验证：{@linkplain java.util.HashMap#putAll(Map)} 内存占用情况。<br/>
 * @author VergiLyn
 * @date 2020-01-20
 */
@Slf4j
public class HashMapPutAllTestng extends AbstractTestng {

    @Test
    public void putAll() {
        System.gc();  // 手动执行GC，清除调用测试方法前的临时对象
        sleep(1, "before init `map`, and manual invoke `System.gc()`");

        int size = 10_0000;
        log.info("begin init `map` >>>> size: {}", size);
        HashMap<Integer, String> map = new HashMap<>(size);
        for(int i = 0; i < size; i++){
            map.put(i, UUID.randomUUID().toString());
        }

        System.gc();  // 手动执行1次GC，清除init产生的临时对象
        sleep(1, "`map` init success, and manual invoke `System.gc()`, map.size: " + map.size());

        log.info("begin putAll() >>>> ");
        HashMap<Integer, String> putAllMap = new HashMap<>(size);
        putAllMap.putAll(map);

        sleep(1, "HashMap.putAll(...) success, putAllMap.size: " + putAllMap.size());
    }

    @Test
    public void test02() {
        System.gc();  // 手动执行GC，清除调用测试方法前的临时对象
        sleep(1, "before init `map`, and manual invoke `System.gc()`");

        int size = 10_0000;
        log.info("begin init `map` >>>> size: {}", size);
        HashMap<Integer, String> map = new HashMap<>(size);
        for(int i = 0; i < size; i++){
            map.put(i, UUID.randomUUID().toString());
        }

        System.gc();  // 手动执行1次GC，清除init产生的临时对象
        sleep(1, "`map` init success, and manual invoke `System.gc()`, map.size: " + map.size());

        log.info("begin iterator map#put >>>> ");
        HashMap<Integer, String> putAllMap = new HashMap<>(size);
        for (Map.Entry<Integer, String> e : map.entrySet()) {
            putAllMap.put(new Integer(e.getKey()), e.getValue());
        }

        System.gc();
        sleep(1, "iterator HashMap.put(...) success, and manual invoke `System.gc()`, putAllMap.size: " + putAllMap.size());
    }
}
