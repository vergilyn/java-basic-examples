## Method Reference
see: 
- http://www.runoob.com/java/java8-method-references.html
- https://www.cnblogs.com/xiaoxi/p/7099667.html
- https://www.cnblogs.com/chenpi/p/5885706.html

> 什么是method-interface？  
> 方法引用是用来直接访问类或者实例的已经存在的方法或者构造方法。
> 方法引用提供了一种引用而不执行方法的方式。
> 它需要由兼容的函数式接口(functional-interface)构成的目标类型上下文。

适用场景：当Lambda表达式中只是执行一个已存在方法调用时，不用Lambda表达式，直接通过方法引用的形式可读性更高一些。**方法引用是一种更简洁易懂的Lambda表达式**。

不适用场景：当我们需要往引用的方法传其它参数的时候，不适合用method-reference，如下示例：`IsReferable demo = () -> ReferenceDemo.commonMethod("Argument in method.")`

### 示例
```java
public class Car {
    // java.util.function.Supplier是jdk1.8定义的函数接口，这里和lambda一起使用
    public static Car create(final Supplier<Car> supplier) {
        return supplier.get();
    }
 
    public static void collide(final Car car) {
        System.out.println("Collided " + car.toString());
    }
 
    public void follow(final Car another) {
        System.out.println("Following the " + another.toString());
    }
 
    public void repair() {
        System.out.println("Repaired " + this.toString());
    }
}
```

- 构造器引用：`Class::new`，或者`Class<T>::new`
```
final Car car = Car.create( Car::new );
final List< Car > cars = Arrays.asList( car );
```

- 静态方法引用：`Class::static_method`
```
cars.forEach( Car::collide );

```

- 对象的方法引用：`instance::method`，或`this::method`，或`super::method`
```
final Car police = Car.create( Car::new );
cars.forEach( police::follow );
```

- 引用类的实例方法：`Class::instance_method`
```
cars.forEach( Car::repair );
```

### 疑问
1. 未能理解`引用类的实例方法 Class::instance_method`的使用方式，及使用场景？

大致解答：任意对象的实例方法引用，如下示例，引用的是String中任意一个对象的`equals`方法。  
因为是instance-method，所以还是必须由某个对象实例去调用，一般都是由第一个参数的实例去调用。所以，若arg1 = null，则会抛出NullPointerException。
```java
public class ClassInstanceMethodReferTest {

    @Test
    public void instance(){
        BiPredicate<String, String> biPre = String::equals;
        // biPre = (str1,str2) -> str1.equals(str2);  // lambda
        System.out.println(biPre.test("vergilyn", "zard"));  // false
    }
    
        @Test
        public void npe(){
            BiPredicate<String, String> biPre = String::equals;
            System.out.println(biPre.test("zard", null));   // false
            System.out.println(biPre.test(null, "zard"));   // NullPointerException
        }
}
```

