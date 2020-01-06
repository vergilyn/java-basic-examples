package com.vergilyn.examples;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @date 2020-01-02
 */
public class ListsTest {

    @Test
    public void test(){
        List<Integer> list = Lists.newLinkedList();
        list.add(1);
        list.add(2);
        for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext();) {
            iterator.next();
        }
    }
}
