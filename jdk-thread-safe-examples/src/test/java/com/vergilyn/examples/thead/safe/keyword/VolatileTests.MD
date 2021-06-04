# volatile

**test-demo:**
- [volatile可见性详解](https://blog.csdn.net/weixin_44796239/article/details/108322955)
- [Volatile可见性分析（一）](https://www.cnblogs.com/bbgs-xc/p/12725426.html)
- [volatile保证可见性的验证](https://blog.csdn.net/weixin_33656548/article/details/88846777)

**Thread, main-memory & work-memory:**
- [Java线程工作内存与主内存变量交换过程及volatile关键字理解](https://www.cnblogs.com/wrencai/p/5704331.html)
- [工作内存于主内存之间相互刷新的时机]

[工作内存于主内存之间相互刷新的时机]: https://blog.csdn.net/qq_45331503/article/details/114374039

### volatile的“可见性”
> 当被`volatile`修饰时，它会保证修改的值会**立即被更新到主存**，当有其他线程需要读取时，它会去内存中读取新值。
>
> 而`普通变量`不能保证可见性，因为普通共享变量被修改之后，**什么时候被<span color="red">写入主存是不确定的</span>**，  
> 当其他线程去读取时，此时内存中可能还是原来的旧值，因此无法保证可见性。

### 工作内存于主内存之间相互刷新的时机
[工作内存于主内存之间相互刷新的时机]

#### 工作内存的变量刷新到主内存时机
- 把一个从执行引擎接收的值赋给工作内存的变量后，立即同步到主内存。但是这两个操作并不保证原子性。中间可能有其他操作。
- 线程释放锁之前，必须将变量同步到主内存


#### 工作内存同步主内存时机
- 线程获取锁，会强制从主存重新刷新变量值
- 当线程切换到其他线程，后来又切换到该线程的时候去重新读取
- 当执行该线程的cpu有空闲时，它会去主存读取一下共享变量的值来更新线程工作内存中的值
