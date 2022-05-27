package com.vergilyn.examples.json.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalTime;

public class ExtendsIgnoreSerialTests {

	/**
	 * 2022-05-18，接口方法签名不要取名叫`getXxx()`。
	 */
	@Test
	public void test(){
		WechatApiRequest request = new WechatApiRequest();
		request.setOpenId("open-id");
		request.setTime(LocalTime.now());

		System.out.println(JSON.toJSONString(request, true));
	}

	// 是否`extends Serializable` 并不影响此接口中的`getXxx()`被fastjson序列化
	public static interface ApiRequest<T> extends Serializable {

		@Transient  // 不能被继承，所以无效。
		@JSONField(serialize = false, deserialize = false)  // 可以控制到`@Override`
		Class<T> getResponseClass();

		// 不取名成`getXxx()`
		Class<T> responseClass();

		// @JSONField(serialize = false, deserialize = false)  // 可以控制到`@Override`
		String getRequestUrl();

		// 非`getXxx()`，不符合`fastjson`
		String httpMethod();
	}

	@Data
	public static class WechatApiRequest implements ApiRequest<Long>{

		private String openId;
		private LocalTime time;

		@Override
		public Class<Long> getResponseClass() {
			return Long.class;
		}

		@Override
		public Class<Long> responseClass() {
			return Long.class;
		}

		@Override
		public String getRequestUrl() {
			return "wechat-request-url";
		}

		@Override
		public String httpMethod() {
			return "POST";
		}
	}
}
