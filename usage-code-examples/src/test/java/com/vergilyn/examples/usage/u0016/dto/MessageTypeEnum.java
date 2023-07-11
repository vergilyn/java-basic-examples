package com.vergilyn.examples.usage.u0016.dto;


import com.vergilyn.examples.usage.u0016.dto.support.DingtalkGroupMsg;
import com.vergilyn.examples.usage.u0016.dto.support.MobileMsg;

public enum MessageTypeEnum {

    SMS(100, "短信", "发送通道", MobileMsg.class),
    DINGTALK_GROUP(141, "钉钉群消息", "钉钉消息", DingtalkGroupMsg.class),
    ;

    private final Integer code;
    private final String name;
    private final String typeName;
    private final Class<? extends AbstractMsg> clazz;

    MessageTypeEnum(Integer key, String value, String typeName, Class<? extends AbstractMsg> clazz) {
        this.code = key;
        this.name = value;
        this.typeName = typeName;
        this.clazz = clazz;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public Class<? extends AbstractMsg> getClazz() {
        return clazz;
    }

    public static MessageTypeEnum of(Integer key) {
        if (key == null) {
            return null;
        }

        for (MessageTypeEnum value : MessageTypeEnum.values()) {
            if (value.getCode().compareTo(key) == 0) {
                return value;
            }
        }
        return null;
    }

    public static String getName(Integer code) {
        MessageTypeEnum of = of(code);
        return of == null ? null : of.getName();
    }


}
