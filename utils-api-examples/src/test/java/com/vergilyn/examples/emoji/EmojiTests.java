package com.vergilyn.examples.emoji;

import com.vdurmont.emoji.EmojiParser;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class EmojiTests {

	@Test
	public void test() {
		String str = "中文123\uD83D\uDE04456";

		System.out.println(EmojiParser.parseToAliases(str));
		System.out.println(EmojiParser.replaceAllEmojis(str, "*"));
	}

	/**
	 * 处理表情符号
	 *
	 * @param str
	 * @return
	 */
	public String parseToAliases(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		return filterEmoji(StringUtils.isBlank(str) ? "" : EmojiParser.parseToAliases(str));
	}

	/**
	 * 表情符号还原
	 *
	 * @param str
	 * @return
	 */
	public String parseToUnicode(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}

		return EmojiParser.parseToUnicode(str);
	}

	private String filterEmoji(String source) {
		if (source != null) {
			return source.replaceAll("[^\\u0000-\\uFFFF]", "");
		}
		return source;
	}
}
