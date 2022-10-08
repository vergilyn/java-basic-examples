package com.vergilyn.examples.json.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.deserializer.PropertyProcessable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

/**
 * <a href="https://github.com/alibaba/fastjson/wiki/PropertyProcessable_cn">Github wiki, PropertyProcessable_cn</a>
 * PropertyProcessable是1.2.35版本开始支持的自定义反序列化接口。
 *
 * <p> {@link PropertyProcessable#getType(String)}: 返回property的类型，如果返回空，则由parser自行推断。
 * <p> {@link PropertyProcessable#apply(String, Object)}: 处理属性值
 *
 * @author vergilyn
 * @since 2022-10-08
 *
 * @see com.alibaba.fastjson.parser.deserializer.PropertyProcessable
 */
public class PropertyProcessableTests {

	@Test
	public void test(){
		VO vo = JSON.parseObject("{\"vo_id\":123,\"vo_name\":\"abc\",\"value\":{}}", VO.class);

		Assertions.assertEquals(123, vo.id);
		Assertions.assertEquals("abc", vo.name);
		Assertions.assertNotNull(vo.value);
	}

	public static class VO implements PropertyProcessable {
		public int id;
		public String name;
		public Value value;

		// 返回property的类型，如果返回 null，则由parser自行推断。
		@Override
		public Type getType(String name) {
			if ("value".equals(name)) {
				return Value.class;
			}
			return null;
		}

		@Override
		public void apply(String name, Object value) {
			System.out.printf("\t PropertyProcessable#apply(...) >>>> name: %s, value.class: %s, value: %s \n", name, value.getClass(), value);

			if ("vo_id".equals(name)) {
				this.id = ((Integer) value).intValue();
			} else if ("vo_name".equals(name)) {
				this.name = (String) value;
			} else if ("value".equals(name)) {
				this.value = (Value) value;
			}
		}
	}

	public static class Value {

	}
}
