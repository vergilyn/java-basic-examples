package com.vergilyn.examples.javax.validation.spring.extension;

import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

/**
 * 抉择：
 *   是否应该 `extends {@link CustomValidatorBean CustomValidatorBean}`，
 *   而不是 `extends {@link Validator}`？
 *
 * <p> 感觉没有必要`extends CustomValidatorBean`，包装一个`CustomValidatorBean`的目的是“期望 还是先校验JSR”，
 * 然后再校验针对Class的自定义校验！
 * <p> <b>特别</b> 由于{@linkplain ClassBeanValidatorBeanManager} 期望注入 1个`CustomValidatorBean`，所以此处<b>更不应该`extends CustomValidatorBean`</b>
 *
 * @author vergilyn
 * @since 2022-06-06
 */
public abstract class ClassBeanValidator<T> implements Validator {

	public abstract Class<T> supportClass();

	public abstract void validateClass(T target, Errors errors);

	@Override
	public final void validate(Object target, Errors errors) {
		Assert.notNull(target, "Target object must not be null");
		Assert.notNull(errors, "Errors object must not be null");

		// 如果是通过`ValidationUtils.invokeValidator(...)`调用，其内部已经调用过`supports(...)`
			/* boolean supports = supports(target.getClass());
			if (!supports){
				return;
			} */

		validateClass((T) target, errors);
	}

	@Override
	public final boolean supports(Class<?> clazz) {
		// return supportClass().isAssignableFrom(clazz);

		// 2022-06-06，只是`supportClass()`这么写比较简单易懂。也可以通过反射获取Class中的`真实class`。
		// ResolvableType type = ResolvableType.forInstance(this);
		// Class<?> supportClass = type.getSuperType().getGeneric(0).resolve();

		return supportClass() == clazz;
	}
}
