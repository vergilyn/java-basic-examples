package com.vergilyn.examples.javax.validation.validator;

import com.vergilyn.examples.javax.validation.anno.CustomJavaxValidationAnno;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 注意：{@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} 返回`boolean`，
 * 所以 每个javax-annotation只校验一种错误，这样才可以保证 错误提示{@link CustomJavaxValidationAnno#message()}比较准确。
 *
 * <p>例如{@link CustomJavaxValidationAnno}存在2个校验：最大值不超过多少、是否必须是偶数。
 * 最后返回的 false，并不能明确知道是那个校验失败！
 *
 * @author vergilyn
 * @since 2022-06-06
 */
public class CustomJavaxValidationAnnoValidator implements ConstraintValidator<CustomJavaxValidationAnno, Integer> {

	private CustomJavaxValidationAnno customJavaxAnno;

	@Override
	public void initialize(CustomJavaxValidationAnno constraintAnnotation) {
		this.customJavaxAnno = constraintAnnotation;
	}

	// javadoc：此方法必须保证线程安全！
	@Override
	public boolean isValid(Integer value, ConstraintValidatorContext context) {

		if (value == null){
			return true;
		}

		// 1. 校验：最大值限制。
		int max = this.customJavaxAnno.max();
		if (value >= max){
			return false;
		}

		// 2. 校验：是否强制偶数
		if (this.customJavaxAnno.isEven()){
			return value % 2 == 0;
		}

		return false;
	}
}