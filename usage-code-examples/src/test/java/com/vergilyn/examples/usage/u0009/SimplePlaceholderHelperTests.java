package com.vergilyn.examples.usage.u0009;

import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class SimplePlaceholderHelperTests {

	/** `properties`中不存在的 placeholder 抛出异常 */
	private final SimplePlaceholderHelper strictHelper = SimplePlaceholderHelper.buildDefaultStrictHelper();

	/** `properties`中不存在的 placeholder替换成指定的`unresolvableValue` */
	private final SimplePlaceholderHelper nonStrictHelper = SimplePlaceholderHelper.buildDefaultNonStrictHelper("");

	/** 保留`properties`中不存在的 placeholder */
	private final SimplePlaceholderHelper nonStrictHelperKeepPlaceholder = SimplePlaceholderHelper.buildDefaultNonStrictHelper(null);

	private final String _text = "默认值`name: ${name:vergilyn}`，\r\n待替换的变量`param: ${param}，未知的变量`unknown: ${unknown}`";

	private final Properties _properties = new Properties(){{
		put("param", "param-01");
	}};

	@Test
	public void nonStrictHelper(){
		Pair<String, Map<String, String>> pair = nonStrictHelper.parseStringValue(_text, _properties::getProperty);

		print(_text, pair.getKey());

		System.out.println("\nplaceholders >>>> " + JSON.toJSONString(pair.getValue()));
	}

	@Test
	public void nonStrictHelperKeepPlaceholder(){
		Pair<String, Map<String, String>> pair = nonStrictHelperKeepPlaceholder.parseStringValue(_text, _properties::getProperty);

		print(_text, pair.getKey());

		System.out.println("\nplaceholders >>>> " + JSON.toJSONString(pair.getValue()));
	}

	private void print(String origin, String replace){
		System.out.println("origin  >>>> " + origin);
		System.out.println();
		System.out.println("replace >>>> " + replace);
	}
}