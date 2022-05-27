package com.vergilyn.examples.usage.u0013.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorFactoryAutoConfiguration {

	/**
	 * {@linkplain LocalValidatorFactoryBean} 同时是：
	 * <pre>
	 *  - {@linkplain javax.validation.ValidatorFactory}
	 *  - {@linkplain org.springframework.validation.Validator}
	 * </pre>
	 *
	 * <p>{@linkplain CustomValidatorBean} <b>只是</b> {@linkplain org.springframework.validation.Validator}
	 *
	 * @see org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean
	 */
	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean(){
		return new LocalValidatorFactoryBean();
	}
}
