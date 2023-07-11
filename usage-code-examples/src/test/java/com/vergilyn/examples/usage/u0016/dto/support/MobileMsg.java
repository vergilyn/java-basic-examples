package com.vergilyn.examples.usage.u0016.dto.support;

import com.vergilyn.examples.usage.u0016.dto.AbstractMsg;
import com.vergilyn.examples.usage.u0016.dto.MessageTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

public class MobileMsg extends AbstractMsg<MobileMsg.SmsReceiver> {

    @Override
    public MessageTypeEnum getType() {
        return MessageTypeEnum.SMS;
    }

    @Override
    public List<String> fetchReceivers() {
        return null;
    }

    @Override
    public AbstractMsg removeReceivers(List<String> removeReceivers) {
        return null;
    }

    @Data
    public static class SmsReceiver implements Serializable {
        private List<String> mobiles;
    }

}
