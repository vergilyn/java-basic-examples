package com.vergilyn.examples.format;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class ThymeleafReplaceTest {

    /**
     * TemplateEngine 实例是线程安全的，可以在多个线程和/或请求中共享。
     * 在大多数情况下，你应该在应用程序的生命周期中只创建一个 TemplateEngine 实例。
     */
    private final TemplateEngine templateEngine = new TemplateEngine();

    @BeforeEach
    public void beforeEach(){
        // templateEngine.addTemplateResolver(new StringTemplateResolver());
    }

    @Test
    void testBasic(){
        Context context = new Context();
        context.setVariable("name", "Thymeleaf");

        String result = templateEngine.process("Hello [[${name}]]!", context);

        Assertions.assertThat(result).isEqualTo("Hello Thymeleaf!");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "true,User is of type Administrator",
            "false,User is of type Unknown"
    }, delimiter = ',')
    void testBooleanExpr(boolean isAdmin, String expected){
        String text = "User is of type [[(${isAdmin} ? 'Administrator' : 'Unknown')]]";

        TemplateEngine templateEngine = new TemplateEngine();

        Context context = new Context();
        context.setVariable("isAdmin", isAdmin);

        String result = templateEngine.process(text, context);

        Assertions.assertThat(result).isEqualTo(expected);
    }

    @Test
    void testInvokeStaticMethod(){

    }

    @Test
    void testInvokeObjectMethod(){

    }
}
