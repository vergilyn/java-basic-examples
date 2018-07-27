package com.vergilyn.examples.utils;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Maps;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/25
 */
public class TransformUtils {

    /**
     * FIXME 内部类还是不大了解其特性，只是感觉把这个interface定义到内部比较好而已。
     */
    @FunctionalInterface
    public interface KeyGenerate<K, V> {
        K getKey(V v);
    }

    public static <K, V> LinkedHashMap<K, V> listToMap(List<V> list, KeyGenerate<K, V> keyGenerate){
        if (list == null || list.isEmpty()){
            return null;
        }

        LinkedHashMap<K, V> map = Maps.newLinkedHashMap();
        for (V v : list){
            map.put(keyGenerate.getKey(v), v);
        }

        return map;
    }

}
