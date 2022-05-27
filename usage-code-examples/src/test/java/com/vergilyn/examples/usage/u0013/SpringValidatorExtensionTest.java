package com.vergilyn.examples.usage.u0013;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vergilyn.examples.usage.u0013.bean.ValidationBean;
import com.vergilyn.examples.usage.u0013.service.ManualSpringService;
import com.vergilyn.examples.usage.u0013.spring.CustomValidatorBeanAutoConfiguration;
import com.vergilyn.examples.usage.u0013.spring.ValidatorFactoryAutoConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.validation.*;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author vergilyn
 * @since 2022-05-26
 *
 * @see org.springframework.validation.Validator
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SpringValidatorExtensionTest {

	private final ValidationBean invalid = new ValidationBean();

	@BeforeAll
	public void beforeAll(){
		invalid.setName(SpringValidatorExtensionTest.class.getName());
		invalid.setNumberPositive(10);

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
	 *   <br/> 可以通过 spring的{@link Order} 或 {@link Ordered} 控制调用顺序，
	 *   <br/> <b>但无法避免重复校验逻辑</b>
	 *
	 * <p>
	 * <h3>备注</h3>
	 * <p>1. 2022-05-26，个人不确定 这么写是否符合 spring的设计<br/>
	 * <pre>
	 *   因为自定义的{@link ValidationBeanCustomValidator} 和 {@link LocalValidatorFactoryBean}
	 *   都属于 {@link org.springframework.validation.Validator}。
	 *   所以 不能通过 {@link ApplicationContext#getBean(Class)} 获取 {@link Validator}
	 * </pre>
	 *
	 * <p> <b>2.虽然这么结合`spring-validator`可以达到期望的目的，但感觉反而复杂化了（其实复杂度也还好）。</b>
	 */
	@Test
	public void test(){
		ApplicationContext context = new AnnotationConfigApplicationContext(CustomValidatorBeanAutoConfiguration.class,
		                                                                    ValidationBeanCustomValidator.class,
		                                                                    ClassBeanValidatorBeanManager.class,
																			OtherValidationBeanCustomValidator.class,
		                                                                    ManualSpringService.class);


		// 按个人期望而言：其实更应该是 根据`invalid`的类型 去获取 特殊的validator。
		// 否则，`validator.supports(Class)` 抛出异常其实是不符合期望的代码逻辑！
		// ValidationBeanCustomValidator validationBeanCustomValidator = context.getBean(ValidationBeanCustomValidator.class);
		ClassBeanValidatorBeanManager classBeanValidatorBeanManager = context.getBean(ClassBeanValidatorBeanManager.class);

		BindException bindException = new BindException(invalid, invalid.getClass().getName());
		classBeanValidatorBeanManager.invokeValidator(invalid, bindException);

		System.out.println("error >>>> ");
		System.out.println("\thasErrors: " + bindException.hasErrors());
		System.out.println("\tmessage: " + bindException.getMessage());

		System.out.println("===========================================");

		for (FieldError error : bindException.getFieldErrors()) {
			System.out.println("error-field >>>> ");
			System.out.println("\tObjectName: " + error.getObjectName());
			System.out.println("\tfield: " + error.getField());
			System.out.println("\trejectedValue: " + error.getRejectedValue());
			System.out.println("\tdefaultMessage: " + error.getDefaultMessage());
		}
	}


	// XXX 2022-05-26，只是用 spring-ApplicationContext 获取 ClassBeanValidator's 简单，所以才喜欢这么写。
	public static class ClassBeanValidatorBeanManager {
		private final Map<Class<?>, List<ClassBeanValidator<?>>> classValidatorCache = new ConcurrentHashMap<>(128);

		/**
		 * {@code classBeanValidators}直接由 spring 注入，就不用通过{@linkplain ApplicationContext#getBeansOfType(Class)}获取。
		 *
		 * <p> XXX 2022-05-27 待考虑，如何让 spring扫描到 ClassBeanValidator's？
		 */
		public ClassBeanValidatorBeanManager(List<ClassBeanValidator> classBeanValidators) {

			// 相对通过`bean-name`来实现策略模式，个人更喜欢下面的这种`bean.supportClass()`写法，可以强约束。（bean-name不一定能强约束）
			for (ClassBeanValidator bean : classBeanValidators) {
				classValidatorCache.computeIfAbsent(bean.supportClass(), value -> Lists.newArrayList()).add(bean);
			}

			// 控制执行顺序
			for (Map.Entry<Class<?>, List<ClassBeanValidator<?>>> entry : classValidatorCache.entrySet()) {
				entry.getValue().sort(OrderComparator.INSTANCE);
			}
		}

		public void invokeValidator(Object target, Errors errors){
			Assert.notNull(target, "Target object must not be null");
			Assert.notNull(errors, "Errors object must not be null");

			// 支持 同一个Class 定义了 多个validator.
			List<ClassBeanValidator<?>> classBeanValidators = classValidatorCache.get(target.getClass());

			Assert.notEmpty(classBeanValidators, "classBeanValidator's object must not be empty");

			// XXX 2022-05-27 需要捕获异常吗，并将捕获的异常写入`errors`？
			for (ClassBeanValidator<?> classBeanValidator : classBeanValidators) {

				// if (validationBeanCustomValidator.supports(invalid.getClass())){
				// 	validationBeanCustomValidator.validate(invalid, bindException);
				// }

				// 因为是根据 target 获取的 validator， 所以没有必要调用`validator.support(Class)`
				ValidationUtils.invokeValidator(classBeanValidator, target, errors);
			}
		}

	}

	/**
	 * 抉择：
	 *   是否应该 `extends {@link org.springframework.validation.beanvalidation.CustomValidatorBean CustomValidatorBean}`，
	 *   而不是 `extends {@link Validator}`？
	 *
	 * <p> 感觉没有必要`extends CustomValidatorBean`，包装一个`CustomValidatorBean`的目的是“期望 还是先校验JSR”，
	 * 然后再校验针对Class的自定义校验！
	 */
	public static abstract class ClassBeanValidator<T> implements Validator {
		protected final CustomValidatorBean validator;

		public abstract Class<T> supportClass();

		/**
		 * 由spring注入，例如 {@linkplain ValidatorFactoryAutoConfiguration} 或 {@linkplain CustomValidatorBeanAutoConfiguration}。
		 *
		 * <p> 如果`construct(Validator validator)` 这么写其实有点问题，因为自身也是`Validator`，或者当存在 多个Validator 时，无法正确注入！
		 *   <br/> 同理`construct(CustomValidatorBean validator)` 存在多个时，注入也有问题！
		 */
		public ClassBeanValidator(CustomValidatorBean validator){
			this.validator = validator;
		}

		@Override
		public boolean supports(Class<?> clazz) {
			// return supportClass().isAssignableFrom(clazz);
			return supportClass() == clazz;
		}
	}

	/**
	 * - {@link org.springframework.validation.beanvalidation.CustomValidatorBean} <br/>
	 * - {@link LocalValidatorFactoryBean} <br/>
	 * - {@link OptionalValidatorFactoryBean} <br/>
	 *
	 * 这几个其实是平级的，更多的可能是使用{@linkplain LocalValidatorFactoryBean}及其子类。
	 *
	 * <p>
	 */
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public static class ValidationBeanCustomValidator extends ClassBeanValidator<ValidationBean> {

		public ValidationBeanCustomValidator(CustomValidatorBean validator) {
			super(validator);
		}

		@Override
		public Class<ValidationBean> supportClass() {
			return ValidationBean.class;
		}

		@Override
		public void validate(Object target, Errors errors) {
			ValidationBean bean = (ValidationBean) target;

			// 期望 还是先校验JSR。
			this.validator.validate(target, errors);
			// ValidationUtils.invokeValidator(this.validator, target, errors);

			// 特殊校验：期望针对某个Class 自定义校验。
			Map<String, String> springCustomValidator = bean.getSpringCustomValidator();
			if (springCustomValidator == null || springCustomValidator.isEmpty()){
				errors.rejectValue("springCustomValidator", "map","map不能为空，且必须包含`name`, 且值不能为blank");
				return;
			}

			String name = springCustomValidator.get("name");
			if (StringUtils.isBlank(name)){
				errors.rejectValue("springCustomValidator", "map.key", "map中必须包含`name`, 且值不能为blank");
			}
		}

	}

	@Order(Ordered.LOWEST_PRECEDENCE)
	public static class OtherValidationBeanCustomValidator extends ClassBeanValidator<ValidationBean> {

		public OtherValidationBeanCustomValidator(CustomValidatorBean validator) {
			super(validator);
		}

		@Override
		public Class<ValidationBean> supportClass() {
			return ValidationBean.class;
		}

		@Override
		public void validate(Object target, Errors errors) {
			ValidationBean bean = (ValidationBean) target;

			Map<String, String> springCustomValidator = bean.getSpringCustomValidator();

			String name = springCustomValidator.get("other");
			if (StringUtils.isBlank(name)){
				errors.rejectValue("springCustomValidator", "map.other", "map中必须包含`other`, 且值不能为blank");
			}
		}
	}
}
