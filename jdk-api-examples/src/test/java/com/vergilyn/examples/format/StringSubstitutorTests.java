package com.vergilyn.examples.format;

import com.google.common.collect.Maps;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;


/**
 * <p><h3>默认的 StringLookup</h3>
 * 参考JavaDoc：{@link StringSubstitutor}、{@link StringLookupFactory} <br/>
 * 默认Lookup装载参考：{@link org.apache.commons.text.lookup.InterpolatorStringLookup#InterpolatorStringLookup(Map, StringLookup, boolean)}
 *
 * <pre>
 *   +-------------------+--------------------------------------------------------------------------------------------+
 *   | key               | class                                                                                      |
 *   +===================+============================================================================================+
 *   | base64            | {@link org.apache.commons.text.lookup.FunctionStringLookup}                                |
 *   | base64decoder     | {@link org.apache.commons.text.lookup.FunctionStringLookup}                                |
 *   | base64encoder     | {@link org.apache.commons.text.lookup.FunctionStringLookup}                                |
 *   | const             | {@link org.apache.commons.text.lookup.ConstantStringLookup}                                |
 *   | date              | {@link org.apache.commons.text.lookup.DateStringLookup}                                    |
 *   | dns               | {@link org.apache.commons.text.lookup.DnsStringLookup}                                     |
 *   | env               | {@link org.apache.commons.text.lookup.FunctionStringLookup}                                |
 *   | file              | {@link org.apache.commons.text.lookup.FileStringLookup}                                    |
 *   | java              | {@link org.apache.commons.text.lookup.JavaPlatformStringLookup}                            |
 *   | localhost         | {@link org.apache.commons.text.lookup.LocalHostStringLookup}                               |
 *   | properties        | {@link org.apache.commons.text.lookup.PropertiesStringLookup}                              |
 *   | resourcebundle    | {@link org.apache.commons.text.lookup.ResourceBundleStringLookup}                          |
 *   | script            | {@link org.apache.commons.text.lookup.ScriptStringLookup}                                  |
 *   | sys               | {@link org.apache.commons.text.lookup.FunctionStringLookup}                                |
 *   | url               | {@link org.apache.commons.text.lookup.UrlStringLookup}                                     |
 *   | urldecoder        | {@link org.apache.commons.text.lookup.UrlDecoderStringLookup}                              |
 *   | urlencoder        | {@link org.apache.commons.text.lookup.UrlEncoderStringLookup}                              |
 *   | xml               | {@link org.apache.commons.text.lookup.XmlStringLookup}                                     |
 *   +-------------------+--------------------------------------------------------------------------------------------+
 * </pre>
 *
 * @author vergilyn
 * @since 2022-09-05
 */
@SuppressWarnings("JavadocReference")
public class StringSubstitutorTests {

	/**
	 * @see StringLookupFactory#interpolatorStringLookup()
	 * @see org.apache.commons.text.lookup.ScriptStringLookup;#lookup(String)
	 * @see org.apache.commons.text.lookup.FunctionStringLookup
	 */
	@Test
	public void demo1(){
		// `sys` 和 `script` 模式都是关键字
		String pattern = "OS name: ${sys:os.name}, " + "3 + 4 = ${script:javascript:3 + 4}";

		StringSubstitutor interpolator = StringSubstitutor.createInterpolator();

		String result = interpolator.replace(pattern);

		// StringSubstitutor >>>> OS name: Windows 10, 3 + 4 = 7
		System.out.println("StringSubstitutor >>>> " + result);

		Assertions.assertThat(result)
				.isEqualTo("OS name: Windows 10, 3 + 4 = 7");
	}

	/**
	 * 1. 默认情况，不能同时支持 "自定义变量替换" 和 "脚本引擎替换"。
	 *
	 * @see
	 */
	@Test
	public void demo2(){
		//
		//    正确的写法是 `${name:-vergilyn}`
		/**
		 * 1. `${name:vergilyn}`并不是指`name`的默认值是`vergilyn`
		 *   正确的写法是 `${name:-vergilyn}`  参考javadoc
		 *     - {@linkplain StringSubstitutor}
		 *     - {@linkplain StringSubstitutor#valueDelimiterMatcher}
		 *     - {@linkplain StringSubstitutor#DEFAULT_VAR_DEFAULT} 默认值分隔符是 `:-`
		 *     - {@linkplain StringSubstitutor#DEFAULT_VALUE_DELIMITER}
		 */
		String pattern = "param: ${name:-vergilyn}, OS name: ${sys:os.name}, 3 + 4 = ${script:javascript:3 + 4}";

		Map<String, Object> params = Maps.newHashMap();
		params.put("name", "vergilyn");

		// 2. 如果传递`valueMap`，并没有解析`${sys}` 和 `${script}`
		String result = StringSubstitutor.replace(pattern, params);

		Assertions.assertThat(result)
				.isEqualTo("param: vergilyn, OS name: ${sys:os.name}, 3 + 4 = ${script:javascript:3 + 4}");
	}

	/**
	 * 参考JavaDoc: {@link StringSubstitutor}。默认支持的全部 script-engine。
	 */
	@Test
	public void demo3(){
		final StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
		interpolator.setEnableSubstitutionInVariables(true); // Allows for nested $'s.
		final String text = interpolator.replace("Base64 Decoder:        ${base64Decoder:SGVsbG9Xb3JsZCE=}\n"
				                                         + "Base64 Encoder:        ${base64Encoder:HelloWorld!}\n"
				                                         + "Java Constant:         ${const:java.awt.event.KeyEvent.VK_ESCAPE}\n"
				                                         + "Date:                  ${date:yyyy-MM-dd}\n" + "DNS:                   ${dns:address|apache.org}\n"
				                                         + "Environment Variable:  ${env:USERNAME}\n"
				                                         + "File Content:          ${file:UTF-8:src/test/resources/document.properties}\n"
				                                         + "Java:                  ${java:version}\n" + "Localhost:             ${localhost:canonical-name}\n"
				                                         + "Properties File:       ${properties:src/test/resources/document.properties::mykey}\n"
				                                         + "Resource Bundle:       ${resourceBundle:org.example.testResourceBundleLookup:mykey}\n"
				                                         + "Script:                ${script:javascript:3 + 4}\n" + "System Property:       ${sys:user.dir}\n"
				                                         + "URL Decoder:           ${urlDecoder:Hello%20World%21}\n"
				                                         + "URL Encoder:           ${urlEncoder:Hello World!}\n"
				                                         + "URL Content (HTTP):    ${url:UTF-8:http://www.apache.org}\n"
				                                         + "URL Content (HTTPS):   ${url:UTF-8:https://www.apache.org}\n"
				                                         + "URL Content (File):    ${url:UTF-8:file:///${sys:user.dir}/src/test/resources/document.properties}\n"
				                                         // + "XML XPath:             ${xml:src/test/resources/document.xml:/root/path/to/node}\n"
									);

		System.out.println(text);
	}

	/**
	 * 同时支持 "自定义变量替换" 和 "模版引擎解析"。
	 *
	 * <p><h3>源码参考</h3>
	 * <p> 其核心就是 {@linkplain  org.apache.commons.text.lookup.InterpolatorStringLookup#lookup(String) InterpolatorStringLookup#lookup(String)}
	 * <pre>
	 *     1. 优先匹配`ScriptEngine`，如果找到且解析后 `value != null`，则返回 value。
	 *     2. 否则，使用`defaultStringLookup -> 即自定义变量varMap` 进行解析返回。
	 * </pre>
	 */
	@Test
	public void bothVarMapAndScriptEngine(){
		Map<String, String> varMap = Maps.newHashMap();
		varMap.put("var-express", "3 + 4");

		StringLookup defaultStringLookup = StringLookupFactory.INSTANCE.mapStringLookup(varMap);

		// arg-1：即可以传入自定义的 StringLookup
		// arg-2: 即 defaultStringLookup。
		// arg-3: true，装载默认的 ScriptEngine's。 具体参考JavaDoc: `StringLookupFactory`
		StringLookup interpolatorStringLookup = StringLookupFactory.INSTANCE.interpolatorStringLookup(
				Maps.newHashMap(), defaultStringLookup, true
		);

		StringSubstitutor substitutor = new StringSubstitutor(interpolatorStringLookup);
		substitutor.setEnableSubstitutionInVariables(true);
		substitutor.setDisableSubstitutionInValues(false);
		substitutor.setEnableUndefinedVariableException(true);


		String pattern = "OS name: ${sys:os.name}, " + "3 + 4 = ${script:javascript:${var-express}}";
		String replace = substitutor.replace(pattern);

		Assertions.assertThat(replace)
				.isEqualTo("OS name: Windows 10, 3 + 4 = 7");
	}

}
