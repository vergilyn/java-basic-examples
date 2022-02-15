# logger-examples

## SLF4J，Logback，Log4j2 关系
[Java日志框架SLF4J和log4j以及logback的联系和区别](https://www.cnblogs.com/hanszhao/p/9754419.html)

SLF4J只是一个接口。  
其实slf4j原理很简单，他只提供一个核心slf4j api(就是slf4j-api.jar包)，这个包只有日志的接口，并没有实现，
所以如果要使用就得再给它提供一个实现了些接口的日志包，比 如：log4j,common logging,jdk log日志实现包等，
但是这些日志实现又不能通过接口直接调用，实现上他们根本就和slf4j-api不一致，因此slf4j又增加了一层来转换各日志实现包的使用，
当然slf4j-simple除外。其结构如下：
```
slf4j-api(接口层) 
   | 
各日志实现包的连接层( slf4j-jdk14, slf4j-log4j) 
   | 
各日志实现包 
```

Log4j是Apache的一个开源项目，slf4j 具体的实现！  

logback同样是由log4j的作者设计完成的，拥有更好的特性，用来取代log4j的一个日志框架，是slf4j的原生实现。
（即直接实现了slf4j的接口，而log4j并没有直接实现，所以就需要一个适配器slf4j-log4j12.jar）

**总结如下：**  
1、slf4j是java的一个日志门面，实现了日志框架一些通用的api，log4j和logback是具体的日志框架。  
2、他们可以单独的使用，也可以绑定slf4j一起使用。  
单独使用，分别调用框架自己的方法来输出日志信息。  
绑定slf4j一起使用，调用slf4j的api来输入日志信息，具体使用与底层日志框架无关（需要底层框架的配置文件）。  

显然不推荐单独使用日志框架。假设项目中已经使用了log4j，而我们此时加载了一个类库，而这个类库依赖另一个日志框架。
这个时候我们就需要维护两个日志框架，这是一个非常麻烦的事情。
而使用了slf4j就不同了，由于应用调用的抽象层的api，与底层日志框架是无关的，因此可以任意更换日志框架。
