# logger


## dubbo
例如`dubbo: 3.2.0`打印的日子
```log
[DUBBO] store provider metadata. Identifier : ......, dubbo version: 3.0.11, current host: 127.0.0.1
```
1) 固定包含前缀："\[DUBBO]"；
2) 固定包含后缀："dubbo version: 3.0.11, current host: 127.0.0.1"

**实现原理：**  
- `org.apache.dubbo.common.logger.LoggerFactory`
- `org.apache.dubbo.common.logger.support.FailsafeLogger`

```java
package org.apache.dubbo.common.logger.support;

import org.apache.dubbo.common.logger.Logger;

public class FailsafeLogger implements Logger {
    private Logger logger;

    private static boolean disabled = false;

    public FailsafeLogger(Logger logger) {
        this.logger = logger;
    }


    private String appendContextMessage(String msg) {
        return " [DUBBO] " + msg + ", dubbo version: " + Version.getVersion() + ", current host: " + NetUtils.getLocalHost();
    }

    @Override
    public void info(String msg, Throwable e) {
        if (disabled) {
            return;
        }
        try {
            logger.info(appendContextMessage(msg), e);
        } catch (Throwable t) {
        }
    }
}
```

## 需求场景
例如根据**模板Code**发送不同类型的消息时，期望日志的格式是：
```text
[模板Code][消息业务编号][消息类型][扩展xxx] ......

消息类型：可能是在某一步之后才有
扩展xxx：例如订单类消息，可能都存在`订单号`。此时，可以体现在消息日志中，方便根据`订单号`查询消息发送的日志。
```

需要考虑的问题：  
1) 子线程  
2) 线程池服用线程  