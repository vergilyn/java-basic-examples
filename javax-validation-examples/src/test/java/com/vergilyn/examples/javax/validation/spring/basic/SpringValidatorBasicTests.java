package com.vergilyn.examples.javax.validation.spring.basic;

import com.vergilyn.examples.javax.validation.bean.ParentValidationBean;
import com.vergilyn.examples.javax.validation.spring.AbstractSpringJavaxValidatorTests;
import com.vergilyn.examples.javax.validation.spring.AdviceSpringService;
import com.vergilyn.examples.javax.validation.spring.ManualSpringService;
import com.vergilyn.examples.javax.validation.spring.configuration.AdviceSpringAutoConfiguration;
import com.vergilyn.examples.javax.validation.spring.configuration.ValidatorFactoryAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@ContextConfiguration(classes = ValidatorFactoryAutoConfiguration.class)
public class SpringValidatorBasicTests extends AbstractSpringJavaxValidatorTests {
	@Autowired
	private ManualSpringService manualSpringService;
	@Autowired
	private org.springframework.validation.Validator validator;

	private ParentValidationBean invalid = ParentValidationBean.buildInvalid();


	/**
	 * @see Validated
	 * @see org.springframework.validation.Validator
	 * @see org.springframework.validation.ValidationUtils
	 */
	@Test
	public void manual() {

		// 不需要额外的注解
		// - javax.validation.@Valid`
		// - org.springframework.validation.annotation.@Validated
		// - MethodValidationPostProcessor
		// ValidatorFactory validatorFactory = context.getBean(ValidatorFactory.class);
		// javax.validation.Validator validator = validatorFactory.getValidator();

		//  org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
		// org.springframework.validation.Validator validator = context.getBean(
		// 		org.springframework.validation.Validator.class);
		System.out.println("[vergilyn]validator >>>> " + validator.getClass().getName());

		BindException bindException = new BindException(invalid, invalid.getClass().getName());
		validator.validate(invalid, bindException);

		System.out.println("[vergilyn-javax-validation]error >>>> ");
		System.out.printf("\thasErrors: %b \n", bindException.hasErrors());
		System.out.printf("\tmessage: %s \n", bindException.getMessage());

		// 多一个换行
		System.out.println();
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
	 * @see org.springframework.validation.Validator
	 */
	@Test
	public void springAdvice() {
		ApplicationContext context = new AnnotationConfigApplicationContext(AdviceSpringAutoConfiguration.class,
		                                                                    AdviceSpringService.class);

		AdviceSpringService adviceSpringService = context.getBean(AdviceSpringService.class);

		// javax.validation.ConstraintViolationException: get.info2.numberPositive: 必须是正数
		adviceSpringService.get(invalid, invalid);
	}
}
