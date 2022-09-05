package com.vergilyn.examples.format;

import com.google.common.collect.Maps;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.TextStringBuilder;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

/**
 * 1. 期望：value 默认 `trim` <br/>
 * 2. 特殊替换：`null`替换成 ""，例如 `这是一段{$var}完整的文本`，`var = "null"`，替换后为`这是一段完整的文本` <br/>
 */
public class StringSubstitutorExtensionTests {

	/**
	 * <b>期望:</b> 不管是`ScriptEngine`还是`自定义变量varMap`，最后的 value 都默认进行 {@link String#trim()}
	 *
	 * <p> 它们最终都是由 {@linkplain org.apache.commons.text.lookup.InterpolatorStringLookup#lookup(String) InterpolatorStringLookup#lookup(String)} 进行解析并获取最终的获取值。
	 *  <br/> 所以最好的方式是，参考源码实现类似的 `InterpolatorStringLookup` 进行扩展
	 */
	@ParameterizedTest
	@ValueSource(classes = { ExtensionInterpolatorStringLookup.class, Void.class})
	public void trim(Class<?> stringLookupClass){
		Map<String, String> varMap = Maps.newHashMap();
		varMap.put("var-express", "3 + 4");
		varMap.put("trim", "  javascript:${defalut-: 4 + 5}  ");
		varMap.put("empty", "null");

		StringLookup defaultStringLookup = StringLookupFactory.INSTANCE.mapStringLookup(varMap);

		String expected = "extension >>>> 3 + 4 = 7；4 + 5 = 9";
		String pattern = "extension ${empty}>>>> 3 + 4 = ${script:javascript:${var-express}}；4 + 5 = ${script:${trim}}";

		StringLookup stringLookup;

		if (stringLookupClass == ExtensionInterpolatorStringLookup.class){
			// 特别注意，`${script::javascript:${var-express}}`
			// 1. `InterpolatorExtensionStringLookup` 也只是改变了`script`的第一层解析，后续还只能是`javascript:`
			stringLookup = new ExtensionInterpolatorStringLookup(
					Maps.newHashMap(), defaultStringLookup, ":",
					value -> {
						if (value == null){
							return null;
						}
						// 1. 默认进行 trim
						value = value.trim();

						// 2. 特殊值处理。
						return "null".equalsIgnoreCase(value) ? "" : value;
					},
					true);

		}else {
			stringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
					Maps.newHashMap(), defaultStringLookup, true
			);

		}

		StringSubstitutor substitutor = new StringSubstitutor(stringLookup);
		substitutor.setEnableSubstitutionInVariables(true);
		substitutor.setValueDelimiter("-:");

		String replace = substitutor.replace(pattern);

		System.out.println("[replaced]" + replace);
		Assertions.assertThat(replace).isEqualTo(expected);

	}

	/**
	 * 通过核心源码：{@link StringSubstitutor#substitute(TextStringBuilder, int, int, List)} 可知，
	 * 还可以通过重写 {@link TextStringBuilder#replace(int, int, String)} 来达到 `trim` 的目的。
	 *
	 * <p> <b>这种方式不友好，且有BUG</b>
	 * <p> 如果存在嵌套表达式，`#substitute(...)`内部会重新 `new TextStringBuilder`，此时并未进行`value.trim()`。
	 * <pre> 参考源码：{@linkplain StringSubstitutor#substitute(TextStringBuilder, int, int, List)}, line: 1390
	 *   if (substitutionInVariablesEnabled) {
	 *       final TextStringBuilder bufName = new TextStringBuilder(varNameExpr);
	 *       substitute(bufName, 0, bufName.length());
	 *       varNameExpr = bufName.toString();
	 *   }
	 * </pre>
	 */
	@Test
	public void trim2(){
		Map<String, String> varMap = Maps.newHashMap();
		varMap.put("var-express", "3 + 4");
		varMap.put("trim", "  javascript:${default-: 4 + 5}  ");

		StringLookup defaultStringLookup = StringLookupFactory.INSTANCE.mapStringLookup(varMap);

		StringLookup stringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
					Maps.newHashMap(), defaultStringLookup, true
			);

		String pattern = "3 + 4 = ${script:javascript:${var-express}}；4 + 5 = ${script:${trim}}";

		StringSubstitutor substitutor = new StringSubstitutor(stringLookup);
		substitutor.setEnableSubstitutionInVariables(true);
		substitutor.setDisableSubstitutionInValues(false);
		substitutor.setValueDelimiter("-:");

		TextStringBuilder textStringBuilder = new TextStringBuilder(pattern) {
			@Override
			public TextStringBuilder replace(int startIndex, int endIndex, String replaceStr) {
				replaceStr = replaceStr == null ? null : replaceStr.trim();
				return super.replace(startIndex, endIndex, replaceStr);
			}
		};

		// java.lang.IllegalArgumentException: Error in script engine [  javascript] evaluating script [ 4 + 5  ].
		substitutor.replaceIn(textStringBuilder);

		Assertions.assertThat(textStringBuilder.toString())
				.isEqualTo("3 + 4 = 7；4 + 5 = 9");
	}

}
