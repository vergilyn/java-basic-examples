package com.vergilyn.examples.usage.u0008;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

/**
 * Fisher–Yates shuffle 洗牌算法：是一个<b>非常高效</b> 又 <b>公平</b> 的 <b>随机排序</b> 算法
 * <pre>
 *   原理：（计算机中优化过时间复杂的算法原理，不是原始的算法原理）
 *   假设现在存在有限集合 arr = {1...N}，arr.length = M，排列组合的可能性是`M * (M-1) * (M-2)...1 = M!`。（代码层面`1`这次是没有意义的）
 *     第1次，取 1到M 的随机数 得到X，将arr集合的第 X元素与 最后一个元素（arr[M - 1]）交换。
 *     第2次，取 1到M-1 的随机数 得到X，将arr集合的第 X元素与 最后一个元素（arr[M - 2]）交换。
 *     依次类推。
 * </pre>
 *
 * @author vergilyn
 * @since 2021-09-02
 *
 * @see <a href="Fisher-Yates Shuffle算法 （洗牌算法）">https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle</a>
 * @see <a href="Fisher–Yates shuffle 洗牌算法">https://gaohaoyang.github.io/2016/10/16/shuffle-algorithm/</a>
 * @see java.util.Collections#shuffle(List)
 */
public class FisherYatesShuffleTests {

	@Test
	public void valid(){
		Integer[] arrays = {1, 2, 3, 4, 5, 6};

		shuffle(arrays);

		System.out.println(Arrays.toString(arrays));
	}

	/**
	 * @see java.util.Collections#shuffle(List)
	 */
	public static void shuffle(Object[] array){
		int length = array.length;

		Random random = new Random();
		// nextInt = [0, i)，所以`i > 1`才有意义，只会交换 index= [length - 1, 1] 的数据
		int randomIndex;
		for (int i = length; i > 1; i--) {
			randomIndex = random.nextInt(i);
			swap(array, i - 1, randomIndex);

			System.out.printf("end: %d, random: %d\n", i - 1, randomIndex);
		}
	}

	private static void swap(Object[] arr, int i, int j) {
		Object tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}
}
