package com.vergilyn.examples.usage.logger.message.dubbo.support;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLoggerContextFactory;

public class InheritableThreadLocalMsgLoggerContextFactory implements MsgLoggerContextFactory {
    private static final InheritableThreadLocal<NormMsgLoggerContext> CONTEXT = new InheritableThreadLocal<>();

    @Override
    public NormMsgLoggerContext getContext(){
        NormMsgLoggerContext context = CONTEXT.get();
        if (context != null){
            return context;
        }

        synchronized (CONTEXT){
            context = CONTEXT.get();

            if (context == null){
                context = new NormMsgLoggerContext();
                CONTEXT.set(context);
            }
        }

        return context;
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
