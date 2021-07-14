package com.vergilyn.examples.format;

import java.text.MessageFormat;
import java.util.Map;

import com.google.common.collect.Maps;

import org.apache.commons.text.StringSubstitutor;
import org.testng.annotations.Test;

/**
 *
 * @author vergilyn
 * @since 2021-07-08
 *
 * @see String#format(String, Object...)
 * @see java.text.MessageFormat#format(String, Object...)
 * @see org.apache.commons.text.StringSubstitutor#replace(Object, Map)
 */
public class StringReplaceTestng {

	/**
	 * 不灵活，且比较重复。可读性也不高
	 */
	@Test
	public void stringFormat(){
		String pattern = "name: %s, number: %d, desc: %s-%d";

		String result = String.format(pattern, "vergilyn", 24, "vergilyn", 24);

		System.out.println("String.format >>>> " + result);
	}

	/**
	 * 可读性相对较好，但是 因为占位符是`{x}`，无法格式化JSON 例如`{"name": {0}}`会报错。
	 */
	@Test
	public void messageFormat(){
		String pattern = "name: {0}, number: {1}, desc: {0}-{1}-{2}";

		String result = MessageFormat.format(pattern, "vergilyn", 24);

		System.out.println("MessageFormat >>>> " + result);
	}

	/**
	 * apache-common-text
	 */
	@Test
	public void stringSubstitutor(){
		String pattern = "{name: ${name}, number: ${number}, desc: ${name}-${number}-${unknown}}";

		Map<String, Object> params = Maps.newHashMap();
		params.put("name", "vergilyn");
		params.put("number", 24);

		String result = StringSubstitutor.replace(pattern, params);

		System.out.println("StringSubstitutor >>>> " + result);
	}
}
