package com.vergilyn.examples.json.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

/**
 * <a href="https://github.com/alibaba/fastjson/wiki/FieldTypeResolver">Github wiki, FieldTypeResolver</a>
 * <p> FieldTypeResolver是1.2.9 & 1.1.49.android中引入的新功能，用于解析嵌套对象时，自动识别子对象的类型信息。
 * <p> FieldTypeResolver只会作用于value类型为json object的字段。自动识别类型，能避免二次解析，使用起来更简单，性能也会更好。
 *
 * @author vergilyn
 * @since 2022-10-08
 */
public class FieldTypeResolverProcessorTests {

	@Test
	public void test() {
		FieldTypeResolver fieldResolver = new FieldTypeResolver() {
			@Override
			public Type resolve(Object object, String fieldName) {
				// 字段名称为item_开始的对象，识别类型为Item
				if (fieldName.startsWith("item_")) {
					return Item.class;
				}

				return null;
			}
		};

		String text = "{\"item_0\":{},\"item_1\":{},\"item_2\":1001}";
		JSONObject o = JSON.parseObject(text, JSONObject.class, fieldResolver);
		Assertions.assertTrue(o.get("item_0") instanceof Item);
		Assertions.assertTrue(o.get("item_1") instanceof Item);

		// 还是Integer，因为value不是Object。
		Assertions.assertTrue(o.get("item_2") instanceof Integer);
	}

	public static class Item {
		public int value;
	}

}
