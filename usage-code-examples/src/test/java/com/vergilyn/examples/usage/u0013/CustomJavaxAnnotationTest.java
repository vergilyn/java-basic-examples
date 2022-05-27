package com.vergilyn.examples.usage.u0013;

import com.vergilyn.examples.usage.u0013.bean.ValidationBean;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Set;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * TODO 2022-05-27，通过自定义`javax-annotation`来自定义校验规则。
 *
 * @author vergilyn
 * @since 2022-05-27
 */
public class CustomJavaxAnnotationTest {

	@Test
	public void test(){
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

	/**
	 * 注意：{@link ConstraintValidator#isValid(Object, ConstraintValidatorContext)} 返回boolen，
	 * 所以 每个javax-annotation只校验一种错误，这样才可以保证 错误提示{@link CustomJavaxAnno#message()}比较准确。
	 *
	 * <p>例如{@link CustomJavaxAnno}存在2个校验：最大值不超过多少、是否必须是偶数。最后返回的 false，并不能明确知道是那个校验失败！
	 */
	public static class CustomJavaxAnnoValidator implements ConstraintValidator<CustomJavaxAnno, Integer> {
		private CustomJavaxAnno customJavaxAnno;

		@Override
		public void initialize(CustomJavaxAnno constraintAnnotation) {
			this.customJavaxAnno = constraintAnnotation;
		}

		@Override
		public boolean isValid(Integer value, ConstraintValidatorContext context) {
			if (value == null){
				return false;
			}

			int max = this.customJavaxAnno.max();
			if (value >= max){
				return false;
			}

			if (this.customJavaxAnno.isEven()){
				return value % 2 == 0;
			}

			return false;
		}
	}


	@Target({FIELD})
	@Retention(RUNTIME)
	@Documented
	@Constraint(validatedBy = { CustomJavaxAnnoValidator.class})
	public static @interface CustomJavaxAnno{

		String message() default "{com.vergilyn.examples.usage.u0013.CustomJavaxAnnotationTest.CustomJavaxAnno.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		int max();

		boolean isEven() default false;
	}
}
