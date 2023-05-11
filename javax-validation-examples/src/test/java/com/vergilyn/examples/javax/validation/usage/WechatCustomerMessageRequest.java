package com.vergilyn.examples.javax.validation.usage;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 微信客服消息，请求参数数据结构
 *
 * <p> 1. 级联校验，需要注解 {@link Valid `@Valid`} （例如 {@link #link}），
 * <br/> 参考：<a href="https://docs.jboss.org/hibernate/validator/8.0/reference/en-US/html_single/#_cascaded_validation">Cascaded validation</a>
 *
 * @author vergilyn
 * @since 2022-10-12
 *
 * @see <a href="https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/customer-message/customerServiceMessage.send.html">
 *       微信，客服消息</a>
 */
@Setter
@Getter
@NoArgsConstructor
public class WechatCustomerMessageRequest extends WechatBaseApiRequest {

	private static final long serialVersionUID = 7195698385260578996L;

	/**
	 * 文本消息
	 */
	public static final String MSGTYPE_TEXT = "text";

	/**
	 * 图片消息
	 */
	public static final String MSGTYPE_IMAGE = "image";

	/**
	 * 图文连接
	 */
	public static final String MSGTYPE_LINK = "link";

	/**
	 * 小程序卡片
	 */
	public static final String MSGTYPE_MINIPROGRAMPAGE = "miniprogrampage";

	/**
	 * <pre>
	 *   类型：string
	 *   必填：是
	 *   说明：用户的 OpenID
	 * </pre>
	 */
	@NotBlank
	private String touser;

	/**
	 * <pre>
	 *   类型：string
	 *   必填：是
	 *   说明：消息类型
	 * </pre>
	 *
	 * @see #MSGTYPE_TEXT
	 * @see #MSGTYPE_IMAGE
	 * @see #MSGTYPE_LINK
	 * @see #MSGTYPE_MINIPROGRAMPAGE
	 */
	@NotBlank
	private String msgtype;

	/**
	 * <pre>
	 *   类型：string
	 *   必填：是
	 *   说明：文本消息，msgtype="text" 时必填
	 * </pre>
	 */
	private Text text;

	/**
	 * <pre>
	 *   类型：Object
	 *   必填：是
	 *   说明：图片消息，msgtype="image" 时必填
	 * </pre>
	 */
	private Image image;

	/**
	 * <pre>
	 *   类型：Object
	 *   必填：是
	 *   说明：图文链接，msgtype="link" 时必填
	 * </pre>
	 */
	@Valid
	private Link link;

	/**
	 * <pre>
	 *   类型：Object
	 *   必填：是
	 *   说明：小程序卡片，msgtype="miniprogrampage" 时必填
	 * </pre>
	 */
	private Miniprogrampage miniprogrampage;

	@Override
	public boolean check() throws Exception {
		super.check();

		List<String> notnull = Lists.newArrayList();

		if (StringUtils.isBlank(this.touser)){
			notnull.add("touser");
		}

		if (StringUtils.isBlank(this.msgtype)){
			notnull.add("msgtype");
		}

		if (!notnull.isEmpty()){
			throw new IllegalArgumentException(String.format("必填参数%s不能为空", ArrayUtils.toString(notnull)));
		}

		return true;
	}

	@Data
	@NoArgsConstructor
	public static class Text implements Serializable {

		private static final long serialVersionUID = -1235502263073879269L;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：文本消息内容
		 * </pre>
		 */
		private String content;
	}

	@Data
	@NoArgsConstructor
	public static class Image implements Serializable {
		private static final long serialVersionUID = 1060139825909918717L;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：发送的图片的媒体ID，通过 <a href="https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/customer-message/customerServiceMessage.uploadTempMedia.html">新增素材接口</a> 上传图片文件获得。
		 * </pre>
		 */
		@JSONField(name = "media_id")
		private String mediaId;
	}

	@Data
	@NoArgsConstructor
	public static class Link implements Serializable {

		private static final long serialVersionUID = -5274152351196534199L;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：消息标题
		 * </pre>
		 */
		@NotBlank
		private String title;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：图文链接消息
		 * </pre>
		 */
		@NotBlank
		private String description;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：图文链接消息被点击后跳转的链接
		 * </pre>
		 */
		private String url;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：图文链接消息的图片链接，支持 JPG、PNG 格式，较好的效果为大图 640 X 320，小图 80 X 80
		 * </pre>
		 */
		@JSONField(name = "thumb_url")
		private String thumbUrl;
	}

	@Data
	@NoArgsConstructor
	public static class Miniprogrampage implements Serializable{

		private static final long serialVersionUID = -4523480883130636082L;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：消息标题
		 * </pre>
		 */
		private String title;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：小程序的页面路径，跟 app.json 对齐，支持参数，比如pages/index/index?foo=bar
		 * </pre>
		 */
		private String pagepath;

		/**
		 * <pre>
		 *   类型：string
		 *   必填：是
		 *   说明：小程序消息卡片的封面， image 类型的 media_id，通过 <a href="https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/customer-message/customerServiceMessage.uploadTempMedia.html">新增素材接口</a> 上传图片文件获得，建议大小为 520*416
		 * </pre>
		 */
		@JSONField(name = "thumb_media_id")
		private String thumbMediaId;
	}
}
