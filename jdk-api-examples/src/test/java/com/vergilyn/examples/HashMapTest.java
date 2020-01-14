package com.vergilyn.examples;

import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @date 2020-01-06
 */
public class HashMapTest {

    @Test
    public void hashmap1(){
        //-1996137828
        //10001001000001010101101010011100
        String key = "vergilyn";
        int keyHashCode = key.hashCode();
        System.out.println(keyHashCode);
        System.out.println(keyHashCode >>> 16);

        System.out.println(keyHashCode ^ (keyHashCode >>> 16));
        System.out.println(Integer.toBinaryString(-1996106855));
    }
}
