package com.vergilyn.examples.javax.validation.anno;

import com.vergilyn.examples.javax.validation.validator.CustomJavaxValidationAnnoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { CustomJavaxValidationAnnoValidator.class})
public @interface CustomJavaxValidationAnno {

	String message() default "{com.vergilyn.examples.javax.validation.anno.CustomJavaxValidationAnno.TODO-message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	int max();

	boolean isEven() default false;
}