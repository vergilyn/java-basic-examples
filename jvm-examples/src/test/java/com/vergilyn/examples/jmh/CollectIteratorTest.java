package com.vergilyn.examples.jmh;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.testng.collections.Lists;

/**
 * @author vergilyn
 * @date 2020-05-08
 *
 * @see CollectIteratorJMH
 */
public class CollectIteratorTest {

    private static final List<Integer> LIST = Stream.iterate(1, i -> i++)
                                                .limit(100_0000)
                                                .collect(Collectors.toList());

    private static final AtomicInteger COUNT = new AtomicInteger(0);

    /*
     * 总体上: for-i > for-each > stream
     * stream 预热后（即使不预热也可能），stream 趋近于 for-each，甚至优于for-each接近for-i。
     *
     *  LIST.size >>>> 1000000
     *  for-i >>>> 11 ms
     *  for-each >>>> 17 ms
     *  stream.forEach >>>> 11 ms
     *  stream.forEach(2) >>>> 16 ms
     *  count >>>> 4000000
     */
    public static void main(String[] args) {
        System.out.println("LIST.size >>>> " + LIST.size());

        // System.out.println("is warmup stream >>>> " + isWarmupStream(true));

        cost("for-i", () -> {
            for (int i = 0, len = LIST.size(); i < len; i++){
                doSomething();
            }
        });

        cost("for-each", () -> {
            for(Integer i : LIST){
                doSomething();
            }
        });

        cost("stream.forEach", () -> {
            LIST.stream().forEach(integer -> doSomething());
        });

        cost("stream.forEach(2)", () -> {
            LIST.stream().forEach(integer -> doSomething());
        });

        System.out.println("count >>>> " + COUNT.get());
    }

    private static boolean isWarmupStream(boolean warmup){
        if (!warmup){
            return warmup;
        }

        Lists.newArrayList("warmup-stream").stream().forEach(s -> {});
        return true;
    }

    protected static void cost(String mark, Invoker invoker){
        long begin = System.currentTimeMillis();
        invoker.exec();
        long end = System.currentTimeMillis();

        System.out.printf("%s >>>> %d ms \r\n", mark, end - begin);
    }

    public static void doSomething(){
        COUNT.incrementAndGet();
    }

    @FunctionalInterface
    interface Invoker{

        void exec();
    }
}
