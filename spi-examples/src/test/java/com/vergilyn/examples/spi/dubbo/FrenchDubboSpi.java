package com.vergilyn.examples.spi.dubbo;


/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class FrenchDubboSpi implements DubboSpi {
    @Override
    public void print() {
        print("french");
    }
}
