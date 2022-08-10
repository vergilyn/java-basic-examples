package com.vergilyn.examples.guava.eventbus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * EventBus是Guava的事件处理机制，是设计模式中的观察者模式（生产/消费者编程模型）的优雅实现。
 * https://www.jianshu.com/p/4bddd45a8e7a
 * https://www.jianshu.com/p/93486a604c34
 * https://blog.csdn.net/wangdong5678999/article/details/80561198
 * @author vergilyn
 * @date 2020-02-20
 */
public class EventBusTests {

    /**
     * 1. guava-EventBus 默认是由 push-event-thread 去调用 event-listener。（即 sync）
     */
    @Test
    public void test() throws InterruptedException {
        // 定义一个EventBus对象，这里的Joker是该对象的id
        EventBus eventBus = new EventBus("vergilyn");

        // 向上述EventBus对象中注册一个监听对象
        // 可以发现参数是Object，而不是实现/继承某个类。主要的核心是 @Subscribe 注解
        eventBus.register(new EventListener());

        Executors.newSingleThreadExecutor().submit(() -> {
            println("prepare eventBus#post() >>>> " + LocalTime.now().toString());
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // do nothing
            }

            // 使用EventBus发布一个事件，该事件会给通知到所有注册的监听者
            // 由 current-thread 调用 event-listener。
            eventBus.post(new Event("EventBus, hello world!"));

            println("complete eventBus#post() >>>> " + LocalTime.now().toString());
        });

        // 由 main-thread 调用 Eevent-listener
        eventBus.post(new Event("main-thread push event."));

        new Semaphore(0).tryAcquire(10, TimeUnit.SECONDS);  // 阻止JVM退出
    }

    // 事件，监听者监听的事件的包装对象
    class Event {
        public String message;
        Event(String message) {
            this.message = message;
        }
    }

    // 监听者
    class EventListener {
        // 监听的方法，必须使用注解声明，且只能有一个参数，实际触发一个事件的时候会根据参数类型触发方法
        @Subscribe
        public void listen(Event event) {
            printf("EventListener, %s \r\n", event.message);
        }
    }

    protected void println(String msg){
        System.out.println(logPrefix() + msg);
    }

    protected void printf(String format, Object... args){
        System.out.printf(logPrefix() + format, args);
    }

    protected String logPrefix(){
        return String.format("[vergilyn][%s][%s] >>>> ", LocalTime.now(), Thread.currentThread().getName());
    }
}
