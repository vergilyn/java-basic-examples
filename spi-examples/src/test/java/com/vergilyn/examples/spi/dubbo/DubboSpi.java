package com.vergilyn.examples.spi.dubbo;

import org.apache.dubbo.common.extension.Adaptive;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
@org.apache.dubbo.common.extension.SPI("chinese")
public interface DubboSpi {

    void print();

    @Adaptive({"k1", "k2"})
    void print(org.apache.dubbo.common.URL url);

    default void print(String msg){
        System.out.println("dubbo-spi >>>> " + msg);
    }

    default void print(String name, org.apache.dubbo.common.URL url){
        System.out.printf("dubbo-spi >>>> %s, %s \r\n", name, url);
    }
}
