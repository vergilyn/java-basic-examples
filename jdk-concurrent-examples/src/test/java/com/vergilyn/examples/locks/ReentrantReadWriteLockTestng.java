package com.vergilyn.examples.locks;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.testng.annotations.Test;
import org.testng.collections.Maps;

/**
 * <p>This lock supports a maximum of 65535 recursive write locks
 * and 65535 read locks. Attempts to exceed these limits result in
 * {@link Error} throws from locking methods.
 *
 * <p>
 * 因为{@linkplain ReentrantReadWriteLock}用 32位int 来保存state。其中：
 * 高16位表示读；低16位表示写
 *
 * @author vergilyn
 * @date 2020-06-23
 * @see ReentrantReadWriteLock
 * @see <a href="https://mp.weixin.qq.com/s/0NK1RwmzLjNSEAKSAKSIyA">搞定ReentrantReadWriteLock 几道小小数学题就够了</a>
 */
public class ReentrantReadWriteLockTestng {
    private static final String KEY = "key";
    private final Map<String, LocalTime> cache = Maps.newHashMap();
    private LocalTime data;
    private volatile boolean cacheValid;
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(false);
    private final ReentrantReadWriteLock.ReadLock rl = rwl.readLock();
    private final ReentrantReadWriteLock.WriteLock wl = rwl.writeLock();

    @Test(threadPoolSize = 5, invocationCount = 15)
    public void template() {
        rl.lock();

        if (!cacheValid) {
            // Must release read lock before acquiring write lock
            rl.unlock();

            wl.lock();
            try {
                // Recheck state because another thread might have
                // acquired write lock and changed state before we did.
                if (!cacheValid) {
                    data = loadData();
                    cacheValid = true;
                }

                // write-lock 可以降级成 read-lock （反之，不可以将read-lock升级成write-lock）
                // Downgrade by acquiring read lock before releasing write lock
                rl.lock();
            } finally {
                wl.unlock(); // Unlock write, still hold read
            }
        }

        try {
            print(data);
        } finally {
            rl.unlock();
        }
    }

    @Test(threadPoolSize = 5, invocationCount = 15)
    public void right(){
        LocalTime value;

        rl.lock();
        try{
            // 获取缓存中的值
            value = cache.get(KEY);
        }finally {
            rl.unlock();
        }

        // 缓存中值不为空，直接返回
        if (value != null) {
            print(value);
            return;
        }

        // 缓存中值为空，则通过写锁查询DB，并将其写入到缓存中
        wl.lock();
        try{
            // 再次尝试获取缓存中的值 （双重校验锁）
            value = cache.get(KEY);
            if (value == null) {
                value = loadData();
                cache.put(KEY, value);
            }
        }finally {
            wl.unlock();
        }

        print(value);
    }

    /**
     * 以下代码会造成deadlock：
     * 因为获取一个写入锁需要先释放所有的读取锁，如果有两个读取锁试图获取写入锁，且都不释放读取锁时，就会发生死锁，所以在这里，锁的升级是不被允许的
     */
    @Test(timeOut = 20_000)
    public void deadlock(){
        LocalTime value;
        rwl.readLock().lock();
        try{
            value = cache.get(KEY);
            if (value == null){
                rwl.writeLock().lock();  // 必须先释放read-lock，再获取write-lock，否则发生deadlock
                try{
                    value = cache.get(KEY);
                    if (value == null) {
                        value = LocalTime.now();
                        cache.put(KEY, value);
                    }
                }finally {
                    rwl.writeLock().unlock();
                }
            }
        }finally {
            rwl.readLock().unlock();
        }

        print(data);
    }

    private LocalTime loadData(){
        return LocalTime.now();
    }

    private void print(LocalTime data){
        System.out.printf("thread: %s, hashcode: %d, data: %s \r\n",
                Thread.currentThread().getName(),
                Thread.currentThread().hashCode(),
                data.toString());
    }
}
