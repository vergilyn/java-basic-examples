package com.vergilyn.examples.javax.validation;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public abstract class AbstractValidatorTests {
	protected final Validator _validator = Validation.buildDefaultValidatorFactory().getValidator();

	protected <T> void print(ConstraintViolation<T> violation){
		System.out.println("error >>>>");
		System.out.println("\tclass: " + violation.getRootBeanClass().getName());
		System.out.println("\tproperty: " + violation.getPropertyPath().toString());
		System.out.println("\tinvalidValue: " + String.valueOf(violation.getInvalidValue()));
		System.out.println("\terrmsg: " + violation.getMessage());
	}
}
