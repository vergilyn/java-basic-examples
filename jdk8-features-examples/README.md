## jdk8-features-examples
- [oracle，What's New in JDK 8][What's New in JDK 8]
- [runoob，Java 8 新特性][java8-new-features]

[java8-new-features]: http://www.runoob.com/java/java8-new-features.html
[What's New in JDK 8]: http://www.oracle.com/technetwork/java/javase/8-whats-new-2157071.html

### 1. lambda
lambda允许把函数作为一个方法的参数（函数作为参数传递进方法中）。

### 2. Functional Interface（函数接口）
java8之前要将方法作为参数传递只能通过匿名内部类来实现，而且代码很难看，也很长，functional-interface就是对匿名内部类的优化。

jdk8自带的function-interface在package：{@linkplain java.util.function}。  
@FunctionalInterface：主要用于编译级错误检查，加上该注解，当你写的接口不符合函数式接口定义的时候，编译器会报错。

### 3. Method Reference（方法引用）
方法引用通过方法的名字来指向一个方法，可以直接引用已有类或对象的方法或构造函数。
方法引用可以使语言的构造更紧凑简洁，减少冗余代码。
方法引用使用一对冒号`::`。

### 4. Default Method（默认方法）
默认方法就是一个在接口里面有了一个实现的方法。

### 5. Date Time API
新的日期时间API，加强对日期与时间的处理。

### 6. java.util.Optional
Optional 类已经成为 Java 8 类库的一部分，用来解决空指针异常。

- https://www.cnblogs.com/zhangboyu/p/7580262.html
- http://www.runoob.com/java/java8-optional-class.html