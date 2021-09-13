package com.vergilyn.examples.usage.u0008;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Sets;

import org.junit.jupiter.api.Test;

public class CheatWithDateJdkRandomTests {

	/**
	 * @see Random#Random()
	 */
	private static final Long RANDOM_NUM = 181783497276652981L;
	private static final AtomicInteger INCR = new AtomicInteger(1);

	/**
	 *
	 * @see org.apache.commons.lang3.RandomUtils#nextInt(int, int)
	 * @see java.util.Random#nextInt(int)
	 * @see java.util.Random#next(int)
	 */
	@Test
	public void usage(){
		int id1 = 10086;
		int id2 = 1477;

		long len = 162;
		int min = 50, max = 200;
		int dailyIncr = 0;
		Random random = new Random();
		Set<Integer> incrs = Sets.newLinkedHashSet();
		final long currentTimeMillis = System.currentTimeMillis();
		for (long i = 0; i < len; i++) {
			/**
			 * 例如 `10086 + i` 这种简单的seed 生成的随机数基本都是：一大一小，且比较有规律
			 * 所以可以参考 {@link Random#Random()}
			 */
			// random.setSeed((i + 1));
			random.setSeed(id1 * id2 * (i + 1));  // 162, 91
			// random.setSeed(id1 * id2 * (i + 1) * 99991);  // 162, 108
			// random.setSeed(id1 * id2 * (i + 1) * RANDOM_NUM);  // 162, 110
			dailyIncr = min + random.nextInt(max - min);

			incrs.add(dailyIncr);
		}
		System.out.printf("len: %d, incrs: %d, %s \n", len, incrs.size(), incrs);
		// System.out.printf("date: %s, incr: %d, count: %d\n", nowDate, dailyIncr, dayTotal);
	}

}
