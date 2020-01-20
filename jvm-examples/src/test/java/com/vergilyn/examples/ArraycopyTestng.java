package com.vergilyn.examples;

import java.util.Collection;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * 验证：{@linkplain System#arraycopy(Object, int, Object, int, int)} 是否会占用2倍内存 <br/>
 * ({@linkplain java.util.ArrayList#addAll(Collection)} 内部使用的也是`arraycopy`) <br/>
 *
 * 结论：不会。即变量`arrays`、`arraycopy`指向java-heap中的同一块内存区域.
 * @author VergiLyn
 * @date 2020-01-19
 */
@Slf4j
public class ArraycopyTestng extends AbstractTestng {

    @Test
    public void arraycopy() {
        int size = 20_0000, total = 0;
        log.info("begin init `arrays` >>>> size: {}", size);
        String[] arrays = new String[size];
        for(int i = 0; i < size; i++){
            arrays[i] = UUID.randomUUID().toString();
            total += arrays[i].getBytes().length;
        }

        log.info("debug-01 >>>> arrays.bytes.length: {}", total);
        sleep(1);

        log.info("begin arraycopy >>>> ");
        String[] arraycopy = new String[arrays.length];
        System.arraycopy(arrays, 0, arraycopy, 0, arrays.length);

        log.info("debug-02 >>>> arraycopy success！arraycopy[length - 1]: {}", arraycopy[arraycopy.length - 1]);
        sleep(1);
    }

}
