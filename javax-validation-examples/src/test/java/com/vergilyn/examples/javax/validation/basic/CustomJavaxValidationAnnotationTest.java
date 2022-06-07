package com.vergilyn.examples.javax.validation.basic;

import com.vergilyn.examples.javax.validation.bean.ParentValidationBean;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 通过自定义`javax-annotation`来自定义校验规则
 *
 * @author vergilyn
 * @since 2022-05-27
 *
 * @see com.vergilyn.examples.javax.validation.anno.CustomJavaxValidationAnno
 * @see com.vergilyn.examples.javax.validation.validator.CustomJavaxValidationAnnoValidator
 */
public class CustomJavaxValidationAnnotationTest {

	/**
	 * <h3>ValidationMessages_zh_CN.properties</h3>
	 * <p> 可以参考 hibernate-validator 中的`ValidationMessages_zh_CN.properties`
	 *
	 * <h4>error-message 语法</h4>
	 * <pre> 例如
	 *     {@linkplain Size#message()} = "{javax.validation.constraints.Size.message}"
	 *     javax.validation.constraints.Size.message = 个数必须在{min}和{max}之间
	 * </pre>
	 * <p> 1. 使用`{...}`指定 i18n 的 code，例如 {@linkplain Size#message()}
	 * <p> 2. 支持 SpEL表达式。
	 * <p> 3. 获取 注解本身的属性，语法 `{...}`。例如 {@linkplain Size#max()} `{max}`。
	 * <p> 4. 获取 被注解对象的属性，语法`${validatedValue.xx}`。例如 “${validatedValue.name} 会获取被注解对象的name字段，获取不道则替换为 null”。
	 * <p> 5. 获取 被注解对象的当前值，语法`${validatedValue}`。(感觉接近`Object.toString()`，java复杂类型还待测试效果)
	 * <p> 6. etc，更多语法遇到时再研究...
	 *
	 * @see javax.validation.MessageInterpolator
	 * @see <a href="https://www.jianshu.com/p/158bbc3d3180">java通过自定义注解进行参数校验并使用国际化错误提示</a>
	 */
	@Test
	public void test() {
		ParentValidationBean invalid = new ParentValidationBean();
		invalid.setName(this.getClass().getName());
		invalid.setNumberPositive(20);
		invalid.setSpringCustomValidator(null);
		invalid.setCustomJavaAnno(19);

		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

		Set<ConstraintViolation<ParentValidationBean>> violations = validator.validate(invalid);

		for (ConstraintViolation<ParentValidationBean> violation : violations) {
			System.out.println("error >>>>");
			System.out.println("\tclass: " + violation.getRootBeanClass().getName());
			System.out.println("\tproperty: " + violation.getPropertyPath().toString());
			System.out.println("\tinvalidValue: " + violation.getInvalidValue().toString());
			System.out.println("\terrmsg: " + violation.getMessage());
		}
	}

}
