package com.vergilyn.examples.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.cglib.beans.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

/**
 * Map转换成JavaBean；Junit的执行效率是jackson基本要慢4-5倍，beanMap与gson差不多(gson可能速度更快)。 <br/>
 * 特别：需要注意{@link java.util.Date}的处理（以下代码没有考虑）；
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/6/11
 */
public class BeanMapUtils {
    /**
     * 实质是：先把Map转换成Json，再把Json转换成JavaBean；（fastjson是一样的）
     */
    public static <T> T gson(Object source, Class<T> clazz){
        Gson gson = new GsonBuilder()
             //   .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        return gson.fromJson(gson.toJson(source), clazz);
    }

    /**
     * 具体不清楚怎么实现的，参数支持Object，并不局限于Map；
     * 特别：若map中存在的key，在javabean中不存在，会报异常。
     */
    public static <T> T jackson(Object source, Class<T> clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(source, clazz);
    }

    /**
     * apache common-beanutils
     */
    public static <T> T apache(Map<String, Object>  source, Class<T> clazz){
        T bean = null;
        try {
            bean = clazz.newInstance();
            BeanUtils.populate(bean, source);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return bean;
    }


    /**
     * （建议用此方式）BeanMap在spring-core、cglib中都有定义；
     */
    public static <T> T mapToBean(Map<String, Object> source, Class<T> clazz){
        T bean = null;
        try {
            bean = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(source);
        return bean;
    }

    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }
}
