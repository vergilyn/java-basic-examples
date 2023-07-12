package com.vergilyn.examples.usage.logger.message.dubbo.support;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLoggerContextFactory;

public class ThreadLocalMsgLoggerContextFactory implements MsgLoggerContextFactory {
    private static final ThreadLocal<NormMsgLoggerContext> CONTEXT = ThreadLocal.withInitial(NormMsgLoggerContext::new);

    @Override
    public NormMsgLoggerContext getContext(){
        return CONTEXT.get();
    }

    @Override
    public void setContext(NormMsgLoggerContext context){
        CONTEXT.set(context);
    }

    @Override
    public void clear() {
        CONTEXT.remove();
    }
}
