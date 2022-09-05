# Nacos Event Publisher

`nacos: 2.1.0` 配置中心由 __长轮询__ 改成了 __push模式__，
nacos-server 端是基于 event-publisher 的方式，而且抽象了一套 event-publisher模型。

**重点参考：**  
- <https://github.com/alibaba/nacos/tree/2.1.0/common/src/main/java/com/alibaba/nacos/common/notify>

## 特殊
- com.alibaba.nacos.common.notify.SlowEvent
- com.alibaba.nacos.common.notify.listener.SmartSubscriber

## 对比

## `spring.ApplicationEvent`
1. **nacos 都是 async-handle-event**  
（spring 支持 sync/async event，或者 listener中用async实现）
__貌似，没有提供任何方式可以变成 sync-event-handle！__

2. 将event区分成2类： share、publisher。
不清楚nacos设计之初为什么会将 event 区分开（但个人感觉这么区分还是比较好的）  

因为使用的是`BlockingQueue`，所以可以指定各自的 queue-max-size:  
- share-queue-max-size: 1024。 （只能通过 启动的 system.properties 指定，详见：`NotifyCenter static-code`）
- xxx-publisher-max-size: 16384，默认值，可以有很多种方式改变。

4. 不能触发父类的事件， 例如`ChildEvent extends ParentEvent`
   由于 `subMap.key = event-class-name`， 并没有递归添加Parent （spring貌似支持 继承事件 同时触发）


## `guava EventBus`
1. guava-EventBus 支持 `sync-EventBus`，也支持 `AsyncEventBus`。
async 是由同一个线程池去执行 all-async-event。
（nacos是可以单独指定某个 subscribe的 executor，更灵活）


### nacos 都是 async-handle-event
