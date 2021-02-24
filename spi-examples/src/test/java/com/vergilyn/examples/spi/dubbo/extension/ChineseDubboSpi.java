package com.vergilyn.examples.spi.dubbo.extension;

import lombok.Getter;
import lombok.Setter;
import org.apache.dubbo.common.URL;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class ChineseDubboSpi implements DubboSpi {
    public static final String NAME = "chinese";

    @Setter
    @Getter
    private DubboSpi dubboSpi;

    @Override
    public void print() {
        print(NAME);
    }

    @Override
    public void print(URL url) {
        print(NAME, url);
    }

}
