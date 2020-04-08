package com.vergilyn.examples.spi.jdk;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public interface JdkSpi {

    void print();

    default void print(String msg){
        System.out.println("jdk-spi-print >>>> " + msg);
    }
}
