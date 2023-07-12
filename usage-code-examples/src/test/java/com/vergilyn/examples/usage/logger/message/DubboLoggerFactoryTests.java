package com.vergilyn.examples.usage.logger.message;

import com.vergilyn.examples.usage.logger.message.dubbo.MsgLogger;
import com.vergilyn.examples.usage.logger.message.dubbo.MsgLoggerFactory;
import com.vergilyn.examples.usage.logger.message.dubbo.support.InheritableThreadLocalMsgLoggerContextFactory;
import com.vergilyn.examples.usage.logger.message.dubbo.support.NormMsgLoggerContext;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <h3>2023-07-11</h3>
 * 对比{@linkplain ThreadLocal} 和 {@linkplain InheritableThreadLocal}，inheritable在线程复用时，反而不如ThreadLocal容易理解。<br/>
 * 所以，相对推荐ThreadLocal的方式（跟 {@link org.slf4j.MDC} 一样。）
 *
 * <pre> ChatGPT
 *  MDC的设计目标是为每个线程提供一个独立的上下文，而不是让子线程继承父线程的上下文。
 *
 *  ThreadLocal为每个线程提供了一个独立的变量副本，这样每个线程都可以独立地改变自己的副本，而不会影响其他线程。
 *  这对于实现线程局部变量非常有用，因为它可以避免线程间的数据竞争。
 *
 *  而InheritableThreadLocal则允许子线程继承父线程的值。这在某些情况下可能是有用的，例如你希望在一个线程中设置一些上下文信息，然后在所有的子线程中都可以访问这些信息。
 *  但是，这并不适用于所有的情况，尤其是在使用线程池的情况下，因为线程池中的线程是被复用的，所以你不能假设一个线程总是在同一个上下文中执行。
 *
 *  因此，MDC选择了基于ThreadLocal，而不是InheritableThreadLocal。
 *  这样，每个线程都可以有自己独立的MDC，而不需要考虑线程间的上下文继承问题。
 * </pre>
 *
 * @author vergilyn
 * @since 2023-07-11
 */
public class DubboLoggerFactoryTests {
    private static final MsgLogger msgLogger = MsgLoggerFactory.getLogger(DubboLoggerFactoryTests.class);

    @Test
    void test(){
        NormMsgLoggerContext context = MsgLoggerFactory.getContext();
        context.setTemplateCode("123456");
        context.setBizNo(202307110001L);

        msgLogger.info("test");
    }

    /**
     * 由于`ThreadLocal`不会主动从 父线程传递到子线程
     * <pre>
     *   1. 需要通过代码显式传递，例如以下代码中的 显式传递`context`。（备注：根据实际场景，需要考虑是传递 `context` 还是 `context.clone()`）
     *   2. 子线程使用完时，需要主动调用{@linkplain ThreadLocal#remove()}
     * </pre>
     *
     * <h3>2023-07-11</h3>
     * 目前更推荐此实现方式。虽然需要多写一些代码，但相对更容易理解，且适应更多的情况。（对比 InheritableThreadLocal）
     */
    @SneakyThrows
    @Test
    void testThreadLocal(){
        NormMsgLoggerContext context = MsgLoggerFactory.getContext();
        context.setTemplateCode("main");
        context.setBizNo(202307110001L);

        msgLogger.info("主线程 BEGIN");

        CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < countDownLatch.getCount(); i++) {
            threadPool.submit(() -> {

                try {
                    // 根据实际情况，考虑是 context 还是 context.clone()
                    // 1) 如果是 context，意味着 父子线程使用的是同一个对象。所以子线程内的修改，会影响到主线程。
                    //    例如 子线程可能通过`context.attachments`打印额外的参数，但不期望后续主线程打印，
                    //    此时可以考虑`context.clone()`，或者 子线程退出时移`attachments.remove(key)`
                    MsgLoggerFactory.setContext(context);
                    context.setTemplateCode("thead" + ThreadLocalRandom.current().nextInt(10, 99));
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
    }

    /**
     * <pre>ChatGPT:
     *     如果你的应用需要在父线程和子线程之间共享数据，那么可能需要考虑使用其他的方式，例如通过参数传递数据，或者使用线程安全的数据结构。
     *     因为InheritableThreadLocal并不能很好地支持线程复用，特别是在使用线程池的情况下。
     * </pre>
     */
    @SneakyThrows
    @Test
    void testInheritableThreadLocal(){
        MsgLoggerFactory.CONTEXT_FACTORY = new InheritableThreadLocalMsgLoggerContextFactory();

        NormMsgLoggerContext context = MsgLoggerFactory.getContext();
        context.setTemplateCode("main");
        context.setBizNo(202307110001L);

        msgLogger.info("主线程 BEGIN");

        CountDownLatch countDownLatch = new CountDownLatch(5);
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        for (int i = 0; i < countDownLatch.getCount(); i++) {
            threadPool.submit(() -> {
                try {
                    // 不需要主动传递`context`
                    msgLogger.info("子线程打印");
                }finally {
                    countDownLatch.countDown();
                    // 当子线程调用remove()方法后，InheritableThreadLocal中存储的值会被清除.
                    // 然后，如果这个子线程被复用（例如，在一个线程池中），并且再次尝试获取InheritableThreadLocal的值，
                    // 那么可能会获取不到预期的值，因为这个值已经在之前被清除了。
                    MsgLoggerFactory.clear();
                }
            });
        }

        countDownLatch.await();
        msgLogger.info("主线程 END");
    }
}
