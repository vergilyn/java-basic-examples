## java8 Interface

### Interface中的default-method、static-method
see: https://www.cnblogs.com/zhoading/p/8440633.html
default-method是属于实例的方法，必须通过对象实例才可以调用。
default-method可以被实现类重写，若实现类实现的多个接口中包含相同签名的default-method，则实现类必须重写此方法。

static-method是属于类的方法，（接口中）只能通过`Class.methodName()`调用。如果是普通的类，可以通过`instance.staticMethod()`调用。
但interface中的static-method无法通过`instance`调用，因为interface无法实例化，且其实现类无法“继承”静态方法。

> 摘自：https://blog.csdn.net/u012817635/article/details/79646160
> java8接口中增加了`default-method`。也就是接口可以实现自己的方法了。这样，抽象类（abstract-class）除了可以单继承和定义参数外，基本和接口没有区别了。
> jdk8 brings arguably the abstract class's greatest advantage over the interface to the interface.
> The implication of this is that a large number of abstract classes used today can likely be replaced and a large number of future work that would have been abstract classes will now instead be interfaces with default methods.

（interface中的default-method，跟父类中定义的抽象方法或实现的方法是一样的。）

### default-method与父类的方法
若interface与super-class存在相同签名的方法，具体类可以不重写此方法，但调用的会是super-class的同名方法。
```java
public class InterfaceClass{
    default void print(){
        System.out.println("InterfaceClass print()");
    }
}

public abstract class ParentClass{
    public void print(){
        System.out.println("ParentClass print()");
    }
}

public SomeClass extends ParentClass implements InterfaceClass{

}
```
如果`new SomeClass().print()`则会打印"ParentClass print()"