package com.vergilyn.examples.usage.u0015;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author dingmaohai
 * @version v1.0
 * @since 2023/08/17 11:47
 *
 * @see io.micrometer.core.instrument.distribution.TimeWindowMax
 */
public class TimeWindowMaxTest {

    @Nested
    static class UpdateMaxTest {

        @Test
        void test_UpdateMax(){
            AtomicLong[] ringBuffer = {
                    new AtomicLong(100),
                    new AtomicLong(300),
                    new AtomicLong(80),
                    new AtomicLong(20),
                    new AtomicLong(120)
            };

            long sample = 150;
            for (AtomicLong max : ringBuffer) {
                updateMax(max, sample);
            }

            for (int i = 0; i < ringBuffer.length; i++) {
                System.out.printf("[%d] = %d\n", i, ringBuffer[i].get());
            }
        }

        /**
         * 没看懂为什么需要 do-while。实际逻辑就是：如果 `sample > max`，则 `max.set(sample)`
         * <p> 原因：目的是确保在多线程环境下，能够成功更新最大值，并且保证最终的最大值是正确的。
         */
        private void updateMax(AtomicLong max, long sample) {
            long curMax;
            do {
                curMax = max.get();
            }
            while (curMax < sample && !max.compareAndSet(curMax, sample));
        }
    }
}
