## Method Reference
see: 
- http://www.runoob.com/java/java8-method-references.html
- https://www.cnblogs.com/xiaoxi/p/7099667.html
- https://www.cnblogs.com/chenpi/p/5885706.html

适用场景：当一个Lambda表达式调用了一个已存在的方法。  
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

释疑1：任意对象（属于同一个类）的实例方法引用，如下示例，引用的是String中任意一个对象的`equals`方法。
```java
public class ClassInstanceMethodReferTest {

    @Test
    public void instance(){
        BiPredicate<String, String> biPre = String::equals;
        // biPre = (str1,str2) -> str1.equals(str2);  // lambda
        System.out.println(biPre.test("vergilyn", "zard"));  // false
    }
}
```

