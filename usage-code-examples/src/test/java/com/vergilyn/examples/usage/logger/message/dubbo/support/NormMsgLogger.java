package com.vergilyn.examples.usage.logger.message.dubbo.support;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLogger;
import com.vergilyn.examples.usage.logger.message.dubbo.MsgLoggerFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.StringJoiner;

public class NormMsgLogger implements MsgLogger {

    @Setter
    @Getter
    private MsgLogger msgLogger;


    public NormMsgLogger(MsgLogger msgLogger) {
        this.msgLogger = msgLogger;
    }

    /**
     * <h3>备注</h3>
     * 1) 为了避免日志过大，所以应该避免类似`code-`或`bizNo`这类前缀。
     * 2) 如果为了方便日志解析（例如 logstash），那么最好让日志保留结构化。比如，`messageType`始终用`[]`占位。
     */
    private String appendContextMessage(String msg) {
        NormMsgLoggerContext context = MsgLoggerFactory.getContext();

        StringJoiner joiner = new StringJoiner("][", "[", "]");
        joiner.add("code-" + context.getTemplateCode());
        joiner.add("bizNo-" + context.getBizNo());

        if (context.getMessageType() != null){
            joiner.add("type-" + context.getMessageType().name());
        }

        for (Map.Entry<String, String> entry : context.getAttachments().entrySet()) {
            joiner.add(entry.getKey() + ":" + entry.getValue());
        }

        return joiner + " " + msg;
    }

    @Override
    public void info(String format, Object... arguments) {
        msgLogger.info(appendContextMessage(format), arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        msgLogger.info(appendContextMessage(msg), t);
    }

    @Override
    public void error(String format, Object... arguments) {
        msgLogger.error(appendContextMessage(format), arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        msgLogger.error(appendContextMessage(msg), t);
    }
}
