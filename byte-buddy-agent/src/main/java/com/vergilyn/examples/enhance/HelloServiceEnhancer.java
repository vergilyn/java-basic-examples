package com.vergilyn.examples.enhance;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class HelloServiceEnhancer {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("[Agent] >>>> " + HelloServiceEnhancer.class.getName());

        new AgentBuilder.Default()
                .type(ElementMatchers.nameContainsIgnoreCase("HelloService"))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) -> {
                    return builder.method(ElementMatchers.any())
                                  // .intercept(Advice.to(HelloServiceAdvice.class));
                                  .intercept(MethodDelegation.to(new HelloServiceAdvice()));
                })
                .installOn(instrumentation);
    }

}
