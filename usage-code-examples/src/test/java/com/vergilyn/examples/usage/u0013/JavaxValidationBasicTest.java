package com.vergilyn.examples.usage.u0013;

import com.vergilyn.examples.usage.u0013.bean.ValidationBean;
import com.vergilyn.examples.usage.u0013.service.AdviceSpringService;
import com.vergilyn.examples.usage.u0013.service.DubboInterface;
import com.vergilyn.examples.usage.u0013.service.ManualSpringService;
import com.vergilyn.examples.usage.u0013.spring.AdviceSpringAutoConfiguration;
import com.vergilyn.examples.usage.u0013.spring.ValidatorFactoryAutoConfiguration;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.validation.support.jvalidation.JValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 实际应用参考：
 * <pre>
 *   - dubbo    {@linkplain org.apache.dubbo.validation.support.jvalidation.JValidator}
 *   - spring   {@linkplain org.springframework.validation.annotation.Validated}
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

	/**
	 * <p> 1. 每次都会调用 {@linkplain  Validation#buildDefaultValidatorFactory()}
	 *
	 * <p> 2. dubbo-validation  其实存在 map-cache，不会每次都去生成 dubbo-validator
	 * <pre>
	 *   - {@link org.apache.dubbo.validation.Validation}
	 *   - {@link org.apache.dubbo.validation.support.AbstractValidation#getValidator(URL)}
	 *   - {@link org.apache.dubbo.validation.Validator}
	 * </pre>
	 *
	 * <p> 3. 最终使用逻辑，{@link org.apache.dubbo.validation.filter.ValidationFilter}
	 */
	@Test
	public void dubboValidator(){
		URL url = Mockito.mock(URL.class);

		Mockito.when(url.getServiceInterface()).then(invocationOnMock -> DubboInterface.class.getName());

		JValidator jValidator = new JValidator(url);

		try {
			jValidator.validate("get", new Class[]{ValidationBean.class}, new Object[]{invalid});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @see org.springframework.validation.annotation.Validated
	 * @see org.springframework.validation.Validator
	 * @see org.springframework.validation.ValidationUtils
	 */
	@Test
	public void springManual(){
		ApplicationContext context = new AnnotationConfigApplicationContext(ValidatorFactoryAutoConfiguration.class, ManualSpringService.class);

		// 不需要额外的注解
		// - javax.validation.@Valid`
		// - org.springframework.validation.annotation.@Validated
		// - MethodValidationPostProcessor
		// ValidatorFactory validatorFactory = context.getBean(ValidatorFactory.class);
		// javax.validation.Validator validator = validatorFactory.getValidator();

		//  org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
		org.springframework.validation.Validator validator = context.getBean(org.springframework.validation.Validator.class);
		System.out.println("[vergilyn]validator >>>> " + validator.getClass().getName());

		BindException bindException = new BindException(invalid, invalid.getClass().getName());
		validator.validate(invalid, bindException);

		System.out.println("error >>>> ");
		System.out.println("\thasErrors: " + bindException.hasErrors());
		System.out.println("\tmessage: " + bindException.getMessage());
	}

	/**
	 * <p> 1. spring-bean 的class 需要被 {@linkplain Validated} 修饰；<br/>
	 * <pre>参考：
	 *   - {@linkplain org.springframework.validation.beanvalidation.MethodValidationPostProcessor#validatedAnnotationType}
	 *   - {@linkplain org.springframework.validation.beanvalidation.MethodValidationPostProcessor#afterPropertiesSet()}
	 * </pre>
	 *
	 * <p> 2. class中的 method参数 需要被 {@linkplain Valid} 修饰。
	 *
	 *
	 * @see org.springframework.validation.Validator
	 */
	@Test
	public void springAdvice(){
		ApplicationContext context = new AnnotationConfigApplicationContext(AdviceSpringAutoConfiguration.class, AdviceSpringService.class);

		AdviceSpringService adviceSpringService = context.getBean(AdviceSpringService.class);

		// javax.validation.ConstraintViolationException: get.info2.numberPositive: 必须是正数
		adviceSpringService.get(invalid, invalid);
	}

}
