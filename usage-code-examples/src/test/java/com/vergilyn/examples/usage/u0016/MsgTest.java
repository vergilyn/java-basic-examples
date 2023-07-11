package com.vergilyn.examples.usage.u0016;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.vergilyn.examples.usage.u0016.dto.SendMsg;
import com.vergilyn.examples.usage.u0016.dto.support.DingtalkGroupMsg;
import com.vergilyn.examples.usage.u0016.dto.support.MobileMsg;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.fastjson.serializer.SerializerFeature.DisableCircularReferenceDetect;
import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static org.assertj.core.api.Assertions.assertThat;

public class MsgTest {

    /**
     * 枚举的序列化使用CODE。  反序列化时也从CODE反序列化到枚举。
     */
    @Test
    SendMsg build(){

        Map<String, String> param = new HashMap<>();
        param.put("key1", "value1");
        param.put("key2", "value2");
        MobileMsg.SmsReceiver smsReceiver = new MobileMsg.SmsReceiver();
        smsReceiver.setMobiles(Lists.newArrayList("123", "456"));

        MobileMsg mobileMsg01 = new MobileMsg();
        mobileMsg01.setParam(param);
        mobileMsg01.setTo(smsReceiver);

        smsReceiver = new MobileMsg.SmsReceiver();
        smsReceiver.setMobiles(Lists.newArrayList("789", "000"));
        MobileMsg mobileMsg02 = new MobileMsg();
        mobileMsg02.setParam(param);
        mobileMsg02.setTo(smsReceiver);

        DingtalkGroupMsg dingtalkGroupMsg = new DingtalkGroupMsg();
        DingtalkGroupMsg.Webhook webhook = new DingtalkGroupMsg.Webhook();
        webhook.setAccessToken("access-token-0001");
        webhook.setSecret("secret-0001");
        webhook.setName("name-0001");
        DingtalkGroupMsg.WebhookWrapper webhookWrapper = new DingtalkGroupMsg.WebhookWrapper();
        webhookWrapper.setWebhooks(Lists.newArrayList(webhook));
        dingtalkGroupMsg.setParam(param);
        dingtalkGroupMsg.setTo(webhookWrapper);

        SendMsg sendMsg = new SendMsg();
        sendMsg.setBizNo("biz-no-0001");
        sendMsg.setBody(Lists.newArrayList(mobileMsg01, mobileMsg02, dingtalkGroupMsg));

        return sendMsg;
    }

    @Test
    void des(){
        SendMsg origin = build();

        String mobileJson = JSON.toJSONString(origin, PrettyFormat, DisableCircularReferenceDetect);
        // ParserConfig.getGlobalInstance().putDeserializer(SendMsg.class, deserializer);

        SendMsg des = JSON.parseObject(mobileJson, SendMsg.class);

        assertThat(des.getBizNo()).isEqualTo(origin.getBizNo());

        assertThat(des.getBody())
                .usingElementComparator((o1, o2) -> EqualsBuilder.reflectionEquals(o1, o2) ? 0 : 1)
                .isEqualTo(origin.getBody());

    }

}
