package com.vergilyn.examples;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author VergiLyn
 * @date 2020-01-19
 */
@Slf4j
public class GCTestng extends AbstractTestng {

    @Test
    public void test(){
        System.gc();
        sleep(1, "before-init-list, manual invoke GC");

        int size = 100_000;
        List<Integer> list = Lists.newArrayList(size);
        for (int i = 0; i < size; i++){
            list.add(i);
        }

        System.gc();
        sleep(1, "after-init-list, manual invoke GC");  // P1


        List<Integer> list2 = Lists.newArrayList(size);
        list.forEach(e -> {
            list2.add(new Integer(e));
        });

        System.gc();
        log.info("list.size: {}", list.size());  // code-1
      //  log.info("list2.size: {}", list2.size());  // code-2
        sleep(1, "manual invoke GC");  // P2
    }

}
