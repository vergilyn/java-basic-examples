package com.vergilyn.examples.jdk8.features.reference;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * method-reference，静态方法引用：`Class::static_method`
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class StaticMethodReferTest {

    @Test
    public void test(){
        Supplier<LocalDateTime> now = StaticMethodReferTest::now;
        System.out.println("test: " + now.get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));

        List<Integer> list = Lists.newArrayList(2, 1, 3, 4);
        list.forEach(StaticMethodReferTest::increment);

        // 泛型
        // 注意，参数传递发生在::的后面。这种语法可以推广。
        // 当把泛型方法指定为方法引用时，类型参数出现在::之后、方法名之前。
        // 但是，需要指出的是，在这种情况(和其它许多情况)下，并非必须显示指定类型参数，因为类型参数会被自动推断得出。
        // 对于指定泛型类的情况，类型参数位于类名的后面::的前面。
        Function<String, Class<?>> str = StaticMethodReferTest::<String>generic;
        System.out.println(str.apply("string"));

        Function<Long, Class<?>> lon = StaticMethodReferTest::<Long>generic;
        System.out.println(lon.apply(0L));
    }

    private static <T> Class<?> generic(T t){
        return t.getClass();
    }

    private static LocalDateTime now(){
        System.out.println("static-method now()");
        return LocalDateTime .now();
    }

    private static int increment(int x){
        System.out.println("static-method increment(x): " + x);
        return x + 1;
    }
}
