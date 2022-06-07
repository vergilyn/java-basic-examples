package com.vergilyn.examples.javax.validation.spring.extension.validator;

import com.vergilyn.examples.javax.validation.bean.ParentValidationBean;
import com.vergilyn.examples.javax.validation.spring.extension.core.ClassBeanValidator;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import java.util.Map;

/**
 * - {@link CustomValidatorBean} <br/>
 * - {@link LocalValidatorFactoryBean} <br/>
 * - {@link OptionalValidatorFactoryBean} <br/>
 * <p>
 * 这几个其实是平级的，更多的可能是使用{@linkplain LocalValidatorFactoryBean}及其子类。
 *
 * <p>
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationBeanCustomValidator extends ClassBeanValidator<ParentValidationBean> {

	@Override
	public Class<ParentValidationBean> supportClass() {
		return ParentValidationBean.class;
	}

	@Override
	public void validateClass(ParentValidationBean target, Errors errors) {
		// 特殊校验：期望针对某个Class 自定义校验。
		Map<String, String> springCustomValidator = target.getSpringCustomValidator();
		if (springCustomValidator == null || springCustomValidator.isEmpty()) {
			errors.rejectValue("springCustomValidator", "map", "map不能为空，且必须包含`name`, 且值不能为blank");
			return;
		}

		String name = springCustomValidator.get("name");
		if (StringUtils.isBlank(name)) {
			errors.rejectValue("springCustomValidator", "map.key", "map中必须包含`name`, 且值不能为blank");
		}
	}

}
