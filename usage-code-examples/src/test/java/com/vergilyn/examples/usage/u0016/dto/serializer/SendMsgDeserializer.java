package com.vergilyn.examples.usage.u0016.dto.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.google.common.collect.Lists;
import com.vergilyn.examples.usage.u0016.dto.AbstractMsg;
import com.vergilyn.examples.usage.u0016.dto.MessageTypeEnum;
import com.vergilyn.examples.usage.u0016.dto.SendMsg;

import java.lang.reflect.Type;

/**
 * <p> 1. 如果 {@link SendMsg} 中的 field's 有修改（新增、删除、修改），那么需要修改此反序列化代码。
 * <br/> 所以，更推荐只自定义 {@link SendMsg#body} 的反序列化方式。
 *
 * @author vergilyn
 * @since 2023-05-31
 *
 * @deprecated 不推荐此方式。请用 {@link SendMsg#body} 自定义反序列化来替换。参考：{@link MsgBodyDeserializer}
 */
public class SendMsgDeserializer implements ObjectDeserializer {

    @Override
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONObject jsonObject = parser.parseObject();

        // 如果 SendMsg 修改 field's，那么需要修改此`ObjectDeserializer`代码
        // 并且例如`bizNo`这部分反序列化，本来就可以默认。没必要通过编码来 set。
        SendMsg sendMsg = new SendMsg();
        sendMsg.setBizNo(jsonObject.getString("bizNo"));

        JSONArray body = jsonObject.getJSONArray("body");
        sendMsg.setBody(Lists.newArrayListWithCapacity(body.size()));
        for (int i = 0; i < body.size(); i++) {
            JSONObject item = (JSONObject) body.get(i);

            Integer typeCode = item.getInteger(AbstractMsg.KEY_TYPE);
            MessageTypeEnum messageTypeEnum = MessageTypeEnum.of(typeCode);
            AbstractMsg abstractMsg = item.toJavaObject(messageTypeEnum.getClazz());
            sendMsg.getBody().add(abstractMsg);
        }

        return (T) sendMsg;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
