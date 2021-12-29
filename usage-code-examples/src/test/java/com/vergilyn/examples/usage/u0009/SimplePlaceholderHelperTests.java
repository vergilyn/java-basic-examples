package com.vergilyn.examples.usage.u0009;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

class SimplePlaceholderHelperTests {
	/** Prefix for system property placeholders: "${". */
	private static final String PLACEHOLDER_PREFIX = "${";

	/** Suffix for system property placeholders: "}". */
	private static final String PLACEHOLDER_SUFFIX = "}";

	/** Value separator for system property placeholders: ":". */
	private static final String VALUE_SEPARATOR = ":";

	private final SimplePlaceholderHelper strictHelper = new SimplePlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, false);
	private final SimplePlaceholderHelper nonStrictHelper = new SimplePlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, true);

	private final String _text = "默认值`name: ${name:vergilyn}`，\r\n待替换的变量`param: ${param}，未知的变量`unknown: ${unknown}`";

	private final Properties _properties = new Properties(){{
		put("param", "param-01");
	}};

	@Test
	public void test(){
		Pair<String, List<String>> pair = nonStrictHelper.parseStringValue(_text, _properties::getProperty);

		print(_text, pair.getKey());

		System.out.println("\nplaceholders >>>> " + Arrays.toString(pair.getValue().toArray(new String[0])));
	}

	private void print(String origin, String replace){
		System.out.println("origin  >>>> " + origin);
		System.out.println();
		System.out.println("replace >>>> " + replace);
	}
}