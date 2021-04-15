package com.vergilyn.examples.guava.hash;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @date 2019-06-19
 */
public class BloomFilterTest {
    private static final int NUM_PUTS = 100_000;
    private static final String pattern = "aaa:bbb:";

    @Test
    public void basic(){
        // 4000_0000, 28s, 32 bytes
        // 1_0000
        int numInsertions = 1000_0000;

        // 默认0.03
        BloomFilter<String> bf = BloomFilter.create(Funnels.unencodedCharsFunnel(), numInsertions, 0.003);

        for (int i = 0; i < numInsertions; i++) {
            bf.put(pattern + i);
        }

        System.out.println(bf.approximateElementCount());
/*
        System.out.println(bf.mightContain("bmd:uid:" + 1000_0001));    // false
        System.out.println(bf.mightContain("bmd:uid:" + 1000_0002));    // true
        System.out.println(bf.mightContain("bmd:uid:" + 1000_0003));    // false
        System.out.println(bf.mightContain("bmd:uid:" + 1000_0004));    // false

        List<String> error = Lists.newArrayList();
        String str;
        for (int i = 0; i < 10000; i++){
            str = pattern + (numInsertions + i);
            if (bf.mightContain(str)){
                error.add(str);
            }
        }

        System.out.println(error.size() + ": " + error);*/
    }

    @Test
    public void visualVM() throws InterruptedException {
        List<String> list = Lists.newArrayList();

        for (int i = 0; i < 1000_0000; i++){
            list.add(pattern + i);
        }

        System.out.println(list.size());

        /* value = aaa:bbb:12345, 13 bytes
         * 13 * 1000_0000 / 1024 / 1024 ≈ 124MB
         */

        Thread.sleep(1000);
    }
}
