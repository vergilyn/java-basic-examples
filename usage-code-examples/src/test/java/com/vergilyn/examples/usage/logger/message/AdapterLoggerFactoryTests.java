package com.vergilyn.examples.usage.logger.message;

import com.vergilyn.examples.usage.logger.message.adapter.MsgLoggerAdapterFactory;
import com.vergilyn.examples.usage.logger.message.adapter.NormMsgLogger;
import com.vergilyn.examples.usage.logger.message.adapter.NormMsgLoggerContext;
import com.vergilyn.examples.usage.logger.message.dubbo.MsgLoggerFactory;
import lombok.SneakyThrows;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class AdapterLoggerFactoryTests {
    private static final NormMsgLogger<Logger> msgLogger = MsgLoggerAdapterFactory.getLoggerSlf4j(AdapterLoggerFactoryTests.class);

    @Test
    void test(){
        try (LogCaptor logCaptor = LogCaptor.forClass(this.getClass())) {

            String code = ThreadLocalRandom.current().nextInt(10000, 99999) + "";
            Long bizNo = Long.parseLong(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))) * 1000 + 1;

            NormMsgLoggerContext context = msgLogger.getContext();
            context.setTemplateCode(code);
            context.setBizNo(bizNo);

            msgLogger.info("test");

            assertThat(logCaptor.getLogs())
                    .hasSize(1)
                    .contains("[code-" + code + "][bizNo-" + bizNo + "] test");
        }
    }

    @SneakyThrows
    @Test
    void testThreadLocal(){
        try (LogCaptor logCaptor = LogCaptor.forClass(this.getClass())) {
            NormMsgLoggerContext context = msgLogger.getContext();
            context.setTemplateCode("main");
            context.setBizNo(202307110001L);

            msgLogger.info("主线程 BEGIN");

            CountDownLatch countDownLatch = new CountDownLatch(5);
            AtomicInteger index = new AtomicInteger();
            ExecutorService threadPool = Executors.newFixedThreadPool(2);
            for (int i = 0; i < countDownLatch.getCount(); i++) {
                threadPool.submit(() -> {

                    try {
                        // 未保证线程安全，所以 2个子线程间会相互影响。
                        msgLogger.setContext(context);
                        context.setTemplateCode("thead" + index.getAndIncrement());

                        // 由于本测试代码存在2个子线程，且都指向同一个 context
                        // 所以可能存在 thread-1 和 thread-2 都准备执行 `#info`。
                        // 此时，ThreadLocal中保存的 code 会是同一个。
                        // 例如，存在以下打印结果：
                        //   "[code-thead1][bizNo-202307110001] 子线程打印"
                        //   "[code-thead1][bizNo-202307110001] 子线程打印"
                        //   "[code-thead3][bizNo-202307110001] 子线程打印",
                        //   "[code-thead3][bizNo-202307110001] 子线程打印",
                        //   "[code-thead4][bizNo-202307110001] 子线程打印",
                        msgLogger.info("子线程打印");
                    }finally {
                        countDownLatch.countDown();
                        // 不会因为线程复用导致`context`被回收。
                        MsgLoggerFactory.clear();
                    }
                });
            }

            countDownLatch.await();
            msgLogger.info("主线程 END");
            MsgLoggerFactory.clear();

            assertThat(logCaptor.getInfoLogs())
                    .hasSize(7)
                    .contains(
                      "[code-main][bizNo-202307110001] 主线程 BEGIN");

        }
    }
}
