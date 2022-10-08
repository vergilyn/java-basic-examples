package com.vergilyn.examples.json.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * <a href="https://github.com/alibaba/fastjson/wiki/ParseProcess">Github wiki, ParseProcess</a>
 * <p> {@link ParseProcess} 是编程扩展定制反序列化的接口。fastjson支持如下ParseProcess：
 * <br/> {@link ExtraProcessor}: 用于处理 <b>多余的字段</b>
 * <br/> {@link ExtraTypeProvider}: 用于处理 <b>多余的字段</b> 时提供类型信息
 *
 * @author vergilyn
 * @since 2022-10-08
 */
public class ExtraProcessorTests {

	@Test
	public void test() {
		VO vo = JSON.parseObject("{\"id\":123,\"name\":\"abc\",\"value\":\"123456\"}", VO.class, new MyExtraProcessor());
		Assertions.assertEquals(123, vo.getId());
		Assertions.assertEquals("abc", vo.getAttributes().get("name"));

		// value本应该是字符串类型的，通过getExtraType的处理变成Integer类型了。
		Assertions.assertEquals(123456, vo.getAttributes().get("value"));
	}

	public static class MyExtraProcessor implements ExtraProcessor, ExtraTypeProvider {
		@Override
		public void processExtra(Object object, String key, Object value) {
			VO vo = (VO) object;
			vo.getAttributes().put(key, value);
		}

		@Override
		public Type getExtraType(Object object, String key) {
			if ("value".equals(key)) {
				return int.class;
			}
			return null;
		}
	}

	public static class VO {
		@Setter
		@Getter
		private int id;

		private Map<String, Object> attributes = new HashMap<String, Object>();

		public Map<String, Object> getAttributes() {
			return attributes;
		}
	}
}
