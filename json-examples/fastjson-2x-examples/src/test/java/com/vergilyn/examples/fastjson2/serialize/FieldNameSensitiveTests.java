package com.vergilyn.examples.fastjson2.serialize;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

public class FieldNameSensitiveTests {

	/**
	 * fastjson/fastjson2 会 忽略字段大小写！<br/>
	 *
	 * 可以参考反编译的代码：`com.alibaba.fastjson2.reader.ObjectReader_1.java`
	 */
	@SneakyThrows
	@Test
	public void test(){
		String json = "{\"userName\": \"username\"}";

		// 无论 true/false 都会忽略大小写。
		JSON.config(JSONReader.Feature.SupportSmartMatch, true);   

		JavaBean javaBean = JSON.parseObject(json, JavaBean.class);

		Assertions.assertThat(javaBean.username).isEqualTo("username");
		// 同时存在时，也会赋值到`username`，而无法赋值到 `userName`。（注意 成员变量 的定义顺序）
		Assertions.assertThat(javaBean.userName).isEqualTo("userName");

		// TimeUnit.HOURS.sleep(1);
	}


	@Getter
	@Setter
	@NoArgsConstructor
	public static class JavaBean implements Serializable {
		private String username;

		private String userName;

	}
}

