package com.vergilyn.examples.javax.validation.spring.extension;

import com.google.common.collect.Maps;
import com.vergilyn.examples.javax.validation.bean.ChildValidationBean;
import com.vergilyn.examples.javax.validation.bean.ParentValidationBean;
import com.vergilyn.examples.javax.validation.spring.AbstractSpringJavaxValidatorTests;
import com.vergilyn.examples.javax.validation.spring.configuration.CustomValidatorBeanAutoConfiguration;
import com.vergilyn.examples.javax.validation.spring.configuration.ValidatorFactoryAutoConfiguration;
import com.vergilyn.examples.javax.validation.spring.extension.core.ClassBeanValidator;
import com.vergilyn.examples.javax.validation.spring.extension.core.ClassBeanValidatorBeanManager;
import com.vergilyn.examples.javax.validation.spring.extension.validator.ChildValidationBeanCustomValidator;
import com.vergilyn.examples.javax.validation.spring.extension.validator.OtherValidationBeanCustomValidator;
import com.vergilyn.examples.javax.validation.spring.extension.validator.ValidationBeanCustomValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Map;

/**
 * <p> 1. 可以不用{@linkplain CustomValidatorBean}，直接用{@linkplain LocalValidatorFactoryBean} 也可以达到目的。
 *
 * @author vergilyn
 * @see Validator
 * @since 2022-05-26
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = { ValidatorFactoryAutoConfiguration.class, CustomValidatorBeanAutoConfiguration.class })
@Import({ ClassBeanValidatorBeanManager.class,
		ValidationBeanCustomValidator.class,
		OtherValidationBeanCustomValidator.class,
		ChildValidationBeanCustomValidator.class
})
public class SpringValidatorExtensionTest extends AbstractSpringJavaxValidatorTests {

	@Autowired
	private ClassBeanValidatorBeanManager classBeanValidatorBeanManager;

	private final ParentValidationBean invalid = new ChildValidationBean();

	@BeforeAll
	public void beforeAll() {
		invalid.setName(SpringValidatorExtensionTest.class.getName());
		invalid.setNumberPositive(20);

		Map<String, String> map = Maps.newHashMap();
		map.put("name", "");
		invalid.setSpringCustomValidator(map);
	}

	/**
	 * <h3>期望</h3>
	 * <pre>
	 *   1. 默认的 JSR 校验正常执行。
	 *   2. 针对具体的某个Class，指定 这个Class 的校验规则。
	 * </pre>
	 *
	 *
	 * <p><h3>待考虑</h3>
	 * <p>1. 如何更友好的让 spring扫描到 {@linkplain ClassBeanValidator}？
	 *
	 * <p>2. 如果 同一个Class 的 多个{@linkplain ClassBeanValidator} 中存在重复的校验，怎么友好处理？
	 * <br/> 可以通过 spring的{@link Order} 或 {@link Ordered} 控制调用顺序，
	 * <br/> <b>但无法避免重复校验逻辑</b>
	 *
	 * <p>
	 * <h3>备注</h3>
	 * <p>1. 2022-05-26，个人不确定 这么写是否符合 spring的设计<br/>
	 * <pre>
	 *   因为自定义的{@link ValidationBeanCustomValidator} 和 {@link LocalValidatorFactoryBean}
	 *   都属于 {@link Validator}。
	 *   所以 不能通过 {@link ApplicationContext#getBean(Class)} 获取 {@link Validator}
	 * </pre>
	 *
	 * <p> <b>2.虽然这么结合`spring-validator`可以达到期望的目的，但感觉反而复杂化了（其实复杂度也还好）。</b>
	 */
	@Test
	public void test() {

		// 按个人期望而言：其实更应该是 根据`invalid`的类型 去获取 特殊的validator。
		// 否则，`validator.supports(Class)` 抛出异常其实是不符合期望的代码逻辑！
		// ValidationBeanCustomValidator validationBeanCustomValidator = context.getBean(ValidationBeanCustomValidator.class);
		// ClassBeanValidatorBeanManager classBeanValidatorBeanManager = applicationContext.getBean(ClassBeanValidatorBeanManager.class);

		BindException bindException = new BindException(invalid, invalid.getClass().getName());
		classBeanValidatorBeanManager.invokeValidator(invalid, bindException);

		System.out.println("error >>>> ");
		System.out.println("\thasErrors: " + bindException.hasErrors());
		System.out.println("\tmessage: " + bindException.getMessage());

		System.out.println("===========================================");

		// List<FieldError> fieldErrors = bindException.getFieldErrors();
		for (ObjectError error : bindException.getAllErrors()) {
			System.out.println("error >>>> ");
			System.out.println("\tObjectName: " + error.getObjectName());
			// System.out.println("\tfield: " + error.getField());
			// System.out.println("\trejectedValue: " + error.getRejectedValue());
			System.out.println("\tdefaultMessage: " + error.getDefaultMessage());
		}
	}

}
