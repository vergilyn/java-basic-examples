package com.vergilyn.examples.spi.jdk;

import java.util.ServiceLoader;

import com.vergilyn.examples.spi.jdk.extension.JdkSpi;

import org.testng.annotations.Test;

/**
 * @author vergilyn
 * @date 2020-04-08
 */
public class JdkSpiTestng {

    /**
     * @see ServiceLoader#load(Class)
     * @see ServiceLoader#load(Class, ClassLoader)
     */
    @Test
    public void test(){
        ServiceLoader<JdkSpi> serviceLoader = ServiceLoader.load(JdkSpi.class);

        // 遍历在配置文件中已配置的 AnimalService 的所有实现类
        for (JdkSpi instance : serviceLoader){
            instance.print();
        }
    }
}
