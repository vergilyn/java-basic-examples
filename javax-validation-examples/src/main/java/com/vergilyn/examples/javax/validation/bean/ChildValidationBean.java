package com.vergilyn.examples.javax.validation.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class ChildValidationBean extends ParentValidationBean {

	@NotNull
	@Positive
	@Max(value = 10, message = "`childNumberPositive` 最大值不能超过 10!")
	private Integer childNumberPositive;

	public static ChildValidationBean buildInvalid() {
		ChildValidationBean bean = new ChildValidationBean();
		bean.setName("parent-invalid-bean");
		bean.setNumberPositive(-10);
		bean.setChildNumberPositive(20);

		return bean;
	}
}
