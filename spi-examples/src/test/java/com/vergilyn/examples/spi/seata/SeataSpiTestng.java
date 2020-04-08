package com.vergilyn.examples.spi.seata;

import java.util.List;

import org.testng.annotations.Test;

/**
 * The type Enhanced service loader test.
 *
 * @author Otis.z
 */
public class SeataSpiTestng {

    /**
     * Test load by class and class loader.
     */
    @Test
    public void testLoadByClassAndClassLoader() {
        SeataSpi load = EnhancedServiceLoader.load(SeataSpi.class, SeataSpi.class.getClassLoader());
        System.out.println(load.say());
        // Assertions.assertEquals(load.say(), "Bonjour");
    }

    /**
     * Test load exception.
     */
    @Test(expectedExceptions = EnhancedServiceNotFoundException.class)
    public void testLoadException() {
        SeataSpiTestng load = EnhancedServiceLoader.load(SeataSpiTestng.class);
    }

    /**
     * Test load by class.
     */
    @Test
    public void testLoadByClass() {
        SeataSpi load = EnhancedServiceLoader.load(SeataSpi.class);
        System.out.println(load.say());
    }

    /**
     * Test load by class and activate name.
     */
    @Test
    public void testLoadByClassAndActivateName() {
        SeataSpi englishHello = EnhancedServiceLoader.load(SeataSpi.class, "EnglishHello");
        System.out.println(englishHello.say());
        // assertThat(englishHello.say()).isEqualTo("hello!");
    }

    /**
     * Test load by class and class loader and activate name.
     */
    @Test
    public void testLoadByClassAndClassLoaderAndActivateName() {
        SeataSpi englishHello = EnhancedServiceLoader
                .load(SeataSpi.class, "EnglishHello", SeataSpiTestng.class.getClassLoader());

        System.out.println(englishHello.say());
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
    }

    /**
     * Gets all extension class 1.
     */
    @Test
    public void getAllExtensionClass1() {
        List<Class> allExtensionClass = EnhancedServiceLoader
                .getAllExtensionClass(SeataSpi.class, ClassLoader.getSystemClassLoader());

        System.out.println(allExtensionClass.size());
    }

}
