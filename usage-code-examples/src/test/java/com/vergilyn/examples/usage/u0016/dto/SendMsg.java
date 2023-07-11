package com.vergilyn.examples.usage.u0016.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.vergilyn.examples.usage.u0016.dto.serializer.MsgBodyDeserializer;

import java.util.List;

public class SendMsg {
    private String bizNo;

    @JSONField(deserializeUsing = MsgBodyDeserializer.class)
    private List<AbstractMsg> body;

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public List<AbstractMsg> getBody() {
        return body;
    }

    public void setBody(List<AbstractMsg> body) {
        this.body = body;
    }
}
