package com.vergilyn.examples.javax.validation.usage;

import com.vergilyn.examples.javax.validation.AbstractValidatorTests;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * <b>期望：</b>
 * <pre>
 *     参考接口文档：<a href="https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/customer-message/customerServiceMessage.send.html">微信，客服消息</a>
 *
 *     msgtype="text" 时 {@link WechatCustomerMessageRequest#text}必填，
 *     msgtype="image" 时 {@link WechatCustomerMessageRequest#image}必填，
 *     msgtype="link" 时 {@link WechatCustomerMessageRequest#link}必填，
 *     msgtype="miniprogrampage" 时 {@link WechatCustomerMessageRequest#miniprogrampage}必填，
 * </pre>
 *
 * <ol>
 *   <li>
 *       <a href="https://stackoverflow.com/questions/1972933/cross-field-validation-with-hibernate-validator-jsr-303">
 *         Cross field validation with Hibernate Validator (JSR 303)</a>
 *   </li>
 *   <li>
 *       <a href="https://stackoverflow.com/questions/1700295/java-bean-validation-jsr303-constraints-involving-relationship-between-several">
 *         Java Bean Validation (JSR303) constraints involving relationship between several bean properties</a>
 *   </li>
 *   <li>
 *       <a href="https://stackoverflow.com/questions/16317207/spring-validation-between-two-date-fields">
 *         Spring Validation Between Two Date Fields?</a>
 *   </li>
 * </ol>
 *
 * <p> <h3>备注</h3>
 * <p> 1.
 *
 * @author vergilyn
 * @since 2022-10-12
 */
public class CrossFieldValidatorByMethodTests extends AbstractValidatorTests {

	@BeforeEach
	public void beforeEach(){

	}

	/**
	 * {@linkplain Text Text} <b>内部不使用 validator</b>，并且通过 method-validator 进行编码校验。
	 *
	 * <p> 更<b>理论(theoretical)</b> 上说，其实`method-validator`不是一个规范的写法（例如 错误信息如何支持i18n？{@link Length} 之类注解要自己编码校验等。）。
	 * 并且导致混淆 一部分校验是通过 validator，一部分又是 通过硬编码。
	 *  <br/>对<b>实用性</b>而言，如果是复杂的校验，method-validator 蛮好用的。
	 *
	 * <p> FIXME 2022-10-12 method-validator校验的错误信息怎么反馈？ 
	 */
	@Test
	public void testText(){
		WechatRequest request = new WechatRequest(){
			@AssertTrue(message = "msgtype=\"text\"时, `Text`必填")
			private boolean isText(){
				if (!MSGTYPE_TEXT.equalsIgnoreCase(this.getMsgtype())){
					return true;
				}

				// `Text` 内部简单，不用 Validator校验
				return this.getText() != null && StringUtils.isNotBlank(this.getText().getContent());
			}
		};

		request.setMsgtype(WechatRequest.MSGTYPE_TEXT);

		Set<ConstraintViolation<WechatRequest>> violations = _validator.validate(request);

		for (ConstraintViolation<WechatRequest> violation : violations) {
			print(violation);
		}
	}

	/**
	 * 存在`link`，`link`内部validator校验的错误信息怎么反馈？
	 */
	@Test
	public void testLink(){
		WechatRequest request = new WechatRequest(){
			// @AssertTrue(message = "msgtype=\"link\"时, `Link`必填")
			private boolean isLink(){
				if (!MSGTYPE_LINK.equalsIgnoreCase(this.getMsgtype())){
					return true;
				}

				Link link = this.getLink();
				if (link == null){
					return false;
				}
				// `Link` 内部也会用Validator校验, FIXME 2022-10-12 检验错误信息如何反馈到最终的提示信息中？
				//   如果`link`内部使用硬编码校验，失去了 validator 的意义！
				Set<ConstraintViolation<Link>> validate = _validator.validate(link);
				return validate == null || validate.isEmpty();
			}
		};
		request.setMsgtype(WechatRequest.MSGTYPE_LINK);

		request.setLink(new Link());

		Set<ConstraintViolation<WechatRequest>> violations = _validator.validate(request);

		for (ConstraintViolation<WechatRequest> violation : violations) {
			print(violation);
		}
	}

	@Data
	@NoArgsConstructor
	static class WechatRequest {
		public static final String MSGTYPE_TEXT = "text";
		public static final String MSGTYPE_LINK = "link";

		@NotBlank
		private String msgtype;

		private Text text;

		@Valid // 级联校验
		private Link link;
	}

	@Data
	@NoArgsConstructor
	static class Text {

		@NotBlank
		@Length(max = 200)
		private String content;
	}

	@Data
	@NoArgsConstructor
	static class Link {
		@NotBlank
		private String label;

		@NotBlank
		private String url;

		private String target;
	}
}
