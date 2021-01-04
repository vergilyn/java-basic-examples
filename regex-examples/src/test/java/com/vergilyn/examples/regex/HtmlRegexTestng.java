package com.vergilyn.examples.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 经测试HTML语法中，<a
 */
public class HtmlRegexTestng {

	/**
	 * 成对匹配，捕获组匹配。例如：`\1`, `\2`
	 *
	 * @see <a href="https://github.com/tuia-fed/weekly-question/issues/25">匹配成对标签</a>
	 * @see <a href="https://www.runoob.com/w3cnote/java-capture-group.html">Java 正则表达式的捕获组</a>
	 */
	@Test
	public void test(){
		String html = "<a>1111</a> <a>2222</b>";
		String expected = "<a>1111</a>";

		Matcher matcher = Pattern.compile("<([A-Za-z]+?)>[\\s\\S]*?</\\1>")
								.matcher(html);

		String actual = "";
		if (matcher.find()){
			actual = matcher.group();
		}
		System.out.println("actual: " + actual);

		Assert.assertTrue(StringUtils.equals(expected, actual));
	}
}
