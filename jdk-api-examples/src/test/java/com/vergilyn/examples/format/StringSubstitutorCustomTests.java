package com.vergilyn.examples.format;

import com.google.common.collect.Maps;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.TextStringBuilder;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人实际中遇到的使用场景：<br/>
 * 1. 表达式变为 `{$var}` <br/>
 * 2. 支持 默认值表达式 `{$var-:123}` <br/>
 * 3. 支持 嵌套表达式 `{$var-:${v}}` <br/>
 * 4. 支持 自定义函数表达式， 例如 `{$vergilyn-urlTrace:${url}}` <br/>
 * 5. 期望：默认 trim。参考 {@link StringSubstitutorExtensionTests}<br/>
 * 5.1 特殊替换：`null`替换成 ""，例如 `这是一段{$var}完整的文本`，`var = "null"`，替换后为`这是一段完整的文本` <br/>
 * 6. 存在未替换的表达式时，抛出异常 <br/>
 *
 * @author vergilyn
 * @since 2022-09-02
 */
@SuppressWarnings("JavadocReference")
public class StringSubstitutorCustomTests {

	// 线程不安全
	private StringSubstitutor _defaultSubstitutor;

	private Map<String, String> _defaultCustomVarMap = new HashMap<String, String>(){{
		put("express", "3+4");
		put("vergilyn-urlTrace", "error-replace");
	}};

	@BeforeEach
	public void beforeEach(){

		Map<String, StringLookup> stringLookupMap = Maps.newHashMap();
		// 自定义函数表达式
		stringLookupMap.put("vergilyn-urlTrace", key -> {
			return key + "?_sp=mcs.10086.sms.1234";
		});

		_defaultSubstitutor = buildStringSubstitutor(_defaultCustomVarMap, stringLookupMap, true);
	}

	/**
	 * <b>`StringSubstitutor` 线程不安全！！！</b>
	 * @return
	 */
	private StringSubstitutor buildStringSubstitutor(Map<String, String> customVarMap,
			Map<String, StringLookup> stringLookupMap,
			boolean isAddDefaultLookups){
		StringLookupFactory stringLookupFactory = StringLookupFactory.INSTANCE;


		// 第1个参数：自定义扩展的一些 StringLookup，例如`vergilyn-urlTrace`
		// 第2个参数：如果第一个参数中未找到对应的相关的StringLookup，则用 此mapStringLookup 解析。
		// 第3个参数：是否添加默认的Lookups。
		StringLookup stringLookup = stringLookupFactory.interpolatorStringLookup(stringLookupMap,
		                                                                         stringLookupFactory.mapStringLookup(customVarMap),
		                                                                         isAddDefaultLookups);

		StringSubstitutor instance = new StringSubstitutor(stringLookup);
		instance.setVariablePrefix("{$");
		instance.setVariableSuffix("}");
		// 默认值表达式
		instance.setValueDelimiter("-:");

		// 允许嵌套表达式
		instance.setEnableSubstitutionInVariables(true);
		// 自定义参数Map中的value 默认不支持表达式解析。   参考 `#isDisableSubstitutionInValues` JavaDoc描述。
		instance.setDisableSubstitutionInValues(false);

		instance.setEnableUndefinedVariableException(true);

		return instance;
	}

	/**
	 * 1. 表达式变为 `{$var}`
	 *
	 * @see StringSubstitutor#setVariablePrefix(String)
	 * @see StringSubstitutor#setVariableSuffix(String)
	 */
	@Test
	public void expressionChange(){
		String replace = _defaultSubstitutor.replace("表达式变更 `{$express}`");
		System.out.println(replace);
		Assertions.assertThat(replace).isEqualTo("表达式变更 `3+4`");

	}

	/**
	 * 2. 支持 默认值表达式 `{$var-:123}`
	 *
	 * <p><h3>备注</h3>
	 * 与SpEL区别，且不建议改成`:`表示默认值。
	 * <br/>原因：因为`StringSubstitutor`支持的 ScriptEngine 默认使用`:`作为语法解析，且无法自定义。
	 *   所以，不建议都使用`:`作为语法分割，造成有时难以理解。
	 *
	 * @see StringSubstitutor#setValueDelimiter(String)
	 * @see StringSubstitutorExtensionTests
	 */
	@Test
	public void defaultValueExpress(){
		String replace = _defaultSubstitutor.replace("表达式变更 `{$default-: 1234}`");
		System.out.println(replace);
		Assertions.assertThat(replace).isEqualTo("表达式变更 ` 1234`");

		// 期望：能自动`trim`
		Assertions.assertThat(replace).isEqualTo("表达式变更 `1234`");
	}

	/**
	 * 自定义函数表达式的写法 `{$vergilyn-urlTrace:1234}`，<b>其中不能自定义`:`解析方式</b>，参考：
	 * <pre>
	 *  - {@link StringSubstitutor#substitute(TextStringBuilder, int, int, List)}, LINE: 1433
	 *  - {@link StringSubstitutor#resolveVariable(String, TextStringBuilder, int, int)}, 例如`variableName = "vergilyn-urlTrace:1234"`
	 *  - {@link org.apache.commons.text.lookup.InterpolatorStringLookup#lookup(String)}
	 *    - {@link org.apache.commons.text.lookup.InterpolatorStringLookup#PREFIX_SEPARATOR}
	 *
	 *
	 *
	 *  - {@link org.apache.commons.text.lookup.ScriptStringLookup#lookup(String)}
	 *  - {@link org.apache.commons.text.lookup.AbstractStringLookup#SPLIT_STR}
	 * </pre>
	 *
	 * 解决方式：复制`InterpolatorStringLookup`代码，并重写。
	 */
	@Test
	public void test(){
		// 期望：`vergilyn-urlTrace`是自定义函数，而不是 自定义变量名称。
		String replace = _defaultSubstitutor.replace("表达式变更 `{$vergilyn-urlTrace:1234}`");
		System.out.println(replace);
		Assertions.assertThat(replace).isEqualTo("表达式变更 `1234?_sp=mcs.10086.sms.1234`");

		// 不能写成 `-:` 例如 `{$vergilyn-urlTrace-:1234}`
		replace = _defaultSubstitutor.replace("表达式变更 `{$vergilyn-urlTrace-:1234}`");
		System.out.println(replace);
		Assertions.assertThat(replace).isEqualTo("表达式变更 `1234?_sp=mcs.10086.sms.1234`");
	}

	/**
	 * 如果装载了默认的lookups，并且 <b>关键字与自定义变量名重名</b>，例如 `urlEncoder`:
	 *
	 * <pre>表达式例如 `{$urlEncoder:{$urlEncoder}}`
	 *    1. 先解析最内层`{$urlEncoder}`，虽然匹配到 {@link org.apache.commons.text.lookup.UrlEncoderStringLookup},
	 *    但是`value = null`，所以会用`defaultStringLookup`去解析（即`customVarMap`），
	 *    此时表达式为`{$urlEncoder:Hello World!}`
	 *
	 *    2. 接着解析，因为存在`UrlEncoderStringLookup`且表达值满足，所以会执行{@link org.apache.commons.text.lookup.UrlEncoderStringLookup#lookup(String)}
	 *    所以最后`value = encode('Hello World!')`
	 *
	 *    源码参考：{@link org.apache.commons.text.lookup.InterpolatorStringLookup#lookup(String)}
	 * </pre>
	 *
	 * <pre>表达式例如 `{$urlEncoder-:{$password}}`
	 *     同上，先解析最内层得到 `{$urlEncoder-:pass word!}`
	 *
	 * </pre>
	 *
	 */
	@Test
	public void defaultLookups(){
		Map<String, String> customVarMap = Maps.newHashMap();
		customVarMap.put("urlEncoder", "Hello World!");
		customVarMap.put("username", "vergilyn");
		customVarMap.put("password", "pass word!");
		customVarMap.put("url", "https://www.baidu.com");
		customVarMap.put("nested", "{$nx-:nested default}");

		StringSubstitutor enableDefaultLookups = buildStringSubstitutor(customVarMap, Maps.newHashMap(), true);
		enableDefaultLookups.setEnableUndefinedVariableException(false);

		// vergilyn, Hello+World%21
		// System.out.println(enableDefaultLookups.replace("{$username}, {$urlEncoder:{$urlEncoder}}"));

		// vergilyn, Hello World!
		// System.out.println(enableDefaultLookups.replace("{$username}, {$urlEncoder-:{$urlEncoder}}"));

		// vergilyn, Hello World!
		System.out.println(enableDefaultLookups.replace("{$username}, {$urlEncoder-:{$password}}"));

		System.out.println(enableDefaultLookups.replace("{$username}, {$urlEncoder:{$url}}"));
		System.out.println(enableDefaultLookups.replace("{$username}, {$urlEncoder:{$nested}}"));

	}
}
