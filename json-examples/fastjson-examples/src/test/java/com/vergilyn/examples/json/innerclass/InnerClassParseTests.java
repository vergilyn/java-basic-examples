package com.vergilyn.examples.json.innerclass;

import java.util.List;

import com.alibaba.fastjson.JSON;

import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * 无法单独反序列化成 inner-class，所以实际业务中，最好定义成 static-inner-class。
 *
 * @author vergilyn
 * @since 2021-08-16
 */
public class InnerClassParseTests {
	public static final String INNER_JSON = "[{\"name\":\"01\"},{\"name\":\"02\"}]";

	/**
	 * fastjson 无法单独实例化<b>非静态内部类</b>（原因：非静态内部类需要 外部类才可以实例化）。<br/>
	 * 最简单的解决方式是：将非静态内部类 改成静态内部类or外部类
	 */
	@Test
	public void innerClass(){
		// com.alibaba.fastjson.JSONException: can't create non-static inner class instance.
		final Throwable throwable = Assertions.catchThrowable(() -> {
			List<UserInfo.InnerClass> innerClasses = JSON.parseArray(INNER_JSON, UserInfo.InnerClass.class);
			System.out.println("innerClass >>>> 01: " + innerClasses.get(0).getName());
			System.out.println("innerClass >>>> 02: " + innerClasses.get(1).getName());
			System.out.println("innerClass >>>> json: " + JSON.toJSONString(innerClasses));
		});

		Assertions.assertThat(throwable).isInstanceOf(com.alibaba.fastjson.JSONException.class);
		Assertions.assertThat(throwable).hasMessage("can't create non-static inner class instance.");

		throwable.printStackTrace();
	}

	/**
	 * 这样是可以的
	 */
	@Test
	public void fullInnerClass(){
		String json = "{\"username\":\"inner-class\",\"innerClasses\":[{\"name\":\"0001\"},{\"name\":\"0002\"}]}";

		UserInfo userInfo = JSON.parseObject(json, UserInfo.class);

		System.out.println("fullInnerClass >>>> " + JSON.toJSONString(userInfo));

		RandomUtils.nextInt(1, 10);
	}

	@Test
	public void fullStaticInnerClass(){
		String json = "{\"username\":\"static-inner-class\",\"staticInnerClasses\":[{\"name\":\"0001\"},{\"name\":\"0002\"}]}";

		UserInfo userInfo = JSON.parseObject(json, UserInfo.class);

		System.out.println("fullStaticInnerClass >>>> " + JSON.toJSONString(userInfo));
	}

	@Test
	public void staticInnerClass(){
		List<UserInfo.StaticInnerClass> staticInnerClasses = JSON.parseArray(INNER_JSON, UserInfo.StaticInnerClass.class);
		System.out.println("staticInnerClass >>>> 01: " + staticInnerClasses.get(0).getName());
		System.out.println("staticInnerClass >>>> 02: " + staticInnerClasses.get(1).getName());
		System.out.println("staticInnerClass >>>> json: " + JSON.toJSONString(staticInnerClasses));
	}



	@Data
	public static class UserInfo {

		private String username;

		private List<InnerClass> innerClasses;

		private List<StaticInnerClass> staticInnerClasses;

		@Data
		public class InnerClass {
			private String name;
		}

		@Data
		public static class StaticInnerClass {
			private String name;
		}
	}
}
