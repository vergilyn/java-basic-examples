package com.vergilyn.examples.javax.validation.basic;

import com.vergilyn.examples.javax.validation.bean.ValidationBean;
import org.apache.dubbo.validation.support.jvalidation.JValidator;
import org.junit.jupiter.api.Test;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 实际应用参考：
 * <pre>
 *   - dubbo    {@linkplain JValidator}
 *   - spring   {@linkplain Validated}
 * </pre>
 *
 * @author vergilyn
 * @since 2022-05-25
 */
public class JavaxValidationBasicTest {

	private final ValidationBean invalid = ValidationBean.buildInvalid();

	/**
	 * maven 依赖：
	 * <pre>
	 *    - org.hibernate.validator: hibernate-validator
	 *      - javax.validation: validation-api
	 *    - javax.el: javax.el-api
	 *    - org.glassfish: javax.el
	 * </pre>
	 *
	 */
	@Test
	public void standard(){
		// `Validation.buildDefaultValidatorFactory()`
		// javax.validation.NoProviderFoundException: Unable to create a Configuration,
		// because no Jakarta Bean Validation provider could be found.
		// Add a provider like Hibernate Validator (RI) to your classpath.
		// 1. 因为需要 validator-provider，所以需要依赖`hibernate-validator`
		// 2. hibernate-validator 中需要用到 ELManager，所以需要依赖`javax.el`
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		// validator = Validation.byProvider(HibernateValidator.class)
		// 		.configure()
		// 		.failFast(true)
		// 		.buildValidatorFactory()
		// 		.getValidator();

		Set<ConstraintViolation<ValidationBean>> violations = validator.validate(invalid);

		/* 例如 dubbo-validation 最后直接是
		 * {@code
		 *  if (!violations.isEmpty()) {
		 *     logger.error("Failed to validate service: " + clazz.getName() + ", method: " + methodName + ", cause: " + violations);
		 *     throw new ConstraintViolationException("Failed to validate service: " + clazz.getName() + ", method: " + methodName + ", cause: " + violations, violations);
		 *  }
		 * }
		 */
		for (ConstraintViolation<ValidationBean> violation : violations) {
			System.out.println("error >>>>");
			System.out.println("\tclass: " + violation.getRootBeanClass().getName());
			System.out.println("\tproperty: " + violation.getPropertyPath().toString());
			System.out.println("\tinvalidValue: " + violation.getInvalidValue().toString());
			System.out.println("\terrmsg: " + violation.getMessage());
		}
	}





}
