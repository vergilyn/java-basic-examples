# spi-examples

> SPI(Service Provider Interface): 是一种服务发现机制。  
> 当程序运行调用接口时，会根据配置文件或默认规则信息加载对应的实现类。
> 所以在程序中并没有直接指定使用接口的哪个实现，而是在外部进行装配。

+ [Service Provider in Java 5](http://java.sun.com/j2se/1.5.0/docs/guide/jar/jar.html#Service%20Provider)
+ [Dubbo 扩展点加载机制：从 Java SPI 到 Dubbo SPI](https://mp.weixin.qq.com/s/PMF2kqT-XnAVmrxoutE0eQ)

2020-04-08 >>>> dubbo SPI 引入（关联代码太多，不再单独提取代码）  
```xml
<!-- dubbo SPI -->
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-common</artifactId>
    <version>2.7.6</version>
</dependency>
```

## SPI, SEATA vs DUBBO vs Java
dubbo SPI 与 `org.apache.dubbo.common.URL`结合定制了很多的功能。  
相对的，SEATA 源码中的SPI实现相对简单很多。  

Dubbo SPI 相较于 Java SPI 更为强大，并且都是由自己实现的一套 SPI 机制。其中主要的改进和优化：  
- 相对于 Java SPI 一次性加载所有实现，**Dubbo SPI 是按需加载，只加载需要使用的实现类。同时带有缓存支持。**
- 更为详细的扩展加载失败信息。
- 增加了对扩展 IOC 和 AOP的支持。