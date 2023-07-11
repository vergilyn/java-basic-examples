package com.vergilyn.examples.usage.u0016.dto.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.google.common.collect.Lists;
import com.vergilyn.examples.usage.u0016.dto.AbstractMsg;
import com.vergilyn.examples.usage.u0016.dto.MessageTypeEnum;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 主要需要考虑，如何根据 `type` 找到Java类型。
 * <p> 1. 最简单的方式还是 直接在 {@link MessageTypeEnum} 中维护。
 *   <br/> 这样的问题是，可能会导致 {@link MessageTypeEnum} "职责不单一"。
 *
 * <p> 2. 通过 Map 或者 Wrapper 来包装。
 *   <br/> 新增消息类型时 不友好，可能并不知道要添加此关系。
 *
 * @author vergilyn
 * @since 2023-05-31
 */
public class MsgBodyDeserializer implements ObjectDeserializer {
    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONArray jsonArray = (JSONArray) parser.parse();

        List<AbstractMsg> result = Lists.newArrayListWithCapacity(jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);

            // XXX 2023-05-31 消息类型 与 java类 的映射关系如何维护？
            Integer typeCode = item.getInteger(AbstractMsg.KEY_TYPE);
            MessageTypeEnum messageTypeEnum = MessageTypeEnum.of(typeCode);
            AbstractMsg abstractMsg = item.toJavaObject(messageTypeEnum.getClazz());
            result.add(abstractMsg);
        }

        return (T) result;
    }

    /*
     * 表示该反序列化器可以处理的 JSON 数据类型。这个整数值对应于 JSONToken 类中定义的常量。
     * JSONToken 类中定义了一组常量，用于表示 JSON 中的各种数据类型，例如：
     *
     * JSONToken.LITERAL_INT：整数值
     * JSONToken.LITERAL_FLOAT：浮点数值
     * JSONToken.LITERAL_STRING：字符串值
     * JSONToken.LBRACE：对象（以 { 开始）
     * JSONToken.LBRACKET：数组（以 [ 开始）
     */
    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACKET;
    }
}
