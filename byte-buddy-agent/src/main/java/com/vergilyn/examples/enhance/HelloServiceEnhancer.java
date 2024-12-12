package com.vergilyn.examples.enhance;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class HelloServiceEnhancer {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        new AgentBuilder.Default()
                .type(ElementMatchers.nameContains("HelloService"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
                    return builder.method(ElementMatchers.named("hello"))
                                  .intercept(Advice.to(HelloServiceAdvice.class));
                })
                .installOn(instrumentation);
    }
}
