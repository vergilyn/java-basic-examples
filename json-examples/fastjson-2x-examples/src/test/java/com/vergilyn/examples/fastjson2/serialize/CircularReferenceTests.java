package com.vergilyn.examples.fastjson2.serialize;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CircularReferenceTests {

	private Person person;
	private String expectedMapJson;

	@BeforeAll
	public void beforeAll(){
		LocalTime now = LocalTime.now();

		Map<String, String> map = new HashMap<String, String>(){{
			put("time", now.toString());
		}};

		person = new Person(map, map);

		expectedMapJson = JSON.toJSONString(map);
	}

	/**
	 * fastjson: 1.2.79 OR 1.2.80 （2022-05-09，最新 1.x 版本）<br/>
	 *
	 * 默认fastjson 启用了 `CircularReferenceDetect`，所以 序列化成json字符串时，会出现`$ref`。
	 * <b>但是</b>，fastjson 反序列化带`$ref`关键字到 java-object 会出现 null，暂时未找到（反序列化时的）解决方法。
	 *
	 * <p> 其实为了保证 满足JSON规范，可以序列化时全部都`DisableCircularReferenceDetect`。
	 */
	@Test
	public void test(){
		// {"map":{"time":"13:55:08.806"},"mapSnapshot":{"$ref":"$.map"}}
		String defaultJson = JSON.toJSONString(person);
		System.out.println("default: " + defaultJson);

		// `$ref` 没有正确反序列化！
		Person defaultDes = JSON.parseObject(defaultJson, Person.class);
		System.out.println("default-deserial: " + JSON.toJSONString(defaultDes));
		System.out.println("default-deserial.MapSnapshot.time: " + defaultDes.getMapSnapshot().get("time"));
		System.out.println("default-deserial.MapSnapshot.$ref: " + defaultDes.getMapSnapshot().get("$ref"));

		System.out.println();

		// 1. 可以在序列化时 禁用循环引用。（代价：占用大小增加。  ）
		// ReferenceDetection	打开引用检测，这个缺省是关闭的，和fastjson 1.x不一致
		String disableCirRef = JSON.toJSONString(person);
		System.out.println("DisableCircularReferenceDetect: " + disableCirRef);

		Person disableCirRefPerson = JSON.parseObject(disableCirRef, Person.class);
		System.out.println("DisableCircularReferenceDetect-des: " + JSON.toJSONString(disableCirRefPerson.mapSnapshot));
	}

	@Test
	public void testGlobalSerial(){
		// 全局设置`DisableCircularReferenceDetect`
		// JSON.DEFAULT_GENERATE_FEATURE |= DisableCircularReferenceDetect.getMask();

		test();
	}

	@Data
	@AllArgsConstructor
	public static class Person {
		private Map<String, String> map;

		private Map<String, String> mapSnapshot;
	}
}
