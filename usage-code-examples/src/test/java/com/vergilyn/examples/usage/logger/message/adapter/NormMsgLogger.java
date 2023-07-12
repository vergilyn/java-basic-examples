package com.vergilyn.examples.usage.logger.message.adapter;

import java.util.Map;
import java.util.StringJoiner;

public abstract class NormMsgLogger<L> implements MsgLogger<L>{
    private static final ThreadLocal<NormMsgLoggerContext> CONTEXT = ThreadLocal.withInitial(NormMsgLoggerContext::new);

    protected L delegate;

    public NormMsgLogger(L delegate) {
        this.delegate = delegate;
    }

    @Override
    public L getDelegate() {
        return delegate;
    }

    public NormMsgLoggerContext getContext(){
        return CONTEXT.get();
    }

    public void setContext(NormMsgLoggerContext context){
        CONTEXT.set(context);
    }

    public void clear() {
        CONTEXT.remove();
    }


    /**
     * <h3>备注</h3>
     * 1) 为了避免日志过大，所以应该避免类似`code-`或`bizNo`这类前缀。
     * 2) 如果为了方便日志解析（例如 logstash），那么最好让日志保留结构化。比如，`messageType`始终用`[]`占位。
     */
    protected String appendContextMessage(String msg) {
        NormMsgLoggerContext context = getContext();

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
}
