package com.vergilyn.examples.usage.logger.message.dubbo;

import com.vergilyn.examples.usage.logger.message.dubbo.support.NormMsgLoggerContext;

public interface MsgLoggerContextFactory {

    NormMsgLoggerContext getContext();

    void setContext(NormMsgLoggerContext context);

    void clear();
}
