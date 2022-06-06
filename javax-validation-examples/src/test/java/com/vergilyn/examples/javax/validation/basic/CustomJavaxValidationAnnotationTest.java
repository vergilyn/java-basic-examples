package com.vergilyn.examples.javax.validation.basic;

import com.vergilyn.examples.javax.validation.bean.ValidationBean;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 通过自定义`javax-annotation`来自定义校验规则
 *
 * @author vergilyn
 * @since 2022-05-27
 *
 * @see com.vergilyn.examples.javax.validation.anno.CustomJavaxValidationAnno
 * @see com.vergilyn.examples.javax.validation.validator.CustomJavaxValidationAnnoValidator
 */
public class CustomJavaxValidationAnnotationTest {

	@Test
	public void test() {
		ValidationBean bean = new ValidationBean();
		bean.setName(this.getClass().getName());
		bean.setNumberPositive(20);
		bean.setSpringCustomValidator(null);
		bean.setCustomJavaAnno(19);

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		Set<ConstraintViolation<ValidationBean>> violations = validator.validate(bean);

		for (ConstraintViolation<ValidationBean> violation : violations) {
			System.out.println("error >>>>");
			System.out.println("\tclass: " + violation.getRootBeanClass().getName());
			System.out.println("\tproperty: " + violation.getPropertyPath().toString());
			System.out.println("\tinvalidValue: " + violation.getInvalidValue().toString());
			System.out.println("\terrmsg: " + violation.getMessage());
		}
	}

}
