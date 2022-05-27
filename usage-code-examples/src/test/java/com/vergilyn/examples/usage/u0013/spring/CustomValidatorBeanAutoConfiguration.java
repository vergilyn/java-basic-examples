package com.vergilyn.examples.usage.u0013.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.CustomValidatorBean;

@Configuration
public class CustomValidatorBeanAutoConfiguration {

	@Bean
	public CustomValidatorBean customValidatorBean(){
		return new CustomValidatorBean();
	}
}
