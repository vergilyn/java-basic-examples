package com.vergilyn.examples.usage.u0005;

import org.junit.jupiter.api.Test;


/**
 * get next UUID(base on snowflake algorithm), which look like:
 * <pre>
 *   highest 1 bit: always 0
 *   next   10 bit: workerId  ()
 *   next   41 bit: timestamp
 *   lowest 12 bit: sequence
 * </pre>
 *
 * @see IdWorker#nextId()
 */
class SeataUUIDGeneratorTest {

	/**
	 * auto generate workerId, try using mac first, if failed, then randomly generate one.
	 *
	 * <b>mac: use lowest 10 bit of available MAC as workerId</b>
	 *
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