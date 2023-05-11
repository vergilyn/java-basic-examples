package com.vergilyn.examples.format;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.lookup.DefaultStringLookup;
import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;
import org.junit.jupiter.api.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

public class StringSubstitutorScriptTests {


	public StringSubstitutor buildStringSubstitutor(Map<String, String> customVarMap){
		customVarMap = customVarMap == null ? Maps.newHashMap() : customVarMap;

		Map<String, StringLookup> stringLookupMap = Maps.newHashMap();
		stringLookupMap.put(DefaultStringLookup.SCRIPT.getKey(), DefaultStringLookup.SCRIPT.getStringLookup());

		StringLookupFactory stringLookupFactory = StringLookupFactory.INSTANCE;

		StringLookup stringLookup = stringLookupFactory.interpolatorStringLookup(stringLookupMap,
		                                                                         stringLookupFactory.mapStringLookup(customVarMap),
		                                                                         false);


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
	 * 例如存在类似模版 " ??"
	 *
	 * @see org.apache.commons.text.lookup.ScriptStringLookup
	 */
	@Test
	public void javascript(){
		String separator = "\n";

		StringBuilder templateBuilder = new StringBuilder();
		// templateBuilder.append("javascript: 3 + 4 = {$script:javascript: 3 + 4}");

		// TODO 2022-12-07  怎么将JavaScript 写成 `if({$type} == 1){ 'http://a.jpg'; } else{ 'http://b.jpg' }`
		//   原因：StringSubstitutor 会截取 `{$...}` ， 所以如果 JavaScript 中存在 `}` 会导致截取错误。
		templateBuilder.append(separator).append("javascript: {$script:javascript: if({$type} == 1){ 'http://a.jpg'; $} else 'http://b.jpg';}");

		Map<String, String> customVarMap = new HashMap<>();
		customVarMap.put("type", "2");

		StringSubstitutor substitutor = buildStringSubstitutor(customVarMap);

		String replacement = substitutor.replace(templateBuilder.toString());

		System.out.printf("\n替换模版 >>>> \n%s\n<<<<\n", templateBuilder.toString());
		System.out.printf("\n替换变量 >>>> \n%s\n<<<<\n", JSON.toJSONString(customVarMap));
		System.out.printf("\n替换结果 >>>> \n%s\n<<<<\n", replacement);
	}

	@SneakyThrows
	@Test
	public void jdkScript(){
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

		ScriptEngine javascript = scriptEngineManager.getEngineByName("javascript");

		// 不能写 "return"，  因为 script 不是 `function xx(){...}`
		// Object result1 = javascript.eval("var c=0; if(2 == 1){ c +=1; return c; }else 'false';");
		Object result1 = javascript.eval("var c=0; if(2 == 1){ c +=1; c; }else 'false';");
		System.out.println(result1);

		Object result2 = javascript.eval("if(1 == 1){ 'true'; } else{ 'false'; }");
		System.out.println(result2);

	}
}
