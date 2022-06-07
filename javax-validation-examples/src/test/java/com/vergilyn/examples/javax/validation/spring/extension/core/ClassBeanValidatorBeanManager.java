package com.vergilyn.examples.javax.validation.spring.extension.core;

import com.google.common.collect.Lists;
import com.vergilyn.examples.javax.validation.spring.configuration.CustomValidatorBeanAutoConfiguration;
import com.vergilyn.examples.javax.validation.spring.configuration.ValidatorFactoryAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 只是通过spring注入 ClassBeanValidator's 简单，所以才喜欢这么写。（也可以完全不依赖spring，实现类似的策略模式代码）
 *
 * @author vergilyn
 * @since 2022-06-06
 */
public class ClassBeanValidatorBeanManager {
	private final Map<Class<?>, List<ClassBeanValidator<?>>> classValidatorCache = new ConcurrentHashMap<>(64);

	/**
	 * 用途：“用于先校验标准的JSR”。
	 */
	private final CustomValidatorBean customValidatorBean;

	/**
	 * <p> 1. 由spring注入{@linkplain CustomValidatorBean}，例如 {@linkplain ValidatorFactoryAutoConfiguration} 或 {@linkplain CustomValidatorBeanAutoConfiguration}。
	 * <pre>备注：
	 *     例如`construct(Validator validator)` 这么写其实有点问题，因为自身也是`Validator`，或者当存在 多个Validator 时，无法正确注入！
	 *     同理`construct(CustomValidatorBean validator)` 存在多个时，注入也有问题！
	 * </pre>
	 *
	 * <p> 2. {@code classBeanValidators}直接由 spring 注入，就不用通过{@linkplain ApplicationContext#getBeansOfType(Class)}获取。
	 *
	 * <p> XXX 2022-05-27 待考虑，如何让 spring扫描到 ClassBeanValidator's？
	 * <br/> 1. `ClassBeanValidator` 实现类增加 {@linkplain Component}；
	 * <br/> 2. 定义 scan-packages；
	 */
	public ClassBeanValidatorBeanManager(CustomValidatorBean customValidatorBean, List<ClassBeanValidator<?>> classBeanValidators) {

		this.customValidatorBean = customValidatorBean;

		buildClassValidator(classBeanValidators);
	}

	/**
	 * XXX 2022-06-06，根据开发原则，此方法不应该写在此处（或者此类不要叫 xx-manager）
	 */
	public void invokeValidator(Object target, Errors errors) {
		Assert.notNull(target, "Target object must not be null");
		Assert.notNull(errors, "Errors object must not be null");

		// 1. 期望 还是先校验标准的JSR。
		// 区别：是否调用`org.springframework.validation.Validator#supports(...)`
		// this.customValidatorBean.validate(target, errors);
		ValidationUtils.invokeValidator(this.customValidatorBean, target, errors);

		// 2. 根据具体的 target-class 自定义校验规则
		invokeClassValidator(target.getClass(), target, errors);
	}

	/**
	 * <p> 1. 优先调用 父类的自定类校验，再调用 子类的自定义类校验。
	 *
	 * @see ValidationUtils#invokeValidator(Validator, Object, Errors)
	 */
	private void invokeClassValidator(Class<?> targetClass, Object target, Errors errors){
		if (targetClass == null){
			return;
		}

		// 如果存在父类，则先调用父类的自定义校验
		Class<?> superclass = targetClass.getSuperclass();
		if (superclass != null){
			invokeClassValidator(superclass, target, errors);
		}

		List<ClassBeanValidator<?>> classBeanValidators = classValidatorCache.get(targetClass);
		if (classBeanValidators == null || classBeanValidators.isEmpty()){
			return;
		}

		// XXX 2022-05-27 需要捕获异常吗，并将捕获的异常写入`errors`？
		for (ClassBeanValidator<?> classBeanValidator : classBeanValidators) {
			// 因为是根据`target`获取的 class-validator， 所以没有必要调用`validator.support(Class)`
			classBeanValidator.validate(target, errors);

			// if (validationBeanCustomValidator.supports(invalid.getClass())){
			// 	validationBeanCustomValidator.validate(invalid, bindException);
			// }

			// 内部也会调用`validator.support(Class)`
			// ValidationUtils.invokeValidator(classBeanValidator, target, errors);
		}
	}

	private void buildClassValidator(List<ClassBeanValidator<?>> classBeanValidators) {
		// 相对通过`bean-name`来实现策略模式，个人更喜欢下面的这种`bean.supportClass()`写法，可以强约束。（bean-name不一定能强约束）
		for (ClassBeanValidator<?> bean : classBeanValidators) {
			classValidatorCache.computeIfAbsent(bean.supportClass(), value -> Lists.newArrayList()).add(bean);
		}

		// 控制执行顺序，因为spring注入时已经按order排序，所以不需要再次排序。
		// for (Map.Entry<Class<?>, List<ClassBeanValidator<?>>> entry : classValidatorCache.entrySet()) {
		// 	entry.getValue().sort(OrderComparator.INSTANCE);
		// }
	}
}