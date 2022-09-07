package com.vergilyn.examples.format;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.TextStringBuilder;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.apache.commons.text.matcher.StringMatcher;
import org.apache.commons.text.matcher.StringMatcherFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 提取文本中的 var-name
 * <p> 例如 "3 + 4 = ${script:javascript:${var-express}}；4 + 5 = ${script:${trim}}"
 *  <br/> 其中的 variable-name 指: `var-express`, `trim`
 *
 *
 * @author vergilyn
 * @since 2022-09-05
 */
public class StringSubstitutorExtractVariableNameTests {

	/**
	 * <b>思路：</b> 反正`StringSubstitutor`线程不安全，那么可以通过 defaultStringLookup 保存 variableNames。
	 *
	 * <p><b>缺陷：</b>
	 * <br/> 1. 必须执行 replace。（实际应用中，可能 replace 比较耗时。例如 CustomStringLookup 中可能查询数据库等。）
	 * <br/> 2. 如果是 script 嵌套 variable，可能抛出异常。
	 *
	 * <p><b>完全不推荐此方式，只是提供一种思路参考。</b>
	 */
	@Test
	public void extractVariableName(){
		Map<String, String> varMap = Maps.newHashMap();
		varMap.put("var-express", "3 + 4");
		// 注释后，会抛出异常
		varMap.put("trim", "javascript:${default-: 4 + 5}");

		String pattern = "3 + 4 = ${script:javascript:${var-express}}；4 + 5 = ${script:${trim}}";

		// 因为是通过`defaultStringLookup`的方式获取，所以`script:javascript:xx` 不会被记录到 `variableNames`
		Set<String> expectedVariableNames = Sets.newHashSet("var-express", "trim", "default");
		Set<String> variableNames = Sets.newHashSet();
		StringLookup defaultStringLookup = new StringLookup() {
			@Override
			public String lookup(String key) {
				variableNames.add(key);
				return varMap.get(key);
			}
		};

		StringLookup stringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
				Maps.newHashMap(), defaultStringLookup, true
		);


		StringSubstitutor substitutor = new StringSubstitutor(stringLookup);
		substitutor.setEnableSubstitutionInVariables(true);
		substitutor.setDisableSubstitutionInValues(false);
		substitutor.setValueDelimiter("-:");

		substitutor.replace(pattern);

		System.out.println(JSON.toJSONString(variableNames));
		Assertions.assertThat(variableNames).hasSameElementsAs(expectedVariableNames);
	}

	/**
	 * 参考{@link StringSubstitutor#substitute(TextStringBuilder, int, int, List)} 实现提取 自定义变量名称 的方法。
	 *
	 * <p> <b>难点：</b> 同时存在 `自定义变量` 和 `内嵌函数`时，比较难解析提取有哪些varNames。
	 * <p> <b>比较推荐的方式：</b> 区分 `自定义变量 ${var}` 和 `内嵌函数 #{func:xx}`，使用不同的 {@link StringSubstitutor}。
	 * <pre> 例如 "#{toStatsUrl:${var}}"
	 *     思路：先解析表达式 `${...}`, 替换后再执行 内置函数 `#{func:xx}`。
	 *
	 *     1) 这样会有2套 StringSubstitutor。（并且 new了多个 StringSubstitutor对象）
	 *     2) `${var-:#{toStatsUrl:xx}}`  其实可以支持.
	 * </pre>
	 */
	@Test
	public void implementExtract(){
		Map<String, String> values = new HashMap<>();
		//region 1. 自定义变量`${var}`
		StringSubstitutor variableSubstitutor = new StringSubstitutor(values);
		variableSubstitutor.setVariablePrefix("${");
		variableSubstitutor.setVariableSuffix("}");
		variableSubstitutor.setEscapeChar('$');
		variableSubstitutor.setValueDelimiter("-:");
		variableSubstitutor.setEscapeChar('$');
		variableSubstitutor.setEnableSubstitutionInVariables(true);
		//endregion

		//region 2. 内嵌函数 `#{func}`
		Map<String, StringLookup> customFunc = new HashMap<>();
		customFunc.put("func", key -> key);
		StringLookup stringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
				customFunc, null, false
		);
		StringSubstitutor funcSubstitutor = new StringSubstitutor(stringLookup);
		funcSubstitutor.setVariablePrefix("#{");
		variableSubstitutor.setEscapeChar('{');
		funcSubstitutor.setVariableSuffix("#");
		funcSubstitutor.setValueDelimiter("-:");
		funcSubstitutor.setEscapeChar('#');
		funcSubstitutor.setEnableSubstitutionInVariables(true);
		//endregion


		String source =
				"The ${animal.1} is ${animal.${species}};\n"
						+ " ${default.true-:1234};\n"
						// 存在默认值 + 不存在默认值，意味着也必须传。
						+ " ${default.false-:4321} - ${default.false};\n"
						// 假设 `var-value-nested-exp = 123${var1}456`，无法提取 var-value中存在的自定义变量参数名称。
						+ " ${var-express-:${var-value-nested-exp}};\n"

						// 内置函数
						+ " #{func.toStatsUrl:${url}};\n"
						+ " #{func.toStatsUrl:${url.default-:https://www.baidu.com}};\n"
						+ " #{func.toStatsUrl:https://d.baidu.com/1234};\n"
						+ " #{func.toStatsUrl:https://d.baidu.com/${url.code}};\n"
						+ " #{func.toStatsUrl:https://d.baidu.com/${url.code.default-:1234}};\n"

						// 无法区分 `自定义变量` 和 `内嵌函数`。
						+ " ${func.toStatsUrl:https://d.baidu.com/${url.code.2}};\n"
				;


		// 有序
		Map<String, Boolean> expectedOrdered = new LinkedHashMap<>();
		expectedOrdered.put("animal.1", false);
		expectedOrdered.put("species", false);
		expectedOrdered.put("animal.${species}", false);
		expectedOrdered.put("default.true", true);
		expectedOrdered.put("default.false", false);
		expectedOrdered.put("var-value-nested-exp", false);
		expectedOrdered.put("var-express", true);
		expectedOrdered.put("url", false);
		expectedOrdered.put("url.default", true);
		expectedOrdered.put("url.code", false);
		expectedOrdered.put("url.code.default", true);

		expectedOrdered.put("url.code.2", false);
		// 这个其实是不满足 期望的。
		expectedOrdered.put("func.toStatsUrl:https://d.baidu.com/${url.code.2}", false);


		LinkedHashMap<String, Boolean> actualVarNames = new LinkedHashMap<>();

		TextStringBuilder builder = new TextStringBuilder(source);
		extract(actualVarNames, builder, 0, source.length());

		actualVarNames.entrySet()
				.forEach(entry -> System.out.printf("%s, \t %s \n", entry.getKey(), entry.getValue()));

		Assertions.assertThat(actualVarNames).containsExactlyEntriesOf(expectedOrdered);
	}

	/**
	 * <p> <h3>思路</h3>
	 * <p> 1. 完全参考 {@link StringSubstitutor#substitute(TextStringBuilder, int, int, List)}，
	 *   利用入参{@code variableNameMap}记录 varNames。
	 *
	 * <p> 2. <b>永远是从`最内层` 往 `最外层`开始解析。</b>
	 *
	 * <p> <h3>缺陷</h3>
	 * <p> 1. 无法区分 `自定义变量` 和 `内嵌函数`。
	 *   <br/> TODO 2022-09-07，其实也可以实现，但需要扩展{@linkplain org.apache.commons.text.lookup.InterpolatorStringLookup InterpolatorStringLookup}
	 *
	 * <p> <h3>备注</h3>
	 * <p> 1. 源代码中的`{@code List<String> priorVariables}` 其实是用来 记录和判断是否存在 无限嵌套引用。
	 *   <br/> 参考：{@link StringSubstitutor#checkCyclicSubstitution(String, List)}
	 *
	 * @param variableNameMap
	 *  - key, variable-name
	 *  - value, false 表示不存在默认值；true，表示存在默认值。
	 */
	public void extract(LinkedHashMap<String, Boolean> variableNameMap, TextStringBuilder builder, int offset, int length){
		// Objects.requireNonNull(builder, "builder");
		final StringMatcher prefixMatcher = StringMatcherFactory.INSTANCE.stringMatcher("${");
		final StringMatcher suffixMatcher = StringMatcherFactory.INSTANCE.stringMatcher("}");
		final StringMatcher valueDelimMatcher = StringMatcherFactory.INSTANCE.stringMatcher("-:");
		final boolean substitutionInVariablesEnabled = true;
		// 特别：无法提取 var-value 中存在的自定义变量参数名称。
		final boolean substitutionInValuesDisabled = false;

		int bufEnd = offset + length;
		int pos = offset;
		while (pos < bufEnd) {
			final int startMatchLen = prefixMatcher.isMatch(builder, pos, offset, bufEnd);
			if (startMatchLen == 0) {
				pos++;
				continue;
			}

			// find suffix
			int startPos = pos;
			pos += startMatchLen;
			int endMatchLen = 0;
			int nestedVarCount = 0;
			while (pos < bufEnd) {
				if (substitutionInVariablesEnabled && prefixMatcher.isMatch(builder, pos, offset, bufEnd) != 0) {
					// found a nested variable start
					endMatchLen = prefixMatcher.isMatch(builder, pos, offset, bufEnd);
					nestedVarCount++;
					pos += endMatchLen;
					continue;
				}

				endMatchLen = suffixMatcher.isMatch(builder, pos, offset, bufEnd);
				if (endMatchLen == 0) {
					pos++;
					continue;
				}

				// found variable end marker
				if (nestedVarCount == 0) {
					// get var name.
					// 例如 `The ${animal.1} is ${animal.${species}};`
					//   varNameExpr = `animal.1`
					//   varNameExpr = `animal.${species}`
					//   因为存在嵌套表达式，所有先（递归）替换内部的嵌套表达式，然后再替换外层。
					String varNameExpr = builder.midString(startPos + startMatchLen,
					                                       pos - startPos - startMatchLen);
					if (substitutionInVariablesEnabled) {
						final TextStringBuilder bufName = new TextStringBuilder(varNameExpr);
						extract(variableNameMap, bufName, 0, bufName.length());
						varNameExpr = bufName.toString();
					}
					pos += endMatchLen;

					String varName = varNameExpr;
					String varDefaultValue = null;

					if (valueDelimMatcher != null) {
						final char[] varNameExprChars = varNameExpr.toCharArray();
						int valueDelimiterMatchLen = 0;
						for (int i = 0; i < varNameExprChars.length; i++) {
							// if there's any nested variable when nested variable substitution disabled,
							// then stop resolving name and default value.
							if (!substitutionInVariablesEnabled
									&& prefixMatcher.isMatch(varNameExprChars, i, i, varNameExprChars.length) != 0) {
								break;
							}

							if (valueDelimMatcher.isMatch(varNameExprChars, i, 0, varNameExprChars.length) != 0) {
								valueDelimiterMatchLen = valueDelimMatcher.isMatch(varNameExprChars, i, 0,
								                                                   varNameExprChars.length);
								varName = varNameExpr.substring(0, i);
								varDefaultValue = varNameExpr.substring(i + valueDelimiterMatchLen);
								break;
							}
						}
					}

					boolean isDefaultValue = varDefaultValue != null;

					if (variableNameMap.containsKey(varName)){
						// 都存在默认值时，才为 true。
						variableNameMap.put(varName, variableNameMap.get(varName) && isDefaultValue);
					}else {
						variableNameMap.put(varName, isDefaultValue);
					}

					break;
				}
				nestedVarCount--;
				pos += endMatchLen;
			}
		}
	}


}
