package com.vergilyn.examples.spi.seata;

import java.util.List;

import com.vergilyn.examples.spi.seata.extension.EnglishSeataSpi;
import com.vergilyn.examples.spi.seata.extension.SeataSpi;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * The type Enhanced service loader test.
 *
 * @author Otis.z
 */
public class SeataSpiTestng {

    /**
     * Test load exception.
     */
    @Test(expectedExceptions = EnhancedServiceNotFoundException.class)
    public void testLoadException() {
        SeataSpiTestng load = EnhancedServiceLoader.load(SeataSpiTestng.class);
    }

    /**
     * Test load by class and class loader.
     */
    @Test
    public void testLoadByClassAndClassLoader() {
        SeataSpi spi = EnhancedServiceLoader.load(SeataSpi.class, SeataSpi.class.getClassLoader());
        System.out.println(spi.say());

        Assert.assertTrue(spi.say().contains(Integer.MAX_VALUE + ""));
    }

    /**
     * Test load by class.
     */
    @Test
    public void testLoadByClass() {
        SeataSpi spi = EnhancedServiceLoader.load(SeataSpi.class);
        System.out.println(spi.say());

        Assert.assertTrue(spi.say().contains(Integer.MAX_VALUE + ""));
    }

    /**
     * Test load by class and activate name.
     */
    @Test
    public void testLoadByClassAndActivateName() {
        SeataSpi englishHello = EnhancedServiceLoader.load(SeataSpi.class, EnglishSeataSpi.NAME);

        System.out.println(englishHello.say());
        Assert.assertTrue(englishHello.say().contains("english"));
    }

    /**
     * Test load by class and class loader and activate name.
     */
    @Test
    public void testLoadByClassAndClassLoaderAndActivateName() {
        SeataSpi englishHello = EnhancedServiceLoader
                .load(SeataSpi.class, EnglishSeataSpi.NAME, SeataSpiTestng.class.getClassLoader());

        System.out.println(englishHello.say());

        Assert.assertTrue(englishHello.say().contains("english"));
    }

    /**
     * Gets all extension class.
     */
    @Test
    public void getAllExtensionClass() {
        List<Class> allExtensionClass = EnhancedServiceLoader.getAllExtensionClass(SeataSpi.class);

        for(Class clazz : allExtensionClass){
            System.out.println(clazz.getSimpleName());
        }

        Assert.assertEquals(allExtensionClass.size(), 3);
    }

    /**
     * Gets all extension class 1.
     */
    @Test
    public void getAllExtensionClass1() {
        List<Class> allExtensionClass = EnhancedServiceLoader
                .getAllExtensionClass(SeataSpi.class, ClassLoader.getSystemClassLoader());

        for(Class clazz : allExtensionClass){
            System.out.println(clazz.getSimpleName());
        }

        Assert.assertEquals(allExtensionClass.size(), 3);
    }

}
