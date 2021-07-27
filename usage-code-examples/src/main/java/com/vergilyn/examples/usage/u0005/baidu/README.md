# baidu UidGenerator
- <https://github.com/baidu/uid-generator>


```text
+------+----------------------+----------------+-----------+
| sign |     delta seconds    | worker node id | sequence  |
+------+----------------------+----------------+-----------+
  1bit          28bits              22bits         13bits
```
baidu-UidGenerator算法描述：指定机器 & 同一时刻 & 某一并发序列，是唯一的。据此可生成一个64 bits的唯一ID（long）。默认采用的字节分配方式：

* sign(1bit)  
  固定1bit符号标识，即生成的UID为正数。

* delta seconds (28 bits)  
  当前时间，相对于时间基点"2016-05-20"的增量值，单位：秒，最多可支持约8.7年

* worker id (22 bits)  
  机器id，最多可支持约420w次机器启动。内置实现为在启动时由数据库分配，默认分配策略为用后即弃，后续可提供复用策略。

* sequence (13 bits)  
  每秒下的并发序列，13 bits可支持每秒8192个并发。

**以上参数均可通过Spring进行自定义**

## 2021-07-26
`workId` 的获取方式不友好（需要依赖mysql）