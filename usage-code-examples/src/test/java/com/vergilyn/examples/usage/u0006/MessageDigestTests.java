package com.vergilyn.examples.usage.u0006;

import java.security.MessageDigest;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-07-21
 *
 * @see <a href="https://www.cnblogs.com/colorfulkoala/p/5783556.html">Redis百亿级Key存储方案</a>
 * @see <a href="https://baike.baidu.com/item/ASCII/309296">百度百科ASCII</a>
 */
public class MessageDigestTests {

	@Test
	public void test(){
		String key = "redis:set:409839163";
		byte[] bytes = getBucketId(key.getBytes(), 33);


		System.out.println(new String(bytes));
	}

	/**
	 * <pre>
	 * 我们通常使用的md5是32位的hexString（16进制字符），它的空间是128bit，这个量级太大了，
	 * 我们需要存储的是百亿级，大约是33bit(2^33 = 8,589,934,592)，所以我们需要有一种机制计算出合适位数的散列，
	 * 而且<b>为了节约内存</b>，我们需要<b>利用全部字符类型（ASCII码在0~127之间）来填充</b>，而不用HexString，这样Key的长度可以缩短到一半。
	 * </pre>
	 *
	 * 备注：
	 * 1) 0~127的ASCII可读性太低了。
	 */
	@SneakyThrows
	private byte[] getBucketId(byte[] key, int bit) {
		// key[19]: [114, 101, 100, 105, 115, 58, 115, 101, 116, 58, 52, 48, 57, 56, 51, 57, 49, 54, 51]
		MessageDigest mdInst = MessageDigest.getInstance("MD5");
		mdInst.update(key);

		// md[16]: [81, -32, 39, -44, -97, 6, 82, -106, -51, 5, 112, 77, 107, 62, 84, -102]
		byte[] md = mdInst.digest();

		// ASCII规定：使用 7位二进制数（剩下的1位二进制为0，或者作为`奇偶校验位`）来表示指定的字符。
		// 原文：因为一个字节中只有7位能够表示成单字符
		// vergilyn: 理解不能，但知道意思，比如
		//   33bits <= 5 * (8 - 1) = 35
		//   35bits <= 5 * (8 - 1) = 35，最终都需要 5 bytes来表示
		//   36bits <= 6 * (8 - 1) = 42;
		byte[] result = new byte[(bit - 1) / 7 + 1]; // byte[5]

		// XXX 2021-07-22，含义？（结合cnblogs原文，理解一下？反正现在理解不能）
		// `Math#pow` = 2^(bit % 7)
		int a = (int) Math.pow(2, bit % 7) - 2;  // a = 30
		// md[4] = -97 & 30 = 30; [81, -32, 39, -44, 30, ...]
		md[result.length - 1] = (byte) (md[result.length - 1] & a);

		System.arraycopy(md, 0, result, 0, result.length);

		for (int i = 0; i < result.length; i++) {
			if (result[i] < 0){
				result[i] &= 127;  // 为了节约内存，利用全部字符类型（ASCII码在0~127之间）来填充
			}
		}

		return result;
	}

	@Test
	public void confused(){
		//
		// 对于未出现的桶也是存在一定量的，如果过多会导致规划不准确，其实数量是符合二项分布的，对于2^30桶存储2^32kv，不存在的桶大概有（百万级别，影响不大）：
		double rs = Math.pow((1 - 1.0 / Math.pow(2, 30)), Math.pow(2, 32)) * Math.pow(2, 30);
		System.out.println(rs);  // 1.9666267471483495E7

		int a = -97 & 30;
		System.out.println(a);  // 30

		int b = -5 & 4;
		System.out.println(b);  // 0

	}
}
