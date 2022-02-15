package com.vergilyn.examples.json.type;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;
import com.vergilyn.examples.json.AbstractFastjsonTests;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class DecideJsonTypeTests extends AbstractFastjsonTests {
	private final String jsonStr = "[{\"type\": \"json-object\"}, [{\"type\": \"json-array\"}]]";

	/**
	 * {@linkplain JSONValidator#getType()}，应该是严谨判断，所以性能相对比 只判断首字符是 `[` 或 `{`低！
	 */
	@Test
	public void decide(){
		JSONArray jsonArray = JSONArray.parseArray(jsonStr);

		// com.alibaba.fastjson.JSONObject cannot be cast to com.alibaba.fastjson.JSONArray
		// JSONArray jsonArray1 = jsonArray.getJSONArray(0);

		JSONValidator v0 = JSONValidator.from(jsonArray.getString(0));
		System.out.println(v0.getType());

		JSONValidator v1 = JSONValidator.from(jsonArray.getString(1));
		System.out.println(v1.getType());

		JSONValidator vv = JSONValidator.from(jsonArray.getJSONObject(0).getString("type"));
		System.out.println(vv.getType());
	}

	/**
	 * @see JSONArray#getJSONArray(int)
	 * @see JSONArray#getJSONObject(int)
	 */
	@Test
	public void test(){
		JSONArray jsonArray = JSONArray.parseArray(jsonStr);

		Object value = jsonArray.get(0);

		// 写法也不是很友好，但效率比先判断`type`，再调用 `getJSONObject / getJSONArray` 高！
		JSONObject valueObject = null;
		JSONArray valueArray = null;
		if (value instanceof JSONObject) {
			valueObject = (JSONObject) value;
		}else if (value instanceof Map) {
			valueObject = new JSONObject((Map) value);
		}else if (value instanceof JSONArray) {
			valueArray = (JSONArray) value;
		}else if (value instanceof List) {
			valueArray = new JSONArray((List) value);
		}

		System.out.println("valueObject >>>> " + valueObject);
		System.out.println("valueArray >>>> " + valueArray);
	}
}
