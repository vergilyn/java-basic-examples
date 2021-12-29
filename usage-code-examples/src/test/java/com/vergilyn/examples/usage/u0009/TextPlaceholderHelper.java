package com.vergilyn.examples.usage.u0009;

import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * 1. 需要可以扩展: prefix，suffix，default_value_separator <br/>
 * 2. 需要能提取出 origin-placeholder-names <br/>
 * 3. 不用支持 nested-placeholder <br/>
 * 4. 不用支持 表达式/方法 <br/>
 * 5. 支持 value 处理后再进行替换，例如替换前先`String.trim()`
 *
 * @author vergilyn
 * @since 2021-12-28
 *
 * @see org.springframework.util.PropertyPlaceholderHelper
 * @see org.apache.commons.text.StringSubstitutor
 *
 */
public class TextPlaceholderHelper {
	/** Prefix for system property placeholders: "${". */
	private static final String PLACEHOLDER_PREFIX = "${";

	/** Suffix for system property placeholders: "}". */
	private static final String PLACEHOLDER_SUFFIX = "}";

	/** Value separator for system property placeholders: ":". */
	private static final String VALUE_SEPARATOR = ":";

	private final String _text = "默认值`name: ${name:vergilyn}`，待替换的变量`param: ${param}，未知的变量`unknown: ${unknown}`";

	private final Properties _properties = new Properties(){{
		put("param", "param-01");
	}};

	/**
	 * spring-core, {@linkplain PropertyPlaceholderHelper}: <br/>
	 * 1. 可以自定义 prefix/suffix/default_value_separator  <br/>
	 * 2. 无法扩展提取出 origin-placeholder-names  <br/>
	 *
	 * 可以复制`SimplePlaceholderHelper`代码，重新调整。
	 *
	 * @see org.springframework.util.SystemPropertyUtils
	 */
	@Test
	public void propertyPlaceholderHelper(){
		PropertyPlaceholderHelper placeholderHelper = new PropertyPlaceholderHelper(PLACEHOLDER_PREFIX,
		                                                                            PLACEHOLDER_SUFFIX,
		                                                                            VALUE_SEPARATOR,
		                                                                            true);

		String result = placeholderHelper.replacePlaceholders(_text, _properties);

		print(_text, result);
	}

	/**
	 * `SimplePlaceholderHelper` 只是简单的替换，但是`stringSubstitutor`支持 表达式/script 之类的。
	 */
	@Test
	public void stringSubstitutor(){

	}

	private void print(String origin, String replace){
		System.out.println("origin  >>>> " + origin);
		System.out.println("replace >>>> " + replace);
	}
}
