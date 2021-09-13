package com.vergilyn.examples.usage.u0008;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * <p>
 * 1) 一般算法 <br/>
 * 生成一个列表，分成几个区间，例如列表长度100，1-20是靴子的区间，21-45是披风的区间等，<br/>
 * 然后随机从100取出一个数，看落在哪个区间。<br/>
 * 算法时间复杂度：预处理O(MN)，随机数生成O(1)，空间复杂度O(MN)，其中N代表物品种类，M则由最低概率决定。<br/>
 * </p>
 *
 * <p>
 * 2) 离散算法（一般算法的改进）<br/>
 * 竟然1-20都是靴子，21-45都是披风，那抽象成小于等于20的是靴子，大于20且小于等于45是披风，就变成几个点[20,45,55,60,100]，<br/>
 * 然后也是从1到99随机取一个数R，按顺序在这些点进行比较，知道找到第一个比R大的数的下标，<br/>
 * 比一般算法减少占用空间，还可以采用二分法找出R，这样，预处理O(N)，随机数生成O(logN)，空间复杂度O(N)。<br/>
 * 请点击查看详细：http://www.cnblogs.com/miloyip/archive/2010/04/21/1717109.html
 * </p>
 *
 * @author vergilyn
 * @since 2021-09-03
 */
public class ProbabilityHitTests {

	/**
	 * 实现很容易理解。建议将概率转换成int，只需要提前约定概率精确到百分比多少位。
	 */
	@Test
	public void test(){
		// `value = 0` 不应该被命中
		// 特别注意：首位是0，末位是1 会影响`step >= random`判断
		final int[] percentages = {0, 10, 30, 5, 8, 0, 41, 80, 1};

		// 足够多次后，actual-percent 无限趋近 expected-percent
		int count = 1_000_000;
		int[] stats = new int[percentages.length];
		for (int i = 0; i < count; i++) {
			stats[hitIndex(percentages)] += 1;
		}

		int total = IntStream.of(percentages).sum();
		for (int i = 0; i < stats.length; i++) {
			System.out.printf("index: %d, value: %d, expected-percent: %.6f,\t hits: %d, actual-percent: %.6f \n", i, percentages[i],
					percentages[i] * 1.0 / total, stats[i], stats[i] * 1.0 / count);

		}
	}

	/**
	 * 时间复杂度： total=O(N), random=O(1), hit=O(N)
	 *
	 * 根据“离散算法”思路，可以将`hit`改成二分法查找。但是数量不够大时，这种遍历代码更容易理解。
	 */
	private int hitIndex(int[] percentages){
		int length = percentages.length;
		int total = IntStream.of(percentages).sum();

		// `nextInt`范围是 [0, total)，如果得到 0，且 percentages[0] = 0 此时不应该命中。
		// 且不会随机到 total，如果 `percentages[length-1] = 1`时，无法被命中
		// 所以需要 `1 + random.nextInt(total)`
		int random = 1 + new Random().nextInt(total);

		int step = 0;
		for (int i = 0; i < length; i++) {
			step += percentages[i];

			// 考虑 首位是0，"random = 0"，此不应该成立。 所以可以保证 "random >= 1"
			if (step >= random){
				return i;
			}
		}

		return -1;
	}

	/**
	 */
	private int binaryHitIndex(int[] percentages){
		int length = percentages.length;
		int total = 0;
		int[] sections = new int[length];
		for (int i = 0; i < length; i++) {
			total += percentages[i];
			sections[i] = total;
		}

		// `nextInt`范围是 [0, total)，如果得到 0，且 percentages[0] = 0 此时不应该命中。
		// 且不会随机到 total，如果 `percentages[length-1] = 1`时，无法被命中
		// 所以需要 `1 + random.nextInt(total)`
		int random = 1 + new Random().nextInt(total);

		// TODO hitIndex 代码相对复杂了一点
		return binarySearch(sections, random);
	}

	@Test
	public void validBinarySearch(){
		int[] arrays = {0, 3, 5};

		// Assertions.assertEquals(1, binarySearch(arrays, 2));

		// FIXME 2021-09-03
		// Expected: 2, Actual: 3.
		// cause: 二分从 7/2=3， 刚好 a[2]=a[3]=searchValue， 此时期望的是a[2]（并且 a[3] 任何时候都不应该命中）
		//   需要比较前N个元素，因为存在可能 {0, 3, 5, 5, 5, 5, 8, 8, 10} 9/2=4, 但期望的还是 `a[2]`
		// XXX 实在不好理解， 还是提前处理掉 概率是0 的数据，相对处理起来会方便一点。 （数量较少真没必要`二分查找`）
		Assertions.assertEquals(2, binarySearch(arrays, 1));
	}

	/**
	 * XXX 2021-09-06, 感觉性能不一定比遍历高，一部分是因为实现代码的问题，执行了一些多余的判断 比如每次都要判断`mid == 0`
	 *
	 * @param searchValue `>= 1`
	 *
	 * @return -1, 未找到匹配的值。
	 *
	 * @see java.util.Arrays#binarySearch(int[], int)
	 * @see java.util.Collections#binarySearch(List, Object, Comparator)
	 */
	private int binarySearch(int[] arrays, int searchValue) {
		int low = 0;
		int high = arrays.length - 1;

		int mid, midVal, midPrevVal;
		while (low <= high) {
			mid = (low + high) >>> 1;
			midVal = arrays[mid];

			// XXX 每次都要判断`mid == 0`
			if (mid == 0){
				return midVal >= searchValue ? mid : -1;
			}
			// XXX 每次都要取 前一个值
			midPrevVal = arrays[mid - 1];

			if (midPrevVal < searchValue && searchValue <= midVal){
				return mid;
			}

			if (midVal < searchValue){
				// mid = [mid + 1, length)
				low = mid + 1;

			} else {
				// midVal >= searchValue
				// `=`情况，可能存在：[mid] = [mid-1] = [mid-2] = [mid-n]  此时需要取最小的一个（意味着很多概率是0）
				// mid = [0, mid - 1]
				high = mid - 1;
			}
		}

		return -1;  // key not found
	}
}
