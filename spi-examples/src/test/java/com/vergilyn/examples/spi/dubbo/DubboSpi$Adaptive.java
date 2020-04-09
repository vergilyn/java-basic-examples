package com.vergilyn.examples.spi.dubbo;

import org.apache.dubbo.common.extension.ExtensionLoader;

/**
 * dubbo spi adaptive 生成的"代理类" 参考。
 * @see ExtensionLoader#createAdaptiveExtensionClass()
 */
public class DubboSpi$Adaptive implements com.vergilyn.examples.spi.dubbo.DubboSpi {
    public void print(java.lang.String arg0, org.apache.dubbo.common.URL arg1) {
        throw new UnsupportedOperationException(
                "The method public default void com.vergilyn.examples.spi.dubbo.DubboSpi.print(java.lang.String,org.apache.dubbo.common.URL) of interface com.vergilyn.examples.spi.dubbo.DubboSpi is not adaptive method!");
    }

    public void print(java.lang.String arg0) {
        throw new UnsupportedOperationException(
                "The method public default void com.vergilyn.examples.spi.dubbo.DubboSpi.print(java.lang.String) of interface com.vergilyn.examples.spi.dubbo.DubboSpi is not adaptive method!");
    }

    /**
     * 被 {@linkplain org.apache.dubbo.common.extension.Adaptive} 修饰的方法
     * @param arg0
     */
    public void print(org.apache.dubbo.common.URL arg0) {
        if (arg0 == null)
            throw new IllegalArgumentException("url == null");
        org.apache.dubbo.common.URL url = arg0;
        /* @Adaptive
         *   如果指定`#value()`，取URL中的第一个非空，否则默认为@SPI 指定的默认扩展类
         *   如果未指定，默认生成规则是 class-name转换成 例如 `dubbo.spi`再从url中获取参数
         */
        String extName = url.getParameter("k1", url.getParameter("k2", "chinese"));
        if (extName == null)
            throw new IllegalStateException(
                    "Failed to get extension (com.vergilyn.examples.spi.dubbo.DubboSpi) name from url (" + url
                            .toString() + ") use keys([k1, k2])");
        com.vergilyn.examples.spi.dubbo.DubboSpi extension = (com.vergilyn.examples.spi.dubbo.DubboSpi) ExtensionLoader
                .getExtensionLoader(com.vergilyn.examples.spi.dubbo.DubboSpi.class).getExtension(extName);
        extension.print(arg0);
    }

    public void print() {
        throw new UnsupportedOperationException(
                "The method public abstract void com.vergilyn.examples.spi.dubbo.DubboSpi.print() of interface com.vergilyn.examples.spi.dubbo.DubboSpi is not adaptive method!");
    }
}