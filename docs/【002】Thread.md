# Thread



##
+ [你能说出多线程中 sleep、yield、join 的用法及 sleep与wait区别吗？](https://mp.weixin.qq.com/s/XJJ7iDtIL7gGtoR_RpO69A)

### sleep，静态方法（当前线程）
**sleep不会释放锁，不会释放锁，不会释放锁。**  
可以理解为他进入监视器这个房间之后，在这房间里面睡着了。

sleep也是可中断方法（从方法签名可以看得出来，可能抛出InterruptedException），  
也就是说如果一个线程正在sleep，如果另外的线程将他中断（调用interrupt方法），将会抛出异常，并且中断状态将会擦除。

所以对于sleep方法，要么自己醒来，要么被中断后也会醒来。
对于sleep始终有一个超时时间的设置，所以，尽管他是在监视器内睡着了，但是并不会导致死锁，因为它终究是要醒来的。


**被Interrupted"唤醒"的线程，catch-InterruptedException后如果未throw/return，那么其实会继续执行后面的代码。**

为什么`thread.interrupt()`后，`Thread.isInterrupted`始终返回`false`？
- [Thread中interrupted()方法和isInterrupted()方法区别总结](https://blog.csdn.net/zhuyong7/article/details/80852884)

### yield （让步;退让），静态方法（当前线程）
对于sleep或者wait方法，他们都将进入特定的状态，伴随着状态的切换，  
也就意味着等待某些条件的发生，才能够继续，比如条件满足，或者到时间等。

但yield方法不涉及这些事情，他针对的是时间片的划分与调度，所以对开发者来说只是临时让一下，让一下他又不会死，就只是再等等。

yield方法将会暂停当前正在执行的线程对象，并执行其他线程，他始终都是RUNNABLE状态。  
如果调用了yield方法，对CPU时间片的分配进行了“礼让”，他仍旧有可能继续获得时间片，并且继续执行。
所以，**调用一次yield 并不一定代表肯定会发生什么。**

### join，实例方法
主线程main中调用启动线程（调用start），然后调用该线程的join方法，
可以达到主线程等待工作线程运行结束才执行的效果，并且join要在start调用后。
（具体参考代码示例）

join的效果是：  
一个线程等待另一个线程（直到结束或者持续一段时间）才执行，那么谁等待谁？  
在哪个线程调用，哪个线程就会等待；调用的哪个Thread对象，就会等待哪个线程结束；

### sleep、yield、join总结
对于yield方法，比较容易理解，只是简单地对于CPU时间片的“礼让”，
除非循环yield，否则一次yield，可能下次该线程仍旧可能会抢占到CPU时间片，可能方法调用和不调用没差别。

sleep是静态方法，针对当前线程，进入休眠状态，两个版本的sleep方法始终有时间参数，所以必然会在指定的时间内苏醒，**sleep也不会释放锁**，
当然，sleep方法的调用非必须在同步方法（同步代码块）内。

join是实例方法，表示等待谁，是用于线程顺序的调度方法，可以做到一个线程等待另外一个线程，
join有三个版本，指定超时时间或者持续等待直到目标线程执行结束，join也无需在同步方法（同步代码块）内。


sleep和join都是可中断方法，被其他线程中断时，都会抛出InterruptedException异常，并且会醒来。


join方法底层依赖wait，我们对比下wait与sleep：
- wait和sleep都会使线程进入阻塞状态，都是可中断方法，被中断后都会抛出异常
- wait是Object的方法，sleep是Thread的方法
- wait必须在同步中执行，sleep不需要（join底层依赖wait，但是不需要在同步中，因为join方法就是synchronized的）
- wait会释放锁，sleep不会释放锁
- wait（无超时设置的版本）会持续阻塞，必须等待唤醒，而sleep必然有超时，所以一定会自己醒来
- wait 实例方法（Object），在对象上调用，表示在其上等待；sleep静态方法，当前线程