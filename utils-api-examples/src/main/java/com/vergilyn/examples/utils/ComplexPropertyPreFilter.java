package com.vergilyn.examples.utils;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;

import org.apache.commons.lang3.ArrayUtils;

/**
 * {@linkplain #excludes}优先级高于{@linkplain #includes};
 * <p>参考：<a href="https://www.cnblogs.com/sandyfog/articles/3679804.html">fastjson 多级联属性过滤</a>
 * @see com.alibaba.fastjson.serializer.SimplePropertyPreFilter
 */
public class ComplexPropertyPreFilter implements PropertyPreFilter {
    private Map<Class<?>, String[]> includes = new HashMap<>();
    private Map<Class<?>, String[]> excludes = new HashMap<>();

    public ComplexPropertyPreFilter() {
    }

    public ComplexPropertyPreFilter(Map<Class<?>, String[]> includes) {
        super();
        this.includes = includes;
    }

    @Override
    public boolean apply(JSONSerializer serializer, Object source, String name) {
        //对象为空。直接放行
        if (source == null) {
            return true;
        }

        // 获取当前需要序列化的对象的类对象
        Class<?> clazz = source.getClass();

        // 过滤不需要的属性
        for (Map.Entry<Class<?>, String[]> item : this.excludes.entrySet()) {
            // isAssignableFrom()，判断类型间是否有继承关系
            if (item.getKey().isAssignableFrom(clazz)) {
                String[] excludeFields = item.getValue();
                if (ArrayUtils.contains(excludeFields, name)) {
                    return false;
                }
            }
        }

        if (this.includes.isEmpty()) {
            return true;
        }

        for (Map.Entry<Class<?>, String[]> item : this.includes.entrySet()) {
            if (item.getKey().isAssignableFrom(clazz)) {
                String[] includeFields = item.getValue();
                if (ArrayUtils.contains(includeFields, name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Class<?>, String[]> getIncludes() {
        return includes;
    }

    public void setIncludes(Map<Class<?>, String[]> includes) {
        this.includes = includes;
    }

    public Map<Class<?>, String[]> getExcludes() {
        return excludes;
    }

    public void setExcludes(Map<Class<?>, String[]> excludes) {
        this.excludes = excludes;
    }

}