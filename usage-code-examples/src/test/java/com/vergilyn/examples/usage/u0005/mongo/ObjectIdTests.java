package com.vergilyn.examples.usage.u0005.mongo;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-07-14
 *
 * @see <a href="https://docs.mongodb.com/manual/reference/method/ObjectId/">MongoDB ObjectId</a>
 */
class ObjectIdTests {
	private static final String OBJECT_ID = "60793d9bd4056cf3ad9d4fa0";

	/**
	 * ObjectId("60793d9bd4056cf3ad9d4fa0").getTimestamp();
	 */
	private static final String OBJECT_ID_TIMESTAMP = "2021-04-16 15:32:43.000";

	/**
	 * Returns a new ObjectId value. The 12-byte ObjectId value consists of: <br/>
	 *   a 4-byte timestamp value, representing the ObjectId's creation, measured in seconds since the Unix epoch <br/>
	 *   a 5-byte random value <br/>
	 *   a 3-byte incrementing counter, initialized to a random value <br/>
	 *
	 * SEE：mongodb-java-drivers <br/>
	 * ```
	 *   BasicDBObject queryObject = new BasicDBObject(“_id”,new ObjectId(id));
	 * ```
	 */
	@Test
	public void objectId(){
		ObjectId objectId = new ObjectId(OBJECT_ID);
		System.out.println(DateFormatUtils.format(objectId.getDate(), "yyyy-MM-dd HH:mm:ss.SSS"));
	}

	@Test
	public void parser(){
		// 0x60793d9b = 24697
		String timestamp = OBJECT_ID.substring(0, 4 * 2);

		Long mills = Long.parseLong(timestamp, 16) * 1000L;

		System.out.println(DateFormatUtils.format(new Date(mills), "yyyy-MM-dd HH:mm:ss.SSS"));
	}
}
