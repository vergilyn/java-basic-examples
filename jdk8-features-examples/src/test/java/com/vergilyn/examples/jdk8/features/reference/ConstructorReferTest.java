package com.vergilyn.examples.jdk8.features.reference;

import java.time.LocalDate;
import java.util.function.Function;
import java.util.function.Supplier;

import org.testng.annotations.Test;

/**
 * method-reference之构造器引用：`Class::new`，或者`Class<T>::new`
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/11
 */
public class ConstructorReferTest {

    /* 构造器引用
     * 语法是`Class::new`，或者更一般的`Class<T>::new`
     * 注意：需要调用的构造器方法与函数式接口中抽象方法的参数列表保持一致
     */
    @Test
    public void test(){
        Supplier<Person> supp = Person::new;
        Person sp = supp.get();
        System.out.printf("refer constructor: Person() >>>> %s", sp).println("\r\n");

        Function<String, Person> func = Person::new; // lambda语法`(name) -> new Person(name)`
        Person fp = func.apply("func");
        System.out.printf("refer constructor: Person(String name) >>>> %s",fp).println("\r\n");

        // 自定义函数接口，以调用构造函数`Person(String name, int age)`
        PersonFunctionalInterface<Person> custom = Person::new;
        Person cp = custom.instance("custom", LocalDate.now());
        System.out.printf("refer constructor: Person(String name, LocalDate date) >>>> %s", cp).println("\r\n");
    }

    // functional-interface只允许有一个抽象方法
    // 非必须注解，只是用于编译期检测是否符合functional-interface规范
    @FunctionalInterface
    private interface PersonFunctionalInterface<Person>{
        Person instance(String name, LocalDate date);
    }

    private class Person{
        private String name;
        private LocalDate date;

        public Person(){
            System.out.println("invoke Person()!");
        }

        public Person(String name) {
            System.out.println("invoke Person(String name)!");
            this.name = name;
        }

        public Person(String name, LocalDate date) {
            System.out.println("invoke Person(String name, int age)!");
            this.name = name;
            this.date = date;
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", date=" + date + '}';
        }
    }
}
