package com.vergilyn.examples.pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import net.sourceforge.pinyin4j.multipinyin.MultiPinyinConfig;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-16
 *
 * @see <a href="	https://github.com/belerweb/pinyin4j">belerweb/pinyin4j</a>
 */
public class Pinyin4jTests {

	@Test
	public void test(){
		MultiPinyinConfig.multiPinyinPath = "";
		String cq = "重庆";
		String weight = "重量";

		System.out.printf("[vergilyn] >>>> #getFirstPinYin() %s, %s \n", cq, getFirstPinYin(cq));
		System.out.printf("[vergilyn] >>>> #getFirstPinYin() %s, %s \n", weight, getFirstPinYin(weight));

		System.out.printf("[vergilyn] >>>> #getMultiPinYin() %s, %s \n", cq, getMultiPinYin(cq));
		System.out.printf("[vergilyn] >>>> #getMultiPinYin() %s, %s \n", weight, getMultiPinYin(weight));
	}

	private String getFirstPinYin(String cn){
		//输出格式设置
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		/**
		 * 输出大小写设置
		 *
		 * LOWERCASE:输出小写
		 * UPPERCASE:输出大写
		 */
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

		/**
		 * 输出音标设置
		 *
		 * WITH_TONE_MARK:直接用音标符（必须设置WITH_U_UNICODE，否则会抛出异常）
		 * WITH_TONE_NUMBER：1-4数字表示音标
		 * WITHOUT_TONE：没有音标
		 */
		format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

		/**
		 * 特殊音标ü设置
		 *
		 * WITH_V：用v表示ü
		 * WITH_U_AND_COLON：用"u:"表示ü
		 * WITH_U_UNICODE：直接用ü
		 */
		// format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

		char[] hanYuArr = cn.trim().toCharArray();
		StringBuilder pinYin = new StringBuilder();

		try {
			for (int i = 0, len = hanYuArr.length; i < len; i++) {
				//匹配是否是汉字
				if (Character.toString(hanYuArr[i]).matches("[\\u4E00-\\u9FA5]+")) {
					//如果是多音字，返回多个拼音，这里只取第一个
					String[] pys = PinyinHelper.toHanyuPinyinStringArray(hanYuArr[i], format);
					pinYin.append(pys[0]).append(" ");
				} else {
					pinYin.append(hanYuArr[i]).append(" ");
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
			badHanyuPinyinOutputFormatCombination.printStackTrace();
		}
		return pinYin.toString();
	}

	private String getMultiPinYin(String cn){
		//输出格式设置
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);

		try {
			return PinyinHelper.toHanYuPinyinString(cn, format, ",", true);
		} catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
			badHanyuPinyinOutputFormatCombination.printStackTrace();
			return "NULL";
		}
	}
}
