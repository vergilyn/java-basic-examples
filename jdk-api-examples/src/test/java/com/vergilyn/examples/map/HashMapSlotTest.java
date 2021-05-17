package com.vergilyn.examples.map;

import org.testng.annotations.Test;

/**
 * @author vergilyn
 * @date 2020-01-06
 */
public class HashMapSlotTest {

    @Test
    public void slot(){
        // -1996137828
        // 10001001000001010101101010011100
        String key = "vergilyn";
        int keyHashCode = key.hashCode();
        System.out.println(keyHashCode); // -1996137828
        System.out.println(keyHashCode >>> 16); // 35077

        System.out.println(keyHashCode ^ (keyHashCode >>> 16)); // -1996106855
        System.out.println(Integer.toBinaryString(-1996106855));
    }
}
