package com.vergilyn.examples.json.date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-09-10
 */
@SuppressWarnings("JavadocReference")
public class DateDeserializeTests {

	/*
	// spring-boot 启动类通过 `@Import({FastJsonHttpMessageConverterConfiguration.class})`导入。
	// controller可以自动转化 `yyyy-MM-dd HH:mm:ss` 或者 `yyyy/MM/dd HH:mm:ss` 为 `java.util.Date`
	public class FastJsonHttpMessageConverterConfiguration {
		public FastJsonHttpMessageConverterConfiguration() {
		}

		@Bean
		public org.springframework.http.converter.HttpMessageConverter fastJsonHttpMessageConverter() {
			FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
			converter.setSupportedMediaTypes(
					Arrays.asList(new MediaType("application", "json"), new MediaType("application", "*+json")));
			converter.getFastJsonConfig().setSerializeConfig(new SerializeConfig());
			converter.getFastJsonConfig().setSerializerFeatures(new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNonStringKeyAsString});
			converter.getFastJsonConfig().getSerializeConfig().put(Long.class, ToStringSerializer.instance);
			converter.getFastJsonConfig().getSerializeConfig().put(Long.TYPE, ToStringSerializer.instance);
			return converter;
		}
	}
	*/

	/**
	 * fastjson 默认可以识别 `yyyy-MM-dd HH:mm:ss` 或者 `yyyy/MM/dd HH:mm:ss`。
	 * jackson 默认不可以。
	 *
	 * <p>
	 *   {@linkplain JSONScanner#scanISO8601DateIfMatch(boolean, int)}中会解析：
	 *   `yyyy/mm/dd` 或 `yyyy-m-dd` 或 `dd.mm.yyyy` 等等等格式的时间（fastjson-1.2.62-source, line: 386）
	 * </p>
	 *
	 *
	 * @see JSONObject#getDate(String)
	 * @see com.alibaba.fastjson.util.TypeUtils#castToDate(Object, String)
	 * @see com.alibaba.fastjson.parser.JSONScanner#JSONScanner(String)
	 * @see com.alibaba.fastjson.parser.JSONScanner#scanISO8601DateIfMatch(boolean, int)
	 */
	@Test
	public void dateFormat(){
		println("{\"date\": \"2021-09-07 10:55:05\"}");
		println("{\"date\": \"2021/09/07 10:55:05\"}");
		println("{\"date\": \"07.09.2021 10:55:05\"}");
		println("{\"date\": \"2021年09月07日 10:55:05\"}");
		// dateStr = "{\"date\": \"2021$09$07 10:55:05\"}";  // error
	}

	private static void println(String dateStr){
		final JSONObject jsonObject = JSON.parseObject(dateStr);
		System.out.println(jsonObject.getDate("date"));
	}
}
