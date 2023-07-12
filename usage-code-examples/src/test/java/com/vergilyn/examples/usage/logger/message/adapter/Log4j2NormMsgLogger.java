package com.vergilyn.examples.usage.logger.message.adapter;

class Log4j2NormMsgLogger extends NormMsgLogger<org.apache.logging.log4j.Logger> {

    public Log4j2NormMsgLogger(org.apache.logging.log4j.Logger logger) {
        super(logger);
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public void info(String format, Object... arguments) {
        delegate.info(appendContextMessage(format), arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        delegate.info(appendContextMessage(msg), t);
    }

    @Override
    public void error(String format, Object... arguments) {
        delegate.error(appendContextMessage(format), arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        delegate.error(appendContextMessage(msg), t);
    }
}
