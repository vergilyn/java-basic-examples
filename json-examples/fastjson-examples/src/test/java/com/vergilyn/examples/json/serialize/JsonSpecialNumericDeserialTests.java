package com.vergilyn.examples.json.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.util.TypeUtils;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.reflect.Type;

public class JsonSpecialNumericDeserialTests {
	/**
	 * {@linkplain IntegerCodec} & {@linkplain TypeUtils#castToInt(Object)}：fastjson 默认会处理 'null' 和 'NULL'。
	 *
	 * 返回的数据中，数字类型包含特殊的 例如`-` 或者 `None` 之类的，
	 */
	@ParameterizedTest
	@CsvSource(value = {
			"{'name': 'normal', 'prim_num': '123', 'wrap_num': '321'}\tnormal\t123\t321",
			"{'name': 'fastjson', 'prim_num': 'null', 'wrap_num': 'NULL'}\tfastjson\t0\t",
			"{'name': 'fastjson', 'prim_num': '-', 'wrap_num': '-'}\tfastjson\t1\t1",
	}, delimiter = '\t')
	public void parser(String jsonStr, String name, int primNum, Integer wrapNum){
		Stats stats = JSON.parseObject(jsonStr, Stats.class);

		Assertions.assertEquals(name, stats.name);
		Assertions.assertEquals(primNum, stats.primNum);
		Assertions.assertEquals(wrapNum, stats.wrapNum);

		System.out.println("Stats Object >>>> " + JSON.toJSONString(stats));
	}

	/**
	 * 都可以通过扩展 {@linkplain JSONField#deserializeUsing()} 实现。
	 */
	@ParameterizedTest
	@CsvSource(value = {
		"{'rate_int': '1234'}\t1234",
		"{'rate_int': '12.34%'}\t1234",
	}, delimiter = '\t')
	public void rate(String jsonStr, Integer rateInt){
		Stats stats = JSON.parseObject(jsonStr, Stats.class);

		Assertions.assertEquals(rateInt, stats.rateInt);
	}


	@Data
	public static class Stats {
		private String name;

		// @JSONField(deserializeUsing = CustomIntegerCodec.class)
		@JSONField(deserializeUsing = CustomIntegerCodec.class)
		private int primNum;

		@JSONField(deserializeUsing = CustomIntegerCodec.class)
		private Integer wrapNum;

		@JSONField(format = "##.##%")
		private Integer rateInt;
	}

	public static class CustomIntegerCodec extends IntegerCodec{
		@Override
		public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
			String val = parser.getLexer().stringVal();

			// 特殊的数字，转换成 指定的数字！
			if ("-".equalsIgnoreCase(val)){
				return (T) new Integer(1);
			}

			return super.deserialze(parser, clazz, fieldName);
		}
	}
}
