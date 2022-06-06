package com.vergilyn.examples.javax.validation.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

// @ExtendWith(SpringExtension.class)
// @ContextConfiguration(..)
@SpringJUnitConfig(classes = { AbstractSpringJavaxValidatorTests.SpringJavaxValidatorConfiguration.class})
public abstract class AbstractSpringJavaxValidatorTests {

	@Autowired
	protected ApplicationContext applicationContext;

	@Configuration
	@ComponentScan(value = "com.vergilyn.examples.javax.validation.spring",
			excludeFilters = {@Filter(type = FilterType.REGEX, pattern = "com\\.vergilyn\\.examples\\.javax\\.validation\\.spring\\.configuration\\..*") })
	static class SpringJavaxValidatorConfiguration {

	}
}
