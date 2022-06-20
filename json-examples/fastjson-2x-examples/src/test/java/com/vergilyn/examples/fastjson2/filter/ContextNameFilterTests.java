package com.vergilyn.examples.fastjson2.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.annotation.JSONField;
import com.alibaba.fastjson2.filter.BeanContext;
import com.alibaba.fastjson2.filter.ContextNameFilter;
import com.vergilyn.examples.fastjson2.AbstractFasjosn2Tests;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static com.alibaba.fastjson2.JSONWriter.Feature.WriteNullBooleanAsFalse;

public class ContextNameFilterTests extends AbstractFasjosn2Tests {
	private static final JSONWriter.Feature[] features = {WriteNullBooleanAsFalse};

	@Test
	public void contextNameFilter(){
		JsonBean jsonBean = new JsonBean();
		System.out.println("default >>>> " + jsonBean);

		// com.alibaba.fastjson2.writer.ObjectWriterAdapter#writeWithFilter(...), LINE: 395
		// XXX 2022-06-20，
		//  1. 不存在field时，无法识别`getIsSuccessA()`。（fastjson-1x 可以识别）
		//  2. 参考源码，如果 `property-value = null`, 并不会调用 `ContextNameFilter#process`。
		ContextNameFilter contextNameFilter = new ContextNameFilter() {
			@Override
			public String process(BeanContext context, Object object, String name, Object value) {
				System.out.println("ContextNameFilter#process >>>> name: " + name);

				// `filed / method` 可能是 NULL （必有其一 non-null）
				Field field = context.getField();
				Method method = context.getMethod();

				Class<?> fieldClass = context.getFieldClass();
				Type fieldType = context.getFieldType();

				boolean isBooleanType = org.springframework.util.ClassUtils.isAssignable(Boolean.class, fieldClass);
				if (!isBooleanType){
					return name;
				}

				// JSONField 的优先级更高。
				JSONField jsonField = context.getAnnotation(JSONField.class);
				if (jsonField != null && jsonField.name() != null){
					return name;
				}

				String booleanPrefix = "has";
				if (name.startsWith(booleanPrefix)){
					return name;
				}

				String first = name.substring(0, 1);

				return booleanPrefix + first.toUpperCase() + name.substring(1);
			}
		};

		// 期望：boolean 类型强制`isXxx`。
		String string = JSON.toJSONString(jsonBean, contextNameFilter, features);

		System.out.println("ContextNameFilter >>>> " + string);
	}

	@Data
	public static class JsonBean implements Serializable {

		private Boolean propertyBoolean = null;

		// fastjson-2 不存在对应的field时，貌似无法识别`getIsXxx()`，所以不会序列化。
		public boolean getIsSuccessA(){
			return true;
		}

		@JSONField(name = "isSuccessB")
		public boolean isSuccessB(){
			return true;
		}

		public boolean isSuccess(){
			return true;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this, features);
		}
	}
}
