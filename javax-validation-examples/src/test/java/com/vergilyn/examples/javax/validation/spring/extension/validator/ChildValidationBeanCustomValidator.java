package com.vergilyn.examples.javax.validation.spring.extension.validator;

import com.vergilyn.examples.javax.validation.bean.ChildValidationBean;
import com.vergilyn.examples.javax.validation.spring.extension.core.ClassBeanValidator;
import org.springframework.validation.Errors;

public class ChildValidationBeanCustomValidator extends ClassBeanValidator<ChildValidationBean> {

	@Override
	protected Class<ChildValidationBean> supportClass() {
		return ChildValidationBean.class;
	}

	@Override
	protected void validateClass(ChildValidationBean target, Errors errors) {
		errors.reject("force-error", "`ChildValidationBean`强制自定义校验 error。");
	}
}
