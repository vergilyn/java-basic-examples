package com.vergilyn.examples.json.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalTime;

import static com.alibaba.fastjson.serializer.SerializerFeature.IgnoreNonFieldGetter;
import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;

public class ExtendsIgnoreSerialTests {

	/**
	 * <p> 1. 2022-05-18，接口方法签名不要取名叫`getXxx() 或 isXxx()`。
	 *
	 * <p> 2. {@link com.alibaba.fastjson.serializer.SerializerFeature#IgnoreNonFieldGetter}
	 */
	@Test
	public void test(){
		WechatApiRequest request = new WechatApiRequest();
		request.setOpenId("open-id");
		request.setTime(LocalTime.now());
		System.out.println("WechatApiRequest >>>> " + JSON.toJSONString(request, PrettyFormat));
		System.out.println("WechatApiRequest IgnoreNonFieldGetter >>>> " + JSON.toJSONString(request, PrettyFormat, IgnoreNonFieldGetter));

		// `@JSONField(serialize = false, deserialize = false)` 无法控制到"孙子辈"
		WechatExtensionApiRequest extensionApiRequest = new WechatExtensionApiRequest();
		System.out.println("WechatExtensionApiRequest >>>> " + JSON.toJSONString(extensionApiRequest, PrettyFormat));
		System.out.println("WechatExtensionApiRequest IgnoreNonFieldGetter >>>> " + JSON.toJSONString(extensionApiRequest, PrettyFormat, IgnoreNonFieldGetter));
	}

	// 是否`extends Serializable` 并不影响此接口中的`getXxx()`被fastjson序列化
	public static interface ApiRequest<T> extends Serializable {

		/**
		 * @see com.alibaba.fastjson.serializer.SerializerFeature#SkipTransientField
		 */
		@Transient  // 不能被继承，所以无效。
		Class<T> getResponseClass();

		// 不取名成`getXxx()`
		Class<T> responseClass();

		// FIXME 2022-06-17，可以控制到`子类`，但无法控制到`孙子辈`
		@JSONField(serialize = false, deserialize = false)
		String getJsonFieldSerializeFalse();

		// 非`getXxx()`，不符合`fastjson`
		String httpMethod();

		// `isXxx()`，会被fastjson 序列化和反序列化。
		boolean isSuccess();
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
		public String getJsonFieldSerializeFalse() {
			return "wechat-request-url";
		}

		@Override
		public String httpMethod() {
			return "POST";
		}

		@Override
		public boolean isSuccess() {
			return true;
		}
	}

	@Data
	public static class WechatExtensionApiRequest extends WechatApiRequest {
		@Override
		public String getJsonFieldSerializeFalse() {
			return "wechat-extension-request-url";
		}
	}
}
