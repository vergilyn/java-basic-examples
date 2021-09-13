package com.vergilyn.examples.usage.u0008;

import java.time.Duration;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-08-31
 */
public class CheatWithDateTests {

	private final LocalDateTime _startDate = LocalDateTime.of(2021, 8, 1, 12, 34, 56);

	/**
	 * <a href="https://blog.csdn.net/yangchao0510/article/details/98506504">浏览人数/点赞人数 随机稳定增长算法</a>
	 * <pre>
	 *   根据时间流逝，稳定递增算法（假设：每天最多增长156）
	 *
	 *   备注，等差数列求和公式：等差数列的和=(首数+尾数)*项数/2
	 *   1. 获取小时时差（当前时间-创建时间）
	 *   2. 24小时取整 * 156
	 *   3. 24小时取余 ：< 12 则  + ((1+x)*x)/2
	 *                 > 12 则  + 78 +  { ( 1 + ( x-12 ) } * ( x - 12 ) ) / 2
	 *
	 *   最终数值 = (2结果 + 3结果)* n, n 倍数因子 (建议 n = 标题字符数量*0.1)
	 * </pre>
	 */
	@Test
	public void test(){
		int dayMultiple = 156;
		LocalDateTime elapse = _startDate;
		long number;
		for (int i = 0; i < 48; i++) {
			elapse = elapse.plusMinutes(RandomUtils.nextInt(1, 59));
			number = cheatIncr(elapse, dayMultiple);

			System.out.printf("%s: %d\n", elapse, number);
		}
	}

	/**
	 * FIXME `第3条`有问题，没有必要“等差求和”，并且并未带入 `dayMultiple`。
	 * 实质感觉就是 `dayMultiple/24 * hours`，将每天增量平均到 按小时/按分。
	 *
	 * @param now
	 * @param dayMultiple 每天增加量
	 * @return
	 */
	private long cheatIncr(LocalDateTime now, int dayMultiple){
		// 获取小时时差（当前时间-创建时间）
		Duration duration = Duration.between(_startDate, now);
		long hours = duration.toHours();

		long result = hours / 24 * dayMultiple;
		long remainder = hours % 24;

		// XXX 2021-08-31  这个计算有问题。其实大于或小于，其实是一样，且并没有带入 dayMultiple。
		if (remainder < 12){
			result += (1 + remainder) * remainder / 2;
		}else {
			result += dayMultiple / 2 +  ( 1 + (remainder - 12 ) ) * ( remainder - 12 )  / 2;
		}

		return result;
	}
}
