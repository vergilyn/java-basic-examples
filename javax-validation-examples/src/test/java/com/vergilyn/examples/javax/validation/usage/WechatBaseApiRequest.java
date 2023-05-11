package com.vergilyn.examples.javax.validation.usage;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Setter
@Getter
public abstract class WechatBaseApiRequest implements Serializable {

	private static final long serialVersionUID = 2168321270367853948L;

	/**
	 * 必填，<a href="https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/access-token/auth.getAccessToken.html">接口调用凭证</a>
	 */
	protected String accessToken;

	public boolean check() throws Exception {
		if (StringUtils.isBlank(this.accessToken)){
			throw new IllegalArgumentException("`accessToken`不能为空.");
		}

		return true;
	}

}
