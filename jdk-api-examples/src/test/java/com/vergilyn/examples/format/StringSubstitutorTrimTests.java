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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class StringSubstitutorTrimTests {

	/**
	 * <b>期望:</b> 不管是`ScriptEngine`还是`自定义变量varMap`，最后的 value 都默认进行 {@link String#trim()}
	 *
	 * <p> 它们最终都是由 {@linkplain org.apache.commons.text.lookup.InterpolatorStringLookup#lookup(String) InterpolatorStringLookup#lookup(String)} 进行解析并获取最终的获取值。
	 *  <br/> 所以最好的方式是，参考源码实现类似的 `InterpolatorStringLookup` 进行扩展
	 */
	@ParameterizedTest
	@ValueSource(classes = {InterpolatorExtensionStringLookup.class, Void.class})
	public void trim(Class<?> stringLookupClass){
		Map<String, String> varMap = Maps.newHashMap();
		varMap.put("var-express", "3 + 4");
		varMap.put("trim", "  javascript:${defalut-: 4 + 5}  ");

		StringLookup defaultStringLookup = StringLookupFactory.INSTANCE.mapStringLookup(varMap);

		StringLookup stringLookup;
		String pattern;

		if (stringLookupClass == InterpolatorExtensionStringLookup.class){
			// 特别注意，`${script::javascript:${var-express}}`
			// 1. `InterpolatorExtensionStringLookup` 也只是改变了`script`的第一层解析，后续还只能是`javascript:`
			stringLookup = new InterpolatorExtensionStringLookup(
					Maps.newHashMap(), defaultStringLookup, "::", s -> s == null ? null : s.trim(), true);

			pattern = "3 + 4 = ${script::javascript:${var-express}}；4 + 5 = ${script::${trim}}";

		}else {
			stringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
					Maps.newHashMap(), defaultStringLookup, true
			);

			pattern = "3 + 4 = ${script:javascript:${var-express}}；4 + 5 = ${script:${trim}}";
		}

		StringSubstitutor substitutor = new StringSubstitutor(stringLookup);
		substitutor.setEnableSubstitutionInVariables(true);
		substitutor.setValueDelimiter("-:");

		String replace = substitutor.replace(pattern);

		Assertions.assertThat(replace)
				.isEqualTo("3 + 4 = 7；4 + 5 = 9");

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
		varMap.put("trim", "  javascript:${defalut-: 4 + 5}  ");

		StringLookup defaultStringLookup = StringLookupFactory.INSTANCE.mapStringLookup(varMap);

		StringLookup stringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
					Maps.newHashMap(), defaultStringLookup, true
			);

		String pattern = "3 + 4 = ${script:javascript:${var-express}}；4 + 5 = ${script:${trim}}";

		StringSubstitutor substitutor = new StringSubstitutor(stringLookup);
		substitutor.setEnableSubstitutionInVariables(true);
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


	/**
	 * 1. 扩展：`value` 后置处理。<br/>
	 * 2. 扩展：自定义分隔符。<br/>
	 * @see org.apache.commons.text.lookup.InterpolatorStringLookup
	 */
	public static class InterpolatorExtensionStringLookup implements StringLookup {
		/** Constant for the prefix separator. */
		private static final String PREFIX_SEPARATOR = ":";

		private final String prefixSeparator;

		/** The default string lookup. */
		private final StringLookup defaultStringLookup;

		/** The map of String lookups keyed by prefix. */
		private final Map<String, StringLookup> stringLookupMap;

		/**
		 * 后置处理替换后的`value`，例如 统一处理 `value.trim()`
		 */
		private final Function<String, String> valuePostProcessor;

		/**
		 * Creates a fully customized instance.
		 *
		 * @param stringLookupMap the map of string lookups.
		 * @param defaultStringLookup the default string lookup.
		 * @param addDefaultLookups whether the default lookups should be used.
		 */
		public InterpolatorExtensionStringLookup(final Map<String, StringLookup> stringLookupMap,
										final StringLookup defaultStringLookup,
										String prefixSeparator,
										Function<String, String> valuePostProcessor,
										final boolean addDefaultLookups) {

			this.defaultStringLookup = defaultStringLookup;
			this.stringLookupMap = new HashMap<>(stringLookupMap.size());
			this.prefixSeparator = prefixSeparator == null || prefixSeparator.isEmpty()
			                            ? PREFIX_SEPARATOR : prefixSeparator;

			this.valuePostProcessor = valuePostProcessor;

			for (final Map.Entry<String, StringLookup> entry : stringLookupMap.entrySet()) {
				this.stringLookupMap.put(toKey(entry.getKey()), entry.getValue());
			}

			if (addDefaultLookups) {
				StringLookupFactory.INSTANCE.addDefaultStringLookups(this.stringLookupMap);
			}
		}

		@Override
		public String lookup(String var) {
			if (var == null) {
				return null;
			}

			final int prefixPos = var.indexOf(prefixSeparator);
			int separatorLength = prefixSeparator.length();

			if (prefixPos >= 0) {
				final String prefix = toKey(var.substring(0, prefixPos));
				final String name = var.substring(prefixPos + separatorLength);
				final StringLookup lookup = stringLookupMap.get(prefix);
				String value = null;
				if (lookup != null) {
					value = lookup.lookup(name);
				}

				if (value != null) {
					return postProcessor(value);
				}
				var = var.substring(prefixPos + separatorLength);
			}

			if (defaultStringLookup != null) {
				String value = defaultStringLookup.lookup(var);
				return postProcessor(value);
			}

			return null;
		}

		protected String postProcessor(String value){
			if (valuePostProcessor == null){
				return value;
			}

			return valuePostProcessor.apply(value);
		}

		public StringLookup getDefaultStringLookup() {
			return defaultStringLookup;
		}

		public Map<String, StringLookup> getStringLookupMap() {
			return stringLookupMap;
		}

		@Override
		public String toString() {
			return super.toString() + " [stringLookupMap=" + stringLookupMap + ", defaultStringLookup="
					+ defaultStringLookup + "]";
		}

		public static String toKey(final String key) {
			return key.toLowerCase(Locale.ROOT);
		}
	}
}
