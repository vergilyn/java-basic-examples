package com.vergilyn.examples.javax.validation.spring.extension.validator;

import com.vergilyn.examples.javax.validation.bean.ParentValidationBean;
import com.vergilyn.examples.javax.validation.spring.extension.core.ClassBeanValidator;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.Errors;

import java.util.Map;

@Order(Ordered.LOWEST_PRECEDENCE)
public class OtherValidationBeanCustomValidator extends ClassBeanValidator<ParentValidationBean> {

	@Override
	public Class<ParentValidationBean> supportClass() {
		return ParentValidationBean.class;
	}

	@Override
	public void validateClass(ParentValidationBean target, Errors errors) {

		Map<String, String> springCustomValidator = target.getSpringCustomValidator();

		String name = springCustomValidator.get("other");
		if (StringUtils.isBlank(name)) {
			errors.rejectValue("springCustomValidator", "map.other", "map中必须包含`other`, 且值不能为blank");
		}
	}
}