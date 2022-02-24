# jmh-examples

JMH(Java Micro-benchmark Harness):
+ [OpenJDK JMH](http://openjdk.java.net/projects/code-tools/jmh/)
+ [Openjdk Jmh Samples](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)

> JMH is a Java harness for building, running,
> and analysing nano/micro/milli/macro benchmarks written in Java and other languages targetting the JVM.

专门用于代码微基准测试的工具套件。何谓Micro Benchmark呢？  
单的来说就是基于方法层面的基准测试，精度可以达到 μs（微秒级）。
当你定位到热点方法，希望进一步优化方法性能的时候，就可以使用JMH对优化的结果进行量化的分析。

和其他竞品相比——如果有的话，JMH最有特色的地方就是，它是由Oracle内部实现JIT的那拨人开发的。

**JMH比较典型的应用场景有：**
1) 想准确的知道某个方法需要执行多长时间，以及执行时间和输入之间的相关性；
2) 对比接口不同实现在给定条件下的吞吐量；
3) 查看多少百分比的请求在多长时间内完成；

## 概念
<font color="red">JMH 利用maven的plugin，会在`/target/`生成测试class文件代码。</font>

+ [JMH使用指南](https://blog.csdn.net/xiandafu/article/details/94029094)

2020-05-07 TODO >>>>  
1. 配置含义 `org.openjdk.jmh.runner.options.OptionsBuilder`
2. 注解配置 与 Option属性配置区别
3. @TearDown 貌似不满足 类似 AfterTest 逻辑，怎么办？

备注:  
1. run options(OptionsBuilder) 优先级**貌似高于** 注解。

### 预热 warm-up
为什么要预热？  
因为 JVM 的 JIT 机制的存在，如果某个函数被调用多次之后，JVM 会尝试将其编译成为机器码从而提高执行速度。
为了让 benchmark 的结果更加接近真实情况就需要进行预热。

### Mode (IMPORTANT)
Mode 表示 JMH 进行 Benchmark 时所使用的模式。
  - Throughput: 整体吞吐量，例如“1秒内可以执行多少次调用”。
  - AverageTime: 调用的平均时间，例如“每次调用平均耗时xxx毫秒”。
  - SampleTime: 随机取样，最后输出取样结果的分布，例如“99%的调用在xxx毫秒以内，99.99%的调用在xxx毫秒以内”
  - SingleShotTime: 以上模式都是默认一次 iteration 是 1s，唯有 SingleShotTime 是只运行一次。往往同时把 warmup 次数设为0，用于测试冷启动时的性能。

`Throughput`, `AverageTime`, `SampleTime`, `All`: **This mode is <font color="red">time-based</font>, and it will run until the iteration time expires.**  
即迭代执行iterations次，每次执行timeout，然后计算。

`SingleShotTime`: **This mode is <font color="red">work-based</font>, and will run only for a single invocation of Benchmark method.**
即迭代执行iterations次，每次执行batchSize，这样就可以预估最后的调用次数，EX. `JmhEstimateTest.java`

### Iteration
Iteration 是 JMH 进行测试的最小单位。  
在大部分模式下，一次 iteration 代表的是一秒，JMH 会在这一秒内不断调用需要 benchmark 的方法，然后根据模式对其采样，计算吞吐量，计算平均执行时间等。

## Annotations
- [Java 并发测试神器：基准测试神器-JMH]

### `@Benchmark`, Method
`@Benchmark`标签是用来标记测试方法的，只有被这个注解标记的话，该方法才会参与基准测试。
**但是有一个基本的原则就是被@Benchmark标记的方法必须是public的。**

### `@Warmup`, Class / Method
`@Warmup`用来配置预热的内容，可用于类或者方法上，越靠近执行方法的地方越准确。一般配置warmup的参数有这些：
  - iterations: 预热的次数。
  - time: 每次预热的间隔时间。
  - timeUnit: 时间单位，默认是s。
  - batchSize: 批处理大小，每次操作调用几次方法。

### `@Measurement`, Class / Method
用来控制实际执行的内容，配置的选项与`@Warmup`基本一样。

### `@BenchmarkMode`, Class / Method
`@BenchmarkMode主`要是表示测量的纬度，有以下这些纬度可供选择：
  - Mode.Throughput: 吞吐量纬度
  - Mode.AverageTime: 平均时间
  - Mode.SampleTime: 抽样检测
  - Mode.SingleShotTime: 检测一次调用
  = Mode.All: 运用所有的检测模式 在方法级别指定@BenchmarkMode的时候可以一定指定多个纬度，例如：@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime, Mode.SingleShotTime})，代表同时在多个纬度对目标方法进行测量。

### `OutputTimeUnit`
`@OutputTimeUnit`代表测量的单位，比如秒级别，毫秒级别，微妙级别等等。
一般都使用微妙和毫秒级别的稍微多一点。

该注解可以用在方法级别和类级别，当用在类级别的时候会被更加精确的方法级别的注解覆盖，原则就是离目标更近的注解更容易生效。

### `@State`, Class
在很多时候我们需要维护一些状态内容，比如在多线程的时候我们会维护一个共享的状态，这个状态值可能会在每隔线程中都一样，也有可能是每个线程都有自己的状态，JMH为我们提供了状态的支持。  
**该注解只能用来标注在类上，因为类作为一个属性的载体。**

- Scope.Benchmark: 该状态的意思是会在所有的Benchmark的工作线程中共享变量内容。
- Scope.Group: 同一个Group的线程可以享有同样的变量
- Scope.Thread: 每隔线程都享有一份变量的副本，线程之间对于变量的修改不会相互影响。


## 测试注意事项
[Java 并发测试神器：基准测试神器-JMH] 参考文章末尾的`JMH进阶`：
  - 不要辩解无用的代码
  - 循环处理
  - 方法内联

这些代码可能**被编译器优化**，从而影响测试结果！

## 疑问

### `Iteration` 与 `Invocation` 区别
我们在配置Warmup的时候默认的时间是的1s，即1s的执行作为一个Iteration，
假设每次方法的执行是100ms的话，那么1个Iteration就代表10个Invocation。

## 备注
1. IDEA 有一个叫`JMH plugin`的插件，但是last-update 是2017，暂时未使用。

## 参考
  - [Java 并发测试神器：基准测试神器-JMH]
  - [JMH使用说明]
  - [JMH: 最装逼，最牛逼的基准测试工具套件]


[Java 并发测试神器：基准测试神器-JMH]: https://mp.weixin.qq.com/s/JkbtjPnaWNQ57t7MSb1JlQ
[JMH使用说明]: https://blog.csdn.net/lxbjkben/article/details/79410740
[JMH: 最装逼，最牛逼的基准测试工具套件]: https://www.jianshu.com/p/0da2988b9846