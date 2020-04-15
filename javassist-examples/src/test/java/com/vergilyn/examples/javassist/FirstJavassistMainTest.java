package com.vergilyn.examples.javassist;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.NotFoundException;

/**
 * @author vergilyn
 * @date 2020-04-15
 */
public class FirstJavassistMainTest {
    private static final String PACKAGE_NAME = FirstJavassistMainTest.class.getPackage().getName();

    /**
     * expect:
     * <pre>
     *     package com.vergilyn.examples.javassist;
     *
     *     public class FirstJavassist {
     *         private String property;
     *         public static String now = "...";  // = LocalTime.now().toString();
     *
     *         public FirstJavassist(){
     *         }
     *
     *         public FirstJavassist(String property){
     *             this.property = property;
     *         }
     *
     *         public void setProperty(String property){
     *             this.property = property;
     *         }
     *
     *         public void print(String str){
     *             System.out.printf("now: %s, property: %s >>>> %s \r\n", now, property, str);
     *         }
     *     }
     * </pre>
     */
    public static void main(String[] args)
            throws CannotCompileException, IOException, IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException, NotFoundException, NoSuchFieldException {
        String className = "FirstJavassist";

        // 获得类池
        ClassPool pool = ClassPool.getDefault();

        // 创建类
        CtClass cc = pool.makeClass(PACKAGE_NAME + "." + className);

        cc.addField(CtField.make("private String property;", cc));
        cc.addField(CtField.make("public static String now = \"" + LocalTime.now().toString() + "\";", cc));

        String printMethodDesc = "public void print(String str){ "
                                + " System.out.printf(\"now: %s, property: %s >>>> %s \\r\\n\", new Object[]{now, property, $1}); "
                                + "}";
        cc.addMethod(CtMethod.make(printMethodDesc, cc));

        String setPropertyMethodDesc = "public void setProperty(String property){ this.property = $1;}";
        cc.addMethod(CtMethod.make(setPropertyMethodDesc, cc));

        // 添加自定义构造函数
        CtConstructor constructor = CtNewConstructor.make("public " + className + "(String property){}", cc);
        constructor.setBody(" this.property = $1;");
        cc.addConstructor(constructor);

        // 添加默认构造函数
        cc.addConstructor(CtNewConstructor.defaultConstructor(cc));

        Class<?> clazz = cc.toClass();
        Object o1 = clazz.newInstance();

        invokePrint(clazz, o1, "first");

        clazz.getField("now").set(null, LocalTime.now().toString());  // static-field 可以直接设置
        invokePrint(clazz, o1, "second");

        // clazz.getField("property");  // "java.lang.NoSuchFieldException: property", 非static-field
        Field property = clazz.getDeclaredField("property");
        property.setAccessible(true);  // IllegalAccessException...with modifiers "private"
        property.set(o1, "vergilyn");  // 设置对象 o1.property = "vergilyn";

        invokePrint(clazz, o1, "third");

        Object o2 = clazz.newInstance();
        invokePrint(clazz, o2, "fourth");

        outputClassFile(cc);

    }

    private static void invokePrint(Class<?> clazz, Object o, Object... args)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        clazz.getDeclaredMethod("print", String.class)
                .invoke(o, args);
    }

    private static void outputClassFile(CtClass cc) throws CannotCompileException, IOException {
        String directory = System.getProperty("user.dir");
        String path = directory
                + File.separator + "javassist-examples"
                + File.separator + "src"
                + File.separator + "test"
                + File.separator + "java";

        cc.writeFile(path);  // 输出class目录，不包含package路径！
        System.out.println("class output directory >>>> " + path);
    }
}
