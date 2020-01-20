package com.vergilyn.examples;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * 1. 静态成员变量GC问题
 * 2.
 * @author VergiLyn
 * @date 2020-01-19
 */
@Slf4j
public class GCTestng extends AbstractTestng {

    @Test
    public void test(){
        log.info("11111111111");
    }

}
