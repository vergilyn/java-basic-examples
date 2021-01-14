package com.vergilyn.examples.spi.dubbo;

import com.vergilyn.examples.spi.dubbo.extension.DubboSpi;

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
    private final ExtensionLoader<DubboSpi> extensionLoader = ExtensionLoader.getExtensionLoader(DubboSpi.class);;

    /**
     * 获取默认的扩展类的实现。{@linkplain SPI#value()} 中指定的值，
     */
    @Test
    public void defaultExtension(){
        DubboSpi dubboSpi = extensionLoader.getDefaultExtension();
        dubboSpi.print();
    }

    /**
     * 获取<b>指定的实例对象</b>
     */
    @Test
    public void getExtension(){
        DubboSpi dubboSpi = extensionLoader.getExtension("english");
        dubboSpi.print();
    }

    /**
     * 根据 {@linkplain Adaptive#value()} 指定的 参数名，从{@linkplain URL}中获取value并加载扩展实现类。<br/>
     * 获取规则详见：{@linkplain Adaptive#value()}
     *
     * @see DubboSpi#print(URL)
     * @see org.apache.dubbo.common.extension.Adaptive
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
