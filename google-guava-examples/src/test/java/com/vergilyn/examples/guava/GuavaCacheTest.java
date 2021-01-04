package com.vergilyn.examples.guava;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import org.testng.annotations.Test;

/**
 * 应用缓存/内存缓存，减少redis的连接消耗和提升吞吐。
 * 更好的选择可能是：caffeine。
 * @author VergiLyn
 * @date 2019-06-19
 */
public class GuavaCacheTest {

    @Test
    public void cache() throws ExecutionException {
        Cache<Integer, String> cache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS).concurrencyLevel(4).initialCapacity(2).maximumSize(8).build();

        int i = 5;
        while ((i--) > 0) {
            cache.put(i, i + "");
        }

        i = 10;
        while ((i--) > 0) {
            System.out.println(i + ":" + cache.getIfPresent(i));
        }

    }

    @Test
    public void notWriteNull() {
        Cache<Integer, Boolean> cache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .concurrencyLevel(1)
                .initialCapacity(2)
                .maximumSize(2)
                .build();

        try {
            cache.get(1, () -> true);

            cache.get(2, () -> false);


            // Warning: as with CacheLoader.load(K), loader must not return null;
            // it may either return a non-null value or throw an exception.

            // Callable.load() 不能返回空，但又不想 key 被默认值占用
            // 因为根据LRU、maximumSize考虑，期望是：当load()得到的是null时，不占用size。
            // 可以通过抛出异常实现。
            cache.get(3, () -> {
                if (true) {
                    throw new Exception("不写入");
                }
                return false;
            });


        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        printAsMap(cache);
    }

    /**
     * <a href="https://blog.csdn.net/daijiguo/article/details/90748783">本地缓存guava cache的过期策略与刷新策略</a>
     * refresh 并不是 reload，
     */
    @Test
    public void strategyExpireAndRefresh() throws Exception {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(Duration.ofSeconds(2))
                .expireAfterWrite(Duration.ofSeconds(4))
                .concurrencyLevel(1)
                .initialCapacity(2)
                .maximumSize(2)
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer key) throws Exception {
                        return LocalTime.now().toString();
                    }
                });

        /*
         * 期望效果，当一个缓存被写入后，间隔xx时间，自动调用CacheLoader.load()。
         * 坑： cache.asMap()中并不是get，并不会触发 refreshAfterWrite
         *
         * 结论：
         *   并不是间隔xx时间，自动调用。
         *   而是下次调用（例如get时触发），判断时间是否超过xx，超过则会去调用load()。
         *   注意AfterWrite与AfterAccess的区别。
         */

        cache.put(1, LocalTime.now().toString());
        cache.put(2, LocalTime.now().toString());
        printAsMap(cache);

        Thread.sleep(2000);

        System.out.println("refresh: " + cache.get(1));  // 超过refreshAfterWrite设置的duration，会去调用CacheLoader.load()

        printAsMap(cache);


    }

    @Test
    public void loaderDefaultAndSpecify() throws Exception {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(2, TimeUnit.SECONDS)
                .concurrencyLevel(1)
                .initialCapacity(1)
                .maximumSize(1)
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer key) throws Exception {
                        return "NONE";
                    }
                });

        Callable<String> callable = () -> LocalTime.now().toString();

        int key = 1;

        System.out.println(cache.get(key, callable));

        Thread.sleep(3000);

        System.out.println(cache.get(key, callable));
    }

    @Test
    public void clean() throws Exception {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(5, TimeUnit.SECONDS)
                .concurrencyLevel(1)
                .initialCapacity(1)
                .maximumSize(1)
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer key) throws Exception {
                        return LocalTime.now().toString();
                    }
                });

        /*
         * 期望：主动清除所有缓存，并且之后调用get会触发default-load或者specify-load。
         *
         * 更希望存在refreshAll，但是只存在refresh(key)。
         */

        int key = 1;
        System.out.println(cache.get(key));
        // cache.cleanUp();  // 达不到效果，invalidateAll 可以
        cache.invalidateAll();
        System.out.println(cache.get(key));

    }

    private void printAsMap(Cache<?, ?> cache) {
        System.out.println("printAsMap >>>> begin");

        cache.asMap().forEach((key, value) -> System.out.printf("key: %s, value: %s \r\n", key, value));

        System.out.println("printAsMap >>>> end");

    }

    private void printGet(Cache<?, ?> cache){
        System.out.println("printGet >>>> begin");

        Set<?> keySet = cache.asMap().keySet();

        keySet.forEach(e -> System.out.printf("key: %s, value: %s \r\n", e, cache.getIfPresent(e)));

        System.out.println("printGet >>>> end");

    }
}

