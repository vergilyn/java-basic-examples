package com.vergilyn.examples.script.feature;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Feature;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import com.googlecode.aviator.runtime.type.AviatorType;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class AddFunctionTests {

	@Test
	public void test(){
		AviatorEvaluatorInstance instance = AviatorEvaluator.newInstance();

		instance.setOption(Options.USE_USER_ENV_AS_TOP_ENV_DIRECTLY, false);
		instance.setOption(Options.FEATURE_SET, Feature.asSet());

		String script = "1+1";
		System.out.println(instance.execute(script));

	}

	/**
	 * <a href="https://www.yuque.com/boyan-avfmj/aviatorscript/xbdgg2">
	 *     8.3 自定义函数和调用 Java 方法</a>
	 */
	@Test
	public void addFunction(){

		AviatorEvaluatorInstance aviatorEvaluator = AviatorEvaluator.newInstance();
		aviatorEvaluator.addFunction(new AddFunction());

		// AviatorEvaluator.compile("");
		// AviatorEvaluator.addFunction(new AddFunction());

		// String script = "这是一段文本`1 + 1 = ?` ，期望转换的链接：#.convert(https://www.baidu.com)。链接：https://www.baidu.com";
		// String script = "convert('abcd')";
		String script = "convert(https://www.baidu.com)";  // `abcd` 指参数名称

		String execute = (String) aviatorEvaluator.execute(script);

		System.out.println(execute);
	}

	class AddFunction extends AbstractFunction {

		@Override
		public AviatorObject call(Map<String, Object> env, AviatorObject arg1) {
			AviatorType aviatorType = arg1.getAviatorType();
			String argValue = arg1.stringValue(env);

			return new AviatorString(argValue + ".aviator");
		}

		@Override
		public String getName() {
			return "convert";
		}
	}
}
