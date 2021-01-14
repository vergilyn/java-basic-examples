package com.vergilyn.examples.spi.dubbo.extension;

import org.apache.dubbo.common.URL;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class FrenchDubboSpi implements DubboSpi {
    private static final String NAME = "french";

    @Override
    public void print() {
        print(NAME);
    }

    @Override
    public void print(URL url) {
        print(NAME, url);
    }

}
