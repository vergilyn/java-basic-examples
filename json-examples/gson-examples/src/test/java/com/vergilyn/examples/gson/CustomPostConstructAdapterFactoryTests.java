package com.vergilyn.examples.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.typeadapters.PostConstructAdapterFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class CustomPostConstructAdapterFactoryTests {

	/**
	 * 根据 {@link PostConstructAdapterFactory} 思路实现
	 */
	@Test
	public void test(){
		String jsonStr = "{\"name\": \"gson-A\", \"b\": {\"name\": \"gson-B\"}}";

		Gson gson = new GsonBuilder()
				.registerTypeAdapterFactory(new CustomPostConstructAdapterFactory())
				.create();

		A a = gson.fromJson(jsonStr, A.class);

		// 无法达到此期望：
		//   因为`C` 并不是由 反序列化生成的对象，所以不会触发调用`C.afterSet`
		String expected = "{\"name\":\"gson-A_afterPropertiesSet\",\"b\":{\"name\":\"gson-B_afterPropertiesSet\",\"c\":{\"name\":\"C_afterPropertiesSet\"}}}";

		// {"name":"gson-A_afterPropertiesSet","b":{"name":"gson-B_afterPropertiesSet","c":{"name":"C"}}}
		String actual = gson.toJson(a);
		System.out.println(actual);

		Assertions.assertEquals(expected, actual);
	}

	@Test
	public void testList(){
		Gson gson = new GsonBuilder()
				.registerTypeAdapterFactory(new CustomPostConstructAdapterFactory())
				.create();

		String jsonStr = "{\"name\": \"gson-A\", \"b\": {\"name\": \"gson-B\"}, \"cList\":[{\"name\": \"c-01\"}]}";
		A a = gson.fromJson(jsonStr, A.class);

		String actual = gson.toJson(a);

		// {"name":"gson-A_afterPropertiesSet","b":{"name":"gson-B_afterPropertiesSet","c":{"name":"C"}},"cList":[{"name":"c-01_afterPropertiesSet"}]}
		System.out.println(actual);
	}

	static class CustomPostConstructAdapterFactory implements TypeAdapterFactory{

		@Override
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
			if (InitializingBean.class.isAssignableFrom(typeToken.getRawType())){
				TypeAdapter<T> delegate =  gson.getDelegateAdapter(this, typeToken);
				return new InitializingBeanTypeAdapter<>(delegate);
			}

			return null;
		}
	}

	static class InitializingBeanTypeAdapter<T> extends TypeAdapter<T> {
		private final TypeAdapter<T> delegate;

		public InitializingBeanTypeAdapter(TypeAdapter<T> delegate) {
			this.delegate = delegate;
		}

		@Override
		public void write(JsonWriter jsonWriter, T t) throws IOException {
			this.delegate.write(jsonWriter, t);
		}

		@Override
		public T read(JsonReader jsonReader) throws IOException {
			T result = this.delegate.read(jsonReader);
			if (result instanceof InitializingBean){
				try {
					((InitializingBean) result).afterPropertiesSet();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return result;
		}
	}

	static interface InitializingBean {
		void afterPropertiesSet() throws Exception;
	}

	@Data
	@NoArgsConstructor
	class A implements InitializingBean{
		private String name = "A";

		private B b;

		private List<C> cList;

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
	class B implements InitializingBean{
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
	class C implements InitializingBean{
		private String name = "C";

		@Override
		public void afterPropertiesSet() throws Exception {
			this.name += "_afterPropertiesSet";
		}
	}
}