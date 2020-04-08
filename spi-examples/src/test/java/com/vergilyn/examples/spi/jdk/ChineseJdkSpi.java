package com.vergilyn.examples.spi.jdk;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class ChineseJdkSpi implements JdkSpi {

    @Override
    public void print() {
        print("chinese");
    }
}
