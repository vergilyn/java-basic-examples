package com.vergilyn.examples.usage.u0005.seata;

import org.junit.jupiter.api.Test;


/**
 * get next UUID(base on snowflake algorithm), which look like:
 * <pre>
 *   highest 1 bit: always 0
 *   next   10 bit: workerId  (限制 [0, 1023])
 *   next   41 bit: timestamp
 *   lowest 12 bit: sequence
 * </pre>
 *
 * 2021-07-09，特别注意：
 *   1) {@linkplain SeataUUIDGenerator#init(Long)} 时已经决定了 timestamp，
 *   后续每次 {@linkplain SeataUUIDGenerator#generateUUID()} 生成的uuid中解析出来的 timestamp始终是 `init`时的 timestamp。
 *   例如，init 时 timestamp=20210709，隔了2天，反解出来的 timestamp还是 20210709。
 *
 *   2) 反解出来的timestamp不一定对，原因如上，如果sequence增加超过了 2^12，意味着会占用timestamp的低位数(13~53)。
 *   这样，反解出的timestamp是错误的。
 *
 *   如果需要反解出generator-timestamp，可以每次 `SeataUUIDGenerator.init(null).generateUUID()`（不使用单例）。
 *   这样的话，sequence固定是1，当timestamp冲突时，会生成重复的seata-uuid 。
 *   （还是需要改实现方式，或者每天1次 SeataUUIDGenerator.init(null)，但得不到想要的 timestamp）
 *
 * @see IdWorker#nextId()
 */
class SeataUUIDGeneratorTest {
	/**
	 * auto generate workerId, try using mac first, if failed, then randomly generate one.
	 *
	 * <b>mac: use lowest 10 bit of available MAC as workerId</b>
	 * 1900672179763912706
	 * @see IdWorker#generateWorkerId()
	 */
	@Test
	public void defaultWorkId() {
		SeataUUIDGenerator.init(null);
		for (int i = 0; i < 10; i++) {
			System.out.println("[UUID " + i + "] is: " + SeataUUIDGenerator.generateUUID());
		}
	}

	@Test
	public void overflow() {
		final int uuidGenerateCount = 5;
		final Long serverNodeId = 1023L;
		SeataUUIDGenerator.init(serverNodeId);
		for (int i = 0; i < uuidGenerateCount; i++) {
			System.out.println("[UUID " + i + "] is: " + SeataUUIDGenerator.generateUUID());
		}
	}
}