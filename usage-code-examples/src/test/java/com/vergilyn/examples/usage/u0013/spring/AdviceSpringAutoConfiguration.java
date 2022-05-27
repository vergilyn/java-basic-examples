package com.vergilyn.examples.usage.u0013.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.ValidatorFactory;

@Configuration
@Import(ValidatorFactoryAutoConfiguration.class)
public class AdviceSpringAutoConfiguration {

	@Bean
	public MethodValidationPostProcessor methodValidationPostProcessor(ValidatorFactory validatorFactory){
		MethodValidationPostProcessor methodValidationPostProcessor = new MethodValidationPostProcessor();

		// 如果不传，用的其实就是 `Validation.buildDefaultValidatorFactory()`
		methodValidationPostProcessor.setValidatorFactory(validatorFactory);

		return methodValidationPostProcessor;
	}
}
