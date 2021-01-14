package com.vergilyn.examples.spi.dubbo.extension;

import org.apache.dubbo.common.URL;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class ChineseDubboSpi implements DubboSpi {
    private static final String NAME = "chinese";

    @Override
    public void print() {
        print(NAME);
    }

    @Override
    public void print(URL url) {
        print(NAME, url);
    }
}
