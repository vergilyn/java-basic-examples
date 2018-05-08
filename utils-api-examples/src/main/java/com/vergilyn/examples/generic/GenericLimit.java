package com.vergilyn.examples.generic;

/**
 * 泛型限定
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/16
 */
public class GenericLimit {
    public static void main(String[] args) {
        GenericLimit g = new GenericLimit();
        ExtendParent extendParent = new ExtendParent();
        ComInterfaceImpl comInterface = new ComInterfaceImpl();

        g.limitExtends(extendParent);

        g.limitImpl(comInterface);

        System.out.println("");
        Child child = new Child();
        g.limitMulti(child);
        g.limitImpl(child);
        g.limitExtends(child);

//      g.limitImpl(extendParent);    //错误,看错误提示：参数不支持
    }

    /**
     * 限定参数必须继承自Parent
     *
     * @param t
     * @param <T>
     */
    public <T extends Parent> void limitExtends(T t) {
        t.parentMethod("限定参数必须继承自Parent");
    }

    /**
     * 限定参数必须实现接口ComInterface。与限定继承一样。
     * 注意：不要被此处的extends迷惑了，此处的extends可以直接约定到限定其必须实现接口。
     *
     * @param t
     * @param <T>
     */
    public <T extends ComInterface> void limitImpl(T t) {
        t.interfaceMethod("限定参数必须实现ComInterface");
    }

    /**
     * 限定参数必须继承自Parent,且同时实现ComInterface.
     *
     * @param t
     * @param <T>
     */
    public <T extends Parent & ComInterface> void limitMulti(T t) {
        t.interfaceMethod("限定参数必须继承自Parent,且同时实现ComInterface.");
        t.parentMethod("限定参数必须继承自Parent,且同时实现ComInterface.");
    }
}


interface ComInterface{
    void interfaceMethod(String param);
}

abstract class Parent{
    protected String pName = "parent name";

    abstract void parentMethod(String param);
}

class ExtendParent extends Parent{

    void parentMethod(String param) {
        System.out.println("ExtendParent printout: " + param);
    }
}

class ComInterfaceImpl implements ComInterface{

    public void interfaceMethod(String param) {
        System.out.println("ComInterfaceImpl printout: " + param);
    }
}

class Child extends Parent implements ComInterface{

    public void interfaceMethod(String param) {
        System.out.println("Child interfaceMethod printout: " + param);
    }

    void parentMethod(String param) {
        System.out.println("Child parentMethod printout: " + param);
    }
}