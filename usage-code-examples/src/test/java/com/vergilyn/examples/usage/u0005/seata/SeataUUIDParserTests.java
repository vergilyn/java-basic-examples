package com.vergilyn.examples.usage.u0005.seata;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

class SeataUUIDParserTests {
	private static final Long WORK_ID = 124L;

	/**
	 * <pre>
	 *   highest 1 bit: always 0
	 *   next   10 bit: workerId
	 *   next   41 bit: timestamp
	 *   lowest 12 bit: sequence
	 * </pre>
	 *
	 * <pre>
	 *   workId = 124
	 *   newestTimestamp = currentTimeMillis(1625823977787) - twepoch(1588435200000) = 37388777787
	 *
	 *   timestampAndSequence = timestamp << 12
	 *
	 *   next = timestampAndSequence.incrAndGet() = 153144433815553   (sequence = 1)
	 *
	 *   #  mask that help to extract timestamp and sequence from a long
	 *   timestampWithSequence = next & (timestampAndSequenceMask = ~(-1L << (timestampBits + sequenceBits))) = 153144433815553
	 *
	 *   uuid = workerId | timestampWithSequence = 1116892707587883008 | 153144433815553 = 1117045852021698561
	 * </pre>
	 *
	 */
	private static final Long UUID = 1117045852021698561L;

	/**
	 * <a href="https://blog.csdn.net/weixin_44235861/article/details/90112445">java取出一个字节的某几位</a>
	 */
	@Test
	public void parser() {
		// workId 前10位
		long workId = UUID >> 53;  // 53 = (64 - 1 - 10)
		System.out.println("workId: " + workId);

		// timestamp 中间41位
		long timestamp = UUID & 0x1FFFFFFFFFF000L;
		timestamp = timestamp >> 12;
		System.out.println("timestamp: " + timestamp);

		// twepoch = 1588435200000L
		Date newestTimestamp = new Date(timestamp + 1588435200000L);
		System.out.println("newestTimestamp: " + DateFormatUtils.format(newestTimestamp, "yyyy-MM-dd HH:mm:ss"));


		// sequence 后12位；0xFFF -> 0...0,1111,1111,1111
		long sequence = UUID & 0xFFF;
		System.out.println("sequence: " + sequence);
	}

	@Test
	public void calc(){
		int begin = 11, end = 51;
		String bin = "";
		for (int i = 0; i < 64; i++) {
			if (i < begin || i > end){
				bin += "0";
			}

			if (i >= begin && i <= end){
				bin += "1";
			}
		}

		System.out.println(bin);
		System.out.println(Long.parseLong(bin, 2));
		System.out.println(Long.MAX_VALUE);
		System.out.println(Long.toHexString(Long.parseLong(bin, 2)));
	}

	/**
	 * <a href="https://cloud.tencent.com/developer/article/1400407">java 的位运算符</a>
	 */
	@Test
	public void bit(){
		System.out.println("1: " + Integer.toBinaryString(1));

		// -1: 11111111111111111111111111111111
		System.out.println("-1: " + Integer.toBinaryString(-1));

		// min_value: 10000000000000000000000000000000
		System.out.println("min_value: " + Integer.toBinaryString(Integer.MIN_VALUE));

		// "min_value >> 31: -1  （右移，不改变正负）
		System.out.println("min_value >> 31: " + (Integer.MIN_VALUE >> 31));

		// min_value >>> 31: 1 （无符号右移，改变正负）
		System.out.println("min_value >>> 31: " + (Integer.MIN_VALUE >>> 31));

		// 1 其实没改变
		System.out.println("32 % 32: " + (32 % 32));
		System.out.println("1 >> 0: " + (1 >> 0));
		System.out.println("1 >> 32: " + (1 >> 32));
		// 等价于  1 >> (33 % 32 = 1)
		System.out.println("1 >> 1: " + (1 >> 1));
		System.out.println("1 >> 33: " + (1 >> 33));

	}
}
