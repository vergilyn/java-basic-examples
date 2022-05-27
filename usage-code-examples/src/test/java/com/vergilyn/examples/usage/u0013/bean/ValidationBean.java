package com.vergilyn.examples.usage.u0013.bean;

import com.vergilyn.examples.usage.u0013.CustomJavaxAnnotationTest;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class ValidationBean implements Serializable {

	@NotBlank
	private String name;

	@NotNull
	@Positive
	@Max(10)
	private Integer numberPositive;

	private Map<String, String> springCustomValidator;

	@CustomJavaxAnnotationTest.CustomJavaxAnno(max = 10, isEven = true)
	private Integer customJavaAnno;

	public static ValidationBean buildInvalid(){
		ValidationBean bean = new ValidationBean();
		bean.setName("invalid-bean");
		bean.setNumberPositive(-10);

		return bean;
	}
}
