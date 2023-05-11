package com.vergilyn.examples.fastjson2.serialize;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import com.alibaba.fastjson2.reader.ObjectReaders;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DeserializerInstancePostProcessorTests {

	/**
	 * fastjson2 移除 ObjectDeserializer，用 {@link ObjectReader} 代替。
	 * 参考：<a href="https://alibaba.github.io/fastjson2/register_custom_reader_writer_cn">fastjson, register_custom_reader_writer_cn</a>
	 */
	@Test
	public void objectReader(){
		ObjectReader<C> aObjectReader = ObjectReaders.of(C.class);

		ObjectReader<C> delegate = new ObjectReader<C>(){
			@Override
			public C readObject(JSONReader jsonReader, long features) {
				C object = aObjectReader.readObject(jsonReader, features);

				if (object != null /* object instanceof InitializingBean */){
					try {
						object.afterPropertiesSet();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}

				return object;
			}
		};

		JSON.register(C.class, delegate);

		// b 没有正确调用`b.afterSet`
		String jsonStr = "{\"name\":\"AA\", \"b\": {\"name\": \"BB\"}, \"CList\":[{\"name\": \"C01\"}]}";

		A a = JSON.parseObject(jsonStr, A.class);

		// {"CList":[{"name":"C01_afterPropertiesSet"}],"b":{"name":"BB"},"name":"AA"}
		System.out.println(JSON.toJSONString(a));
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
		private List<C> cList;

		@Override
		public void afterPropertiesSet() throws Exception {
			this.name += "_afterPropertiesSet";
		}
	}

	@Data
	@NoArgsConstructor
	static class B implements InitializingBean{
		private String name = "B";
		private C c;

		@Override
		public void afterPropertiesSet() throws Exception {
			this.name += "_afterPropertiesSet";
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
