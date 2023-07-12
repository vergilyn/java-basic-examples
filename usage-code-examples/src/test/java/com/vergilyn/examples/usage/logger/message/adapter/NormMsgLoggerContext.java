package com.vergilyn.examples.usage.logger.message.adapter;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NormMsgLoggerContext {
    @Getter
    @Setter
    private String templateCode;

    @Getter
    @Setter
    private Long bizNo;

    @Getter
    @Setter
    private MessageTypeEnum messageType;

    @Getter
    private final Map<String, String> attachments = new ConcurrentHashMap<>();

    public void addAttachment(String key, String value){
        attachments.put(key, value);
    }

    static enum MessageTypeEnum {
        SMS, EMAIL, DINGTALK;
    }
}
