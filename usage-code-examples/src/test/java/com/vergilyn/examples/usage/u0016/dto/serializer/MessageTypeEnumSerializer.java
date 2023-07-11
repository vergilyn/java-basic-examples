package com.vergilyn.examples.usage.u0016.dto.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.vergilyn.examples.usage.u0016.dto.MessageTypeEnum;

import java.io.IOException;
import java.lang.reflect.Type;

public class MessageTypeEnumSerializer implements ObjectSerializer {

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
        } else {
            MessageTypeEnum messageTypeEnum = (MessageTypeEnum) object;
            out.writeInt(messageTypeEnum.getCode());
        }
    }
}
