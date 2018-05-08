package com.vergilyn.examples.enums;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeBeanInfo;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;

/**
 * 利用fastjson转换enum到json。
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/23
 */
public class EnumJsonUtil {

    /**
     * 枚举值完全转换成json
     * @param someEnum
     * @param <T>
     * @return
     */
    public static <T extends Enum> String toJson(T someEnum){
        SerializeConfig config = new SerializeConfig();
        config.configEnumAsJavaBean(someEnum.getClass());
        return JSON.toJSONString(someEnum, config);
    }

    /**
     * 枚举类全部值完全转换成json
     * @param enumClass
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static String toJson(Class<? extends Enum> enumClass) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method methodValues = enumClass.getMethod("values");
        Object invoke = methodValues.invoke(null);
        int length = java.lang.reflect.Array.getLength(invoke);
        List<Object> values = new ArrayList<Object>();
        for (int i=0; i<length; i++) {
            values.add(java.lang.reflect.Array.get(invoke, i));
        }

        SerializeConfig config = new SerializeConfig();
        config.configEnumAsJavaBean(enumClass);

        return JSON.toJSONString(values,config);
    }

    /**
     * 通过fastjson.jar获取class的FieldInfo。
     * @param enumClass
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static FieldInfo[] getFieldInfos(Class<? extends Enum> enumClass) throws NoSuchFieldException, IllegalAccessException {
        SerializeBeanInfo beanInfo = TypeUtils.buildBeanInfo(enumClass, null, null);
        Field field = beanInfo.getClass().getDeclaredField("fields");
        field.setAccessible(true);
        return (FieldInfo[]) field.get(beanInfo);
    }

    /**
     * @deprecated 无法构建枚举。
     * @param clazz
     * @return
     * @exception
     * Exception com.alibaba.fastjson.JSONException: default constructor not found. class com.vergilyn.examples.enums.SongsEnum
     */
    public static JavaBeanInfo buildJavaBeanInfo(Class<?> clazz){
        return JavaBeanInfo.build(clazz,null,null);
    }

    /**
     * JSONType: 貌似computeGetters()中并没有使用到。
     * <br>FieldCacheMap：不允许null,作为缓存使用,减少后面重新遍历查找。
     * @param clazz
     * @return
     */
    public static List<FieldInfo> getJavaBeanInfo(Class<?> clazz){
        JSONType jsonType = clazz.getAnnotation(JSONType.class);
        Map<String, Field> fieldCacheMap = new HashMap<String, Field>();
        ParserConfig.parserAllFieldToCache(clazz, fieldCacheMap);

        return TypeUtils.computeGetters(clazz,jsonType,null,fieldCacheMap,false,null);
    }


    public static void main(String[] args) throws Exception {
//        System.out.println(toJson(SongsEnum.SAFE_AND_SOUND));

        System.out.println(toJson(SongsEnum.BETTER_MAN));

        System.out.println(toJson(SongsEnum.class));

    }
}
