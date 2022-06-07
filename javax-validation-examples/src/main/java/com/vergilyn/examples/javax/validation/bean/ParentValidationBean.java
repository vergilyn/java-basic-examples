package com.vergilyn.examples.javax.validation.bean;

import com.vergilyn.examples.javax.validation.anno.CustomJavaxValidationAnno;
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
public class ParentValidationBean implements Serializable {

	@NotBlank
	private String name;

	@NotNull
	@Positive
	@Max(10)
	private Integer numberPositive;

	private Map<String, String> springCustomValidator;

	@CustomJavaxValidationAnno(max = 10, isEven = true)
	private Integer customJavaAnno;

	public static ParentValidationBean buildInvalid(){
		ParentValidationBean bean = new ParentValidationBean();
		bean.setName("parent-invalid-bean");
		bean.setNumberPositive(-10);

		return bean;
	}
}
