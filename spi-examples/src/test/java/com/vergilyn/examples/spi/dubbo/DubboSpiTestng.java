package com.vergilyn.examples.spi.dubbo;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.URLBuilder;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.SPI;
import org.testng.annotations.Test;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class DubboSpiTestng {
    private ExtensionLoader<DubboSpi> extensionLoader = ExtensionLoader.getExtensionLoader(DubboSpi.class);;

    @Test
    public void getExtension(){
        // 获取扩展类实现，**按需获取**
        DubboSpi dubboSpi = extensionLoader.getExtension("english");
        dubboSpi.print();
    }

    /**
     * {@linkplain SPI#value()} 中指定的值，获取默认的扩展类的实现
     */
    @Test
    public void defaultExtension(){
        // 获取扩展类实现，**按需获取**
        DubboSpi dubboSpi = extensionLoader.getDefaultExtension();
        dubboSpi.print();
    }

    /**
     * 根据 {@linkplain Adaptive#value()} 指定的 参数名，从{@linkplain URL}中获取value并加载扩展实现类。<br/>
     * 获取规则详见：{@linkplain Adaptive#value()}
     * @see {@linkplain Adaptive}
     */
    @Test
    public void getAdaptiveExtension(){
        DubboSpi dubboSpi = extensionLoader.getAdaptiveExtension();

        URL url = new URLBuilder("http", "127.0.0.1", 8080)
               // .addParameter("k1", "chinese")
                .addParameter("k2", "english")
                .build();

        dubboSpi.print(url);
    }
}
