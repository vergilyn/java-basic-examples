package com.vergilyn.examples.spi.dubbo;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class DubboSpiTestng {
    private ExtensionLoader<DubboSpi> extensionLoader;

    @BeforeTest
    public void beforeTest(){
        extensionLoader = ExtensionLoader.getExtensionLoader(DubboSpi.class);
    }

    @Test
    public void getExtension(){
        // 获取扩展类实现，**按需获取**
        DubboSpi dubboSpi = extensionLoader.getExtension("english");
        dubboSpi.print();
    }

    @Test
    public void defaultExtension(){
        // 获取扩展类实现，**按需获取**
        DubboSpi dubboSpi = extensionLoader.getDefaultExtension();
        dubboSpi.print();
    }

    @Test
    public void getAdaptiveExtension(){
        DubboSpi dubboSpi = extensionLoader.getAdaptiveExtension();
        dubboSpi.print();
    }
}
