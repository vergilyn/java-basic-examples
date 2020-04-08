# reflect-examples

- [Java 的内部类反射，使用 newInstance 总是抛 NoSuchMethodException 异常呢？](https://www.v2ex.com/t/585169)
- [Java内部类反射上的坑](https://www.jianshu.com/p/ecda088dcc5f)

## FAQ

### 内部类通过反射实例化 NoSuchMethodException
```text
java.lang.InstantiationException: com.vergilyn.examples.reflect.OuterClass$NormalInnerClass
    at java.lang.Class.newInstance(Class.java:427)
    at com.vergilyn.examples.reflect.InnerClassInstanceTestng.normalInnerClassError(InnerClassInstanceTestng.java:32)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:498)
Caused by: java.lang.NoSuchMethodException: com.vergilyn.examples.reflect.OuterClass$NormalInnerClass.<init>()
    at java.lang.Class.getConstructor0(Class.java:3082)
    at java.lang.Class.newInstance(Class.java:412)
    ... 28 more
```

**static-inner-class 不存在该问题。**  

**原因：**    
非静态内部类 的无参构造函数`public InnerClass(){}` 其实是有一个隐藏参数：外部类的指针`this`。  
所以，非静态内部类 的无参构造函数 实际类似：`public InnerClass(OuterClass outer){}`。  

所以，normal-inner-class 实例化时，需要先实例化 outer-class，并传递 outer-class 的`this`。