package com.vergilyn.examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.testng.annotations.Test;

/**
 * Pass By Value: 值传递
 * Pass By Reference: 引用传递
 *
 * java一般认为只存在 值传递。不要过于纠结这概念/定义！
 * @author vergilyn
 * @date 2020-05-11
 */
public class PassByValueTestng {

    /**
     * 94、当一个对象被当作参数传递到一个方法后，此方法可改变这个对象的属性，并可返回变化后的结果，
     * 那么这里到底是值传递还是引用传递？
     * <pre>
     *   是值传递。
     *   Java 语言的方法调用只支持参数的值传递。当一个对象实例作为一个参数被传递到方法中时，参数的值就是对该对象的引用。
     *   对象的属性可以在被调用过程中被改变，但对对象引用的改变是不会影响到调用者的。
     *   C++和 C#中可以通过传引用或传输出参数来改变传入的参数的值。
     *   在 C#中可以编写如下所示的代码，但是在 Java 中却做不到。
     * </pre>
     *
     * 备注：
     * <p>
     * Java 中没有传引用实在是非常的不方便，这一点在 Java 8 中仍然没有得到改进，
     * 正是如此在Java 编写的代码中才会出现大量的 Wrapper 类（将需要通过方法调用修改的引用置于一个 Wrapper 类
     * 中，再将 Wrapper 对象传入方法），这样的做法只会让代码变得臃肿，
     * 尤其是让从 C 和 C++转型为Java 程序员的开发者无法容忍。
     */
    @Test
    public void test(){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        String str = "str";

        System.out.printf("%s before `change(...)` >>>> str = %s \r\n", method, str); // str = "str"
        change(str);
        System.out.printf("%s after `change(...)` >>>> str = %s \n", method, str);  // str = "str"

    }

    private void change(String str){
        String method = "change(String str)";
        System.out.printf("%s >>>> param.str = %s \r\n", method, str); // str = "str"

        str += "_change";

        System.out.printf("%s `str` assign after >>>> str = %s \r\n", method, str); // str = "str_change"
    }

    @Test
    public void obj(){
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        ParamDto dto = new ParamDto("str");

        System.out.printf("%s before `change(...)` >>>> ParamDto.str = %s \r\n", method, dto.str); // str = "str"
        change(dto);
        System.out.printf("%s after `change(...)` >>>> ParamDto.str = %s \n", method, dto.str);  // str = "str_change"

    }

    private void change(ParamDto param){
        String method = "change(ParamDto param)";
        System.out.printf("%s >>>> param.ParamDto.str = %s \r\n", method, param.str); // str = "str"

        param.str += "_change";

        System.out.printf("%s `str` assign after >>>> ParamDto.str = %s \r\n", method, param.str); // str = "str_change"

        param = new ParamDto("change");  // 无法改变 参数的引用，所以java可以不存在 "引用传递"，或者是 "特殊的引用传递"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class ParamDto {
        private String str;
    }
}
