package com.vergilyn.examples.usage.u0016.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.vergilyn.examples.usage.u0016.dto.serializer.MessageTypeEnumSerializer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMsg<T> implements ReceiverContext, SupportFailoverMessage, Serializable {
    public static final String KEY_TYPE = "type";

    @JSONField(name = KEY_TYPE, serializeUsing = MessageTypeEnumSerializer.class)
    public abstract MessageTypeEnum getType();

    private Map<String, String> param = new HashMap<>();

    private T to;

    private SendMsg failoverMessage;

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    public T getTo() {
        return to;
    }

    public void setTo(T to) {
        this.to = to;
    }

    public SendMsg getFailoverMessage() {
        return failoverMessage;
    }

    public void setFailoverMessage(SendMsg failoverMessage) {
        this.failoverMessage = failoverMessage;
    }

    @Override
    public SendMsg fetchFailoverMessage() {
        return getFailoverMessage();
    }
}
