# google-guava-examples

## cache
+ [github google-guava](https://github.com/google/guava)
+ [wiki guava-cache](https://github.com/google/guava/wiki/CachesExplained)

- [GUAVA CACHE 总结](https://www.cnblogs.com/lihao007/p/9530496.html)
- [使用google guava做内存缓存](https://www.cnblogs.com/kabi/p/8310145.html)
- [集中式内存缓存Guava Cache学习](https://blog.csdn.net/grafx/article/details/80462628)

- [本地缓存guava cache的过期策略与刷新策略](https://blog.csdn.net/daijiguo/article/details/90748783)
- [[Google Guava] 3-缓存](http://ifeve.com/google-guava-cachesexplained/)

相比guava，可能caffeine是更好的选择。Spring5使用Caffeine来代替GuavaCache就是因为性能的问题了。


```
  LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                //设置写缓存后xx秒钟过期
                .expireAfterWrite(5, TimeUnit.SECONDS)

                //设置并发级别为4，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(4)
                //设置缓存容器的初始容量为xx
                .initialCapacity(2)
                //设置缓存最大容量为max，超过max之后就会按照LRU(least recently used 最近使用最少)来移除缓存项
                .maximumSize(8)

                .removalListener((RemovalListener<Integer, String>) notification -> System.out.printf("remove-listener >>>> key: %s, value: %s, reason: %s\r\n", notification.getKey(), notification.getValue(), notification.getCause()))

                //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<Integer, String>() {
                    @Override
                    public String load(Integer key) throws Exception {
                        return "-1:" + LocalTime.now().toString();
                    }
                });
```
1) 每个segment的 maximumSize 接近 maximumSize/concurrencyLevel。
2) 假设`initialCapacity=60，concurrencyLevel=8`，则`segments=8，segment-element=8`。
3) `LoadingCache.get(key)`本质是`getOrLoad`，当value不存在时，会调用build时指定的`CacheLoader`。
4) `cache.size` 返回此缓存中条目的**大致数量**。（当元素全部expired后，可能数值不是0）

1. 清理什么时候发生？
使用CacheBuilder构建的缓存不会"自动"执行清理和回收工作，也不会在某个缓存项过期后马上清理，也没有诸如此类的清理机制。
相反，它会在写操作时顺带做少量的维护工作，或者偶尔在读操作时做——如果写操作实在太少的话。

这样做的原因在于：
如果要自动地持续清理缓存，就必须有一个线程，这个线程会和用户操作竞争共享锁。
此外，某些环境下线程创建可能受限制，这样CacheBuilder就不可用了

2. refreshAfterWrite requires a LoadingCache