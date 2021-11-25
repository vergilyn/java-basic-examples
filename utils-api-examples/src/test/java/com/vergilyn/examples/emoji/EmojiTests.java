package com.vergilyn.examples.emoji;

import com.vdurmont.emoji.EmojiParser;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class EmojiTests {
	private final String emojiStr = "中文123\uD83D\uDE04456";

	/**
	 * @see cn.hutool.core.util.StrUtil#subByCodePoint(CharSequence, int, int)
	 */
	@Test
	public void substring(){
		// 直接substring 会导致emoji不完整。
		System.out.println(StringUtils.substring(emojiStr, 0, 6));

		System.out.println(StrUtil.subByCodePoint(emojiStr, 0, 6));

	}

	@Test
	public void test() {
		System.out.println(EmojiParser.parseToAliases(emojiStr));
		System.out.println(EmojiParser.replaceAllEmojis(emojiStr, "*"));
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
