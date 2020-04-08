package com.vergilyn.examples.spi.dubbo;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
@org.apache.dubbo.common.extension.SPI("chinese")
public interface DubboSpi {

    void print();

    default void print(String msg){
        System.out.println("dubbo-spi >>>> " + msg);
    }
}
