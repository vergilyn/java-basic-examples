package com.vergilyn.examples.map;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TreeMapSortTests {

    /**
     * {@link TreeMap} 是一个基于红黑树实现的键值对映射，<b>按照键（Key）的自然顺序进行排序</b>
     */
    @Test
    void test(){
        AtomicInteger index = new AtomicInteger(0);

        SortedMap<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("A", index.incrementAndGet());
        treeMap.put("C", index.incrementAndGet());
        treeMap.put("B", index.incrementAndGet());

        print(treeMap);
    }

    void print(SortedMap<String, Integer> map){
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.printf("key: %s, value: %d \n", entry.getKey(), entry.getValue());
        }
    }
}
