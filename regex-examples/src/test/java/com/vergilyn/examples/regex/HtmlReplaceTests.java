package com.vergilyn.examples.regex;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Data;
import org.junit.jupiter.api.Test;

public class HtmlReplaceTests {

	/**
	 * 整个富文本indexof，不考虑keyword为html标签内容，例如 <a title="百度"...
	 */
	@Test
	public void normalAddHref(){
		String html = "<p>1111重庆家电维修22222家电维修</p>";

		List<KeyHref> keywords = Lists.newArrayList();
		keywords.add(new KeyHref("家电维修", "http://www.jdwx.com"));
		keywords.add(new KeyHref("重庆家电维修", "https://www.cqjdwx.com/"));

		// 转换内链关键字为MAP集合，key没有意义。
		Map<Integer, KeyHref> containKey = Maps.newLinkedHashMapWithExpectedSize(keywords.size());
		int i = 0;
		for(KeyHref keyword : keywords){
			containKey.put(i++, keyword);
		}

		StringBuilder target = new StringBuilder();
		int begin = 0;
		boolean isContinue = true;
		do {
			// 找到关键字出现的位置{key: indexOf, value: KeyHref}的集合，未出现的关键字舍弃
			containKey = containKey(html, containKey);

			// 找到最小的indexof（即在html中最先出现的关键字）
			int minIndexOf = minIndexOf(containKey);
			if (minIndexOf >= 0){  // html匹配到关键字

				KeyHref fk = containKey.remove(minIndexOf);

				// 关键字添加外链，以及拼接最终结果
				target.append(html, begin, minIndexOf)
						.append(addHref(fk));

				// 继续从最先匹配的关键字之后的文本中 再次查找剩余的关键字进行添加外链
				html = html.substring(minIndexOf + fk.getKeyword().length());
			}else{
				isContinue = false;
				target.append(html);
			}

		}while (isContinue);

		System.out.println(target);
	}

	private int minIndexOf(Map<Integer, KeyHref> containKey){
		if (containKey == null || containKey.isEmpty()){
			return -1;
		}

		int minIndexOf = Integer.MAX_VALUE;
		// LinkedHashMap.keySet() 能否保证有序性？ 可以保证！
		for (Integer i : containKey.keySet()){
			minIndexOf = Math.min(minIndexOf, i);
		}

		return minIndexOf;
	}

	private Map<Integer, KeyHref> containKey(String html, Map<Integer, KeyHref> containKey){
		if (containKey == null || containKey.isEmpty()){
			return null;
		}

		Map<Integer, KeyHref> result = Maps.newLinkedHashMapWithExpectedSize(containKey.size());
		for (Map.Entry<Integer, KeyHref> keyword : containKey.entrySet()){
			int indexOf = html.indexOf(keyword.getValue().getKeyword());
			if (indexOf >= 0){
				result.put(indexOf, keyword.getValue());
			}
		}

		return result;
	}

	private String addHref(KeyHref key){
		return "<a href=\"" + key.href + "\">" + key.keyword + "</a>";
	}

	@Data
	static class KeyHref{
		private String keyword;
		private String href;

		public KeyHref(String keyword, String href) {
			this.keyword = keyword;
			this.href = href;
		}
	}
}
