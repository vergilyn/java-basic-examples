package com.vergilyn.examples.json.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.*;
import com.vergilyn.examples.json.AbstractFastjsonTests;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * 序列化成json时，强制保留boolean类型的`is`前缀。
 * <br/> 1. 不期望命名成`getIsXxx()`
 * <br/> 2. 不期望通过{@link JSONField#name()} 指定
 *
 * @author vergilyn
 * @since 2022-06-17
 */
public class KeepBooleanIsPrefixTests extends AbstractFastjsonTests {

	/**
	 * @see JSON#toJSONString(Object, SerializeConfig, SerializeFilter, SerializerFeature...)
	 */
	@Test
	public void test(){
		JsonBean jsonBean = new JsonBean();
		System.out.println(jsonBean);

		NameFilter nameFilter = new NameFilter() {
			/**
			 * {@link com.alibaba.fastjson.serializer.JavaBeanSerializer#write(JSONSerializer, Object, Object, Type, int, boolean)}
			 * , LINE: 305
			 *
			 * 2022-06-20, fastjson-v2.0.8 已支持获取 FiledInfo,
			 * @see <a href="https://github.com/alibaba/fastjson2/issues/484#issuecomment-1159219908">484#issuecomment-1159219908</a>
			 */
			@Override
			public String process(Object object, String name, Object value) {
				if (value == null){
					return name;
				}

				// 1. 如果`value == null` 不太方便判断类型！
				// 2. 没有传入 Field/FieldInfo，不易于扩展。（变相的此处又需要通过反射获取。）
				if (!Boolean.class.isAssignableFrom(value.getClass())){
					return name;
				}

				if (name.startsWith("is")){
					return name;
				}

				String first = name.substring(0, 1);

				return "is" + first.toUpperCase() + name.substring(1);
			}
		};

		// 期望：boolean 类型强制`isXxx`。
		System.out.println("NameFilter >>>> " + JSON.toJSONString(jsonBean, nameFilter));
	}


	@Data
	public static class JsonBean implements Serializable {

		private Boolean propertyBoolean;

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
			return JSON.toJSONString(this, true);
		}
	}
}
