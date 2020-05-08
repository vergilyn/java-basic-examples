package com.vergilyn.examples.reflect;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * <a href="https://stackoverflow.com/questions/19886065/java-getmethod-with-subclass-parameter">Java getMethod with subclass parameter</a>
 * <a href="https://github.com/apache/dubbo/issues/6112">issues#6112, GenericService Invoke Don't Support Subclasses</a>
 * @author vergilyn
 * @date 2020-05-08
 */
public class SubclassMethodTestng {

    class A{}

    class B extends A{}

    class C extends B{}

    interface Ia{}

    interface Api{
        void hello(A a);
    }

    public static final Class<?> CLAZZ = Api.class;
    public static final String METHOD_NAME = "hello";

    @Test
    public void byA() throws NoSuchMethodException {
        Method method = CLAZZ.getMethod(METHOD_NAME, A.class);
        assertNotNull(method);
    }

    @Test
    public void byBSupper() throws NoSuchMethodException {
        Method method = CLAZZ.getMethod(METHOD_NAME, B.class.getSuperclass());  // <=> A.class
        assertNotNull(method);
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void byC() throws NoSuchMethodException {
        CLAZZ.getMethod(METHOD_NAME, C.class);
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void byB() throws NoSuchMethodException {
        CLAZZ.getMethod(METHOD_NAME, B.class);  // <=> C.class.getSuperclass()
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void byCSupper() throws NoSuchMethodException {
        CLAZZ.getMethod(METHOD_NAME, C.class.getSuperclass()); // <=> B.class
    }

    @Test(expectedExceptions = NoSuchMethodException.class)
    public void byABC() throws NoSuchMethodException {
        CLAZZ.getMethod(METHOD_NAME, A.class, B.class, C.class);
    }
}
