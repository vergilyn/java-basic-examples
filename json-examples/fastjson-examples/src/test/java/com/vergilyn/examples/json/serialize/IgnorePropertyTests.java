package com.vergilyn.examples.json.serialize;

import java.beans.Transient;
import java.time.LocalTime;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import lombok.Data;
import org.junit.jupiter.api.Test;

/**
 *
 * @author dingmaohai
 * @version v1.0
 * @since 2021/12/06 17:17
 */
public class IgnorePropertyTests {
	private final UserInfo bean = new UserInfo();

	/**
	 * <pre>
	 *  1. {@linkplain JSONField#serialize()}，通过 fastjson 的注解。
	 *  2. {@linkplain SimplePropertyPreFilter}，通过 fastjson 的 filter。（需要注意 嵌套对象）
	 *  3. {@linkplain java.beans.Transient}，该 jdk注解只能作用域`METHOD`
	 * </pre>
	 *
	 * 一般个人更多用`filter`，虽然代码量增加。但是如果地方该property需要序列化时，方便处理。
	 */
	@Test
	public void byFilter(){
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		filter.getExcludes().add("ignorePropertyByFilter");

		System.out.println(JSON.toJSONString(bean, filter));
	}


	@Data
	public static class UserInfo {
		private final String string = LocalTime.now().toString();

		private String id = string;

		private String ignorePropertyByFilter = string;

		@JSONField(serialize = false)
		private String ignorePropertyByJSONField = string;

		private String ignorePropertyByTransient = string;

		@Transient // 该注解只能用于`method`
		public String getIgnorePropertyByTransient() {
			return ignorePropertyByTransient;
		}
	}
}
