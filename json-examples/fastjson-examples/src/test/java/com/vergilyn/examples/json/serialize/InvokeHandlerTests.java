package com.vergilyn.examples.json.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.PropertyProcessable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

public class InvokeHandlerTests {

	/**
	 * 期望：在fastjson反序列化后，可以自动调用 {@link InitializingBean#afterPropertiesSet()} （类似 spring 的功能）。
	 *
	 * <pre> 备注：
	 *     1. 不管是哪层的 对象属性，只要有实现则调用 `afterPropertiesSet`.
	 *     2. 是在 对象属性设置完成后 再调用！
	 * </pre>
	 */
	@SneakyThrows
	@Test
	public void test(){
		//language=JSON
		String json = "{\"name\": \"UserInfoWrapper\", \"userInfo\": {\"name\": \"vergilyn\"}}";

		CustomProcessor processor = new CustomProcessor();

		UserInfoWrapper userInfoWrapper= JSON.parseObject(json, UserInfoWrapper.class, processor);
		// userInfo.afterPropertiesSet();

		System.out.println(userInfoWrapper);

		String expected = userInfoWrapper.userInfo.name;
		Assertions.assertEquals(expected, userInfoWrapper.userInfo.newName);
	}

	public static class CustomProcessor implements PropertyProcessable {

		@Override
		public Type getType(String name) {
			return null;
		}

		@Override
		public void apply(String name, Object value) {
			System.out.printf("name: %s, value: %s \n", name, value);
		}
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean
	 */
	public static interface InitializingBean {

		void afterPropertiesSet() throws Exception;

	}

	@Data
	@NoArgsConstructor
	public static class UserInfoWrapper implements InitializingBean{
		private String name;

		private UserInfo userInfo;

		@Override
		public void afterPropertiesSet() throws Exception {
			this.name += "_afterPropertiesSet";
		}
	}

	@Data
	@NoArgsConstructor
	public static class UserInfo implements InitializingBean {
		private String name;

		private String newName;

		@Override
		public void afterPropertiesSet() throws Exception {
			this.newName = this.newName == null ? this.name : this.newName;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}
}
