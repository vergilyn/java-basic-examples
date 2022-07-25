package com.vergilyn.examples.format;

import com.google.common.collect.Maps;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class StringSubstitutorTests {

	/**
	 * @see StringLookupFactory#interpolatorStringLookup()
	 */
	@Test
	public void test(){
		// `sys` 和 `script` 模式都是关键字
		String pattern = "OS name: ${sys:os.name}, " + "3 + 4 = ${script:javascript:3 + 4}";

		StringSubstitutor interpolator = StringSubstitutor.createInterpolator();

		String result = interpolator.replace(pattern);

		// StringSubstitutor >>>> OS name: Windows 10, 3 + 4 = 7
		System.out.println("StringSubstitutor >>>> " + result);
	}

	@RepeatedTest(10)
	public void test2(){
		//
		//    正确的写法是 `${name:-vergilyn}`
		/**
		 * 1. `${name:vergilyn}`并不是指`name`的默认值是`vergilyn`
		 *   正确的写法是 `${name:-vergilyn}`  参考javadoc
		 *     - {@linkplain StringSubstitutor}
		 *     - {@linkplain StringSubstitutor#valueDelimiterMatcher}
		 *     - {@linkplain StringSubstitutor#DEFAULT_VAR_DEFAULT} 默认值分隔符是 `:-`
		 *     - {@linkplain StringSubstitutor#DEFAULT_VALUE_DELIMITER}
		 */
		String pattern = "param: ${name:-vergilyn}, OS name: ${sys:os.name}, 3 + 4 = ${script:javascript:3 + 4}";

		Map<String, Object> params = Maps.newHashMap();
		params.put("name", "vergilyn");

		StringSubstitutor interpolator = StringSubstitutor.createInterpolator();

		// 2. 如果传递`valueMap`，并没有解析`${sys}` 和 `${script}`
		String result = interpolator.replace(pattern, params);

		//  param: vergilyn, OS name: ${sys:os.name}, 3 + 4 = ${script:javascript:3 + 4}
		System.out.println("StringSubstitutor >>>> " + result);
	}
}
