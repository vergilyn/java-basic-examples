# \[usage-0010] 如何友好定义 logger 的 log-prefix？

多层方法调用（包括 sync&async），怎么保持 log-prefix 一致？

1. 如果通过传参 log-prefix， 感觉每个方法都要 多一个参数....（不友好...但实现简单）

2. 包装 logger，参考 dubbo的实现方式 （推荐）

**个人思路：**  
比如  main ->   sync-method-x -> async-method-y -> sync-method-z  
如果 中途需要 追加log-prefix，那么可以把 ThreadLocal 保存成 LinkedHashSet<String>，logger-wrapper 取list组装。 
（**ThreadLocal需要考虑 线程池复用 带来的问题！**）

2022-01-21 >>>> 感觉可行，待实现demo！  
但是感觉 用 ThreadLocal 不是很理想啊...  ThreadLocal 的操作，可以由 logger-wrapper 提供，比如 logger-wrapper.registry(log-prefix)，
总之 不要让业务代码出现实现 ThreadLocal，方便后续替换实现。业务代码应该只是在需要的地方 定义log-prefix 并 注册！
（ThreadLocal 优点，简单，而且不用考虑 资源释放的问题）

- [利用APT优雅的实现统一日志格式](https://blog.csdn.net/enweitech/article/details/79044368)

