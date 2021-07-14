package com.vergilyn.examples.format;

import java.text.MessageFormat;

import org.testng.annotations.Test;

/**
 * @see MessageFormat
 */
public class MessageFormatTestng {

	@Test
	public void test(){
		String pattern = "'name': {0}, 'email': {1}";

		String result = MessageFormat.format(pattern, "vergilyn", "vergilyn@vip.qq.com");
		System.out.println(result);
	}

	/**
	 * 字符串不能被`{}`包围
	 */
	@Test(expectedExceptions = IllegalArgumentException.class
			, expectedExceptionsMessageRegExp = "can't parse argument number: 'name': \\{0}")
	public void exception(){
		String pattern = "{'name': {0}, 'email': {1}}";
		String result = MessageFormat.format(pattern, "vergilyn", "vergilyn@vip.qq.com");
		System.out.println(result);
	}

	@Test
	public void buildAHtml(){
		String pattern = "<a href=\"{0}\""
				+ " data-web=\"{0}\""
				+ " data-wap=\"{1}\""
				+ " onclick=\"toHref(this)\""
				+ " >{2}</a>";

		String i0 = "http://www.baidu.com";
		String i1 = "http://wap.baidu.com";
		String i2 = "关键字";

		String result = MessageFormat.format(pattern, i0, i1, null);
		System.out.println(result);
	}
}
