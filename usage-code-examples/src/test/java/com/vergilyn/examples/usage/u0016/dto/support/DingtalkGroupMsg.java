package com.vergilyn.examples.usage.u0016.dto.support;

import com.google.common.collect.Lists;
import com.vergilyn.examples.usage.u0016.dto.AbstractMsg;
import com.vergilyn.examples.usage.u0016.dto.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class DingtalkGroupMsg extends AbstractMsg<DingtalkGroupMsg.WebhookWrapper> {

    private String title;

    @Override
    public MessageTypeEnum getType() {
        return MessageTypeEnum.DINGTALK_GROUP;
    }

    @Override
    public List<String> fetchReceivers() {
        WebhookWrapper webhookWrapper = this.getTo();
        if (webhookWrapper == null){
            return Lists.newArrayList();
        }

        List<Webhook> webhooks = webhookWrapper.getWebhooks();
        if (webhooks == null || webhooks.isEmpty()){
            return Lists.newArrayList();
        }

        return webhooks.stream().map(Webhook::getAccessToken).collect(Collectors.toList());
    }

    @Override
    public AbstractMsg removeReceivers(List<String> removeReceivers) {

        return null;
    }


    @Data
    public static class WebhookWrapper implements Serializable {
        private List<Webhook> webhooks;
    }

    @Data
    public static class Webhook implements Serializable {
        private String accessToken;

        private String secret;

        private String name;
    }
}
