# micrometer 
- nacos: `MetricsMonitor`

- 深入分析基于Micrometer和Prometheus实现度量和监控的方案：<https://blog.csdn.net/weixin_45994575/article/details/119140208>
> 集成了`Micrometer`框架的`JVM应用`使用到Micrometer的API收集的度量 **数据位于内存之中**，  
> 因此，需要额外的存储系统去存储这些度量数据，需要有**监控系统负责统一收集和处理这些数据**，还需要有一些UI工具去展示数据。
> 
> 常见的存储系统就是时序数据库，主流的有Influx、Datadog等。
> 比较主流的监控系统（主要是用于数据收集和处理）就是`Prometheus`（一般叫普罗米修斯，下面就这样叫吧）。
> **而展示的UI目前相对用得比较多的就是Grafana。**
> 另外，Prometheus已经内置了一个时序数据库的实现，
> 因此，在做一套相对完善的度量数据监控的系统只需要依赖: 目标JVM应用、Prometheus组件、Grafana组件。