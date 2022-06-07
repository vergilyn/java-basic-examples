package com.vergilyn.examples.javax.validation.anno;

import com.vergilyn.examples.javax.validation.validator.CustomJavaxValidationAnnoValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 具体实现参考：{@linkplain CustomJavaxValidationAnnoValidator}
 *
 * @author vergilyn
 * @since 2022-06-07
 *
 * @see javax.validation.constraints.Max
 * @see javax.validation.constraints.Size
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { CustomJavaxValidationAnnoValidator.class})
public @interface CustomJavaxValidationAnno {

	//region javax-validation固定

	// SEE：`ValidationMessages_zh_CN.properties`
	String message() default "{com.vergilyn.examples.javax.validation.anno.CustomJavaxValidationAnno.message}";
	// String message() default "自定义validation注解，当前值：${validatedValue}，期望：最大值不超过{max}${isEven ? '，且必须为偶数' : ''}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
	//endregion

	//region 自定义注解扩展部分
	int max();

	boolean isEven() default false;
	//endregion
}