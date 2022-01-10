package com.vergilyn.examples.future;

import java.time.LocalTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;

/**
 * seata-client中开启global-transaction时，用于"接收获取"seata-server返回的信息。
 *   - io.seata.core.protocol.MessageFuture
 *   - io.seata.core.rpc.netty.AbstractRpcRemoting#sendAsyncRequest
 *   - io.seata.core.rpc.netty.AbstractRpcRemotingClient#channelRead(...)
 *
 *
 * @author vergilyn
 * @date 2020-02-20
 * @see <a href="https://www.jianshu.com/p/220d05525f27">Future模式之CompletableFuture</a>
 */
public class CompletableFutureTestng {
    private CompletableFuture<Integer> future = new CompletableFuture<>();

    /* 大致就是：
     *   如果get时为null，那么会一直等待（直到timeout）。
     *   执行完future.complete(...)会去触发get！
     */
    @Test(invocationTimeOut = 10_000)
    public void seata() throws Exception {
        // future.complete(1024);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            future.complete(2048);
            System.out.println("after future#complete >>>> " + LocalTime.now().toString());
        }, 2, 60, TimeUnit.SECONDS);

        System.out.println("before future#get >>>> " + LocalTime.now().toString());

        while (true){
            try {
                Integer rs = future.get(5, TimeUnit.SECONDS);
                System.out.println("future#get >>>>" + rs);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("exception >>>> " + e.getMessage());
            }

            System.out.println("after future#get >>>> " + LocalTime.now().toString());

            System.exit(0);
        }
    }

}
