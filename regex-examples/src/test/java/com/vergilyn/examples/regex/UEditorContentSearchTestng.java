package com.vergilyn.examples.regex;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.testng.annotations.Test;

/**
 * 需求：ueditor编辑的富文本内容，为匹配到的<strong>第一个关键字</strong>添加相应的链接。
 * <p>
 *   1. html标签内的关键字不能替换，例如{@code <p alt="百度">内容</p>} <br/>
 *   2. 链接本身包含的关键字不能替换，例如{@code <a href="...">链接到百度</a>}
 * </p>
 *
 * @author dingmaohai
 * @since 2020/11/06 11:26
 *
 * @see <a href="https://my.oschina.net/kaishui/blog/1532092">富文本替换关键字做SEO内链</a>
 */
public class UEditorContentSearchTestng {

	private static final String CONTENT_HTML = "<p>\n" + "    这是一段ueditor菜鸟<br/>\n" + "</p>\n" + "<p>\n"
			+ "    <strong>加粗菜鸟</strong>\n" + "</p>\n" + "<p>\n"
			+ "    <span alt=\"微软\"/>\n" + "</p>\n" + "<p>\n"
			+ "    <span style=\"font-family: 微软雅黑, &quot;Microsoft YaHei&quot;;\">微软雅黑菜鸟</span>\n" + "</p>\n" + "<p>\n"
			+ "    <a href=\"http://www.baidu.com\" target=\"_blank\">超链接到百度</a>\n" + "</p>";

	private static final List<DefaultKeyValue<String, String>> KEYWORDS = Lists.newArrayList();
	static {
		KEYWORDS.add(new DefaultKeyValue<>("百度", "http://www.baidu.com"));
		KEYWORDS.add(new DefaultKeyValue<>("微软", "http://www.microsoft.com"));
		KEYWORDS.add(new DefaultKeyValue<>("菜鸟", "http://www.runoob.com"));
	}

	@Test
	public void demandRegex(){
		String source = CONTENT_HTML;
		/*
		 * 1. 找到完整的 a 标签 <br/>
		 * 2. 找到 html的开始标签 `<...>`，所以文本中如果是{@code "<p>这是<百度百科>的链接</p>"}，其中的"百度"也不会被处理 <br/>
		 * 3. 找到 html的结束标签 `</...>` （已经包含在`2`中）<br/>
		 * 4. 简写标签 例如`<input alt="微软"/>` （经包含在`2`中） <br/>
		 *
		 * FIXME 2020-11-06
		 *   由于`2.`导致类似"其它内容 >>在线咨询<<  这一段的百度关键字不会添加廉价 >>在线咨询<<" 也无法处理。
		 */
		String regex = "((<a[\\s\\S]*?>[\\s\\S]*?</a>)|<[\\s\\S]+?>)";
		Pattern pattern = Pattern.compile(regex);

		Matcher m = pattern.matcher(source);
		int last = 0, indexOf = -1, minIndexOf = Integer.MAX_VALUE;
		DefaultKeyValue<String, String> keyword = null;

		while (m.find()) {
			// 提取没有匹配的文本，查找关键字
			String replaceSub = source.substring(last, m.start());
			if (StringUtils.isNotBlank(replaceSub)){
				for (DefaultKeyValue<String, String> key : KEYWORDS){
					indexOf = replaceSub.indexOf(key.getKey());
					indexOf = indexOf >= 0 ? indexOf + last : -1;
					if (indexOf >= 0 && indexOf < minIndexOf){
						minIndexOf = indexOf;
						keyword = key;
						// break;  // 不能break，后续关键字可能更靠前
					}
				}
			}

			last = m.end();
		}

		if (keyword == null){
			System.out.println("not found");
			return;
		}

		int minIndexOfEnd = minIndexOf + keyword.getKey().length();

		System.out.printf("minIndexOf: %d, key: %s, href: %s\r\n"
				, minIndexOf, keyword.getKey(), keyword.getValue());
		System.out.printf("sub-keyword: [%s] \r\n", source.substring(minIndexOf, minIndexOfEnd));

		String rs = source.substring(0, minIndexOf);
		rs += "<a href=\"" + keyword.getValue() + "\">" + keyword.getKey() + "</a>";
		rs += source.substring(minIndexOfEnd);

		System.out.println("result: \r\n" + rs);
	}

	/**
	 * TODO 2020-11-06
	 */
	@Test
	public void jsoup() {
		Document document = Jsoup.parse(CONTENT_HTML);
		Element body = document.body();

		System.out.println(body.html());
	}

	/**
	 * @see <a href="https://www.cnblogs.com/wisewrong/p/12931158.html">用正则匹配富文本中的文本，并替换其内容</a>
	 * @see <a href="https://blog.csdn.net/newborn2012/article/details/18262677">正则表达式中的的向前匹配、向后匹配、负向前匹配、负向后匹配写法</a>
	 */
	@Test
	public void searchContent() {
		// 匹配标签之间的文本
		String regex = "(?<=>)(.|\\s)*?(?=</?\\w+[^<]*>)";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(CONTENT_HTML);

		int count = 0;
		System.out.println("find >>>> ");
		while (matcher.find()) {
			int groupCount = matcher.groupCount();
			for (int i = 0; i < groupCount; i++) {
				System.out.printf("%d: %s\r\n", count++, matcher.group(i));
			}
		}
		System.out.println("end <<<< ");
	}

	/**
	 * 思路：先找到需要exclude的文本，例如 HTML标签、a标签。再获取需要查找关键字的文本进行替换。
	 *
	 * @see <a href="https://my.oschina.net/kaishui/blog/1532092">富文本替换关键字做SEO内链</a>
	 */
	@Test
	public void test(){
		String source = new String(CONTENT_HTML);
		// `2`其实已经包含了`3`
		String regex = "((<a[\\s\\S]*?>[\\s\\S]*?</a>)|<[\\s\\S]+?>)|(</[\\s\\S]+?>)";
		Pattern p = Pattern.compile(regex);

		// 获取 matcher 对象
		Matcher m = p.matcher(source);
		//保存结果
		StringBuffer result = new StringBuffer();
		//上一个结束位置
		int lastEnd = 0;

		String keyword = "百度";
		//是否匹配
		while (m.find()) {
			int start = m.start();
			int end = m.end();
			System.out.println("group: " + m.group());
			System.out.println("start: " + start + " end: " + end);

			//提取没有匹配段，替换关键字并拼接
			String replaceSub = source.substring(lastEnd, start);
			result.append(replaceSub.replaceAll(keyword, "<a>" + keyword + "</a>"));
			System.out.printf("lastEnd: %d, start: %d, replaceSub: %s\r\n", lastEnd, start, replaceSub);
			//拼接匹配段
			String appendSub = source.substring(start, end);
			result.append(appendSub);
			System.out.printf("start: %d, end: %d, appendSub: %s\r\n", start, end, appendSub);


			System.out.printf("result: %s \r\n\r\n", result.toString());
			//作为下一个未匹配段的开始下标
			lastEnd = end;
		}
		//最后未匹配段
		result.append(source.substring(lastEnd).replaceAll(keyword, "<a>" + keyword + "</a>"));
		System.out.println("finally-result: " + result.toString());
	}
}
