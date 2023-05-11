package com.vergilyn.examples.json.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

public class DeserializerInstancePostProcessorTests {

	/**
	 * 期望：在fastjson反序列化后，可以自动调用 {@link InitializingBean#afterPropertiesSet()} （类似 spring 的功能）。
	 *
	 * <pre> 备注：
	 *     1. 不管是哪层的 对象属性，只要有实现则调用 `afterPropertiesSet`.
	 * </pre>
	 *
	 * <p> 最终方案：
	 * <p> 1. Fastjson/Gson/Jackson 都未找到相关的实现方式。
	 * <p> 2. 只是觉得反序列化时，实现此功能“相对简单”。但实际感觉不应该这么实现，因为
	 *   <br/> 2.1) 无法复用 `afterPropertiesSet` 的逻辑
	 */
	@SneakyThrows
	@Test
	public void test(){
		A a = new A();
		System.out.println("A json: " + JSON.toJSONString(a));

		String jsonStr = "{\"name\":\"A\"}";

		// 只要返回 的`a1` 全部调用过 `afterPropertiesSet` 就可以。
		A a1 = JSON.parseObject(jsonStr, A.class);

		// 硬编码方式：在 `A#afterPropertiesSet` 内部调用 `B#afterPropertiesSet`，同理B内部也调用 `C#afterPropertiesSet`
		//   简单易理解...只是维护比较麻烦！
		// a1.afterPropertiesSet();

		System.out.println(JSON.toJSON(a1));

		TimeUnit.HOURS.sleep(1);
	}

	@Test
	public void objectDeserializer(){

		ObjectDeserializer javaBeanDeserializer = ParserConfig.getGlobalInstance().createJavaBeanDeserializer(A.class,
		                                                                                                      A.class);

		ObjectDeserializer delegateDeserializer = new ObjectDeserializer(){

			@Override
			public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
				Object rs = javaBeanDeserializer.deserialze(parser, type, fieldName);

				if (rs instanceof InitializingBean){
					try {
						((InitializingBean) rs).afterPropertiesSet();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				return (T) rs;
			}

			@Override
			public int getFastMatchToken() {
				return javaBeanDeserializer.getFastMatchToken();
			}
		};

		ParserConfig.getGlobalInstance().putDeserializer(A.class, delegateDeserializer);

		// b 没有正确调用`b.afterSet`
		String jsonStr = "{\"name\":\"AA\", \"b\": {\"name\": \"BB\"}}";

		A a = JSON.parseObject(jsonStr, A.class);

		// {"b":{"c":{"name":"C"},"name":"BB"},"name":"AA_afterPropertiesSet"}
		System.out.println(JSON.toJSONString(a));
	}

	/**
	 * <a href="https://github.com/alibaba/fastjson/wiki/ObjectDeserializer_cn">ObjectDeserializer_cn</a>
	 */
	@Test
	public void javaBeanDeserializer(){

		ParserConfig parserConfig = ParserConfig.getGlobalInstance();

		Class<A> clazz = A.class;

		// 参考：com.alibaba.fastjson.parser.ParserConfig.initJavaBeanDeserializers Line: 993
		JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz
				, clazz
				, parserConfig.propertyNamingStrategy
				, false
				, TypeUtils.compatibleWithJavaBean
				, parserConfig.isJacksonCompatible()
		);

		parserConfig.putDeserializer(A.class, new JavaBeanDeserializer(parserConfig, beanInfo){

			@Override
			public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, int features) {

				return super.deserialze(parser, type, fieldName, features);
			}
		});


		String jsonStr = "{\"name\":\"A\"}";
		A a1 = JSON.parseObject(jsonStr, clazz);
		System.out.println(JSON.toJSON(a1));
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean
	 */
	static interface InitializingBean {

		void afterPropertiesSet() throws Exception;

	}

	@Data
	@NoArgsConstructor
	static class A implements InitializingBean{
		private String name = "A";

		private B b;

		@Override
		public void afterPropertiesSet() throws Exception {
			this.name += "_afterPropertiesSet";
			if (this.b == null){
				this.b = new B();
			}
		}
	}

	@Data
	@NoArgsConstructor
	static class B implements InitializingBean{
		private String name = "B";

		private C c = new C();

		@Override
		public void afterPropertiesSet() throws Exception {
			this.name += "_afterPropertiesSet";

			if (this.c == null){
				this.c = new C();
			}
		}
	}

	@Data
	@NoArgsConstructor
	static class C implements InitializingBean{
		private String name = "C";

		@Override
		public void afterPropertiesSet() throws Exception {
			this.name += "_afterPropertiesSet";
		}
	}
}
