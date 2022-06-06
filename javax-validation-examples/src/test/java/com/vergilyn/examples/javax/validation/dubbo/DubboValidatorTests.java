package com.vergilyn.examples.javax.validation.dubbo;

import com.vergilyn.examples.javax.validation.bean.ValidationBean;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.validation.support.jvalidation.JValidator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.validation.Validation;

public class DubboValidatorTests {

	private ValidationBean invalid = ValidationBean.buildInvalid();

	/**
	 * <p> 1. 每次都会调用 {@linkplain  Validation#buildDefaultValidatorFactory()}
	 *
	 * <p> 2. dubbo-validation  其实存在 map-cache，不会每次都去生成 dubbo-validator
	 * <pre>
	 *   - {@link org.apache.dubbo.validation.Validation}
	 *   - {@link org.apache.dubbo.validation.support.AbstractValidation#getValidator(URL)}
	 *   - {@link org.apache.dubbo.validation.Validator}
	 * </pre>
	 *
	 * <p> 3. 最终使用逻辑，{@link org.apache.dubbo.validation.filter.ValidationFilter}
	 */
	@Test
	public void dubboValidator(){
		URL url = Mockito.mock(URL.class);

		Mockito.when(url.getServiceInterface()).then(invocationOnMock -> DubboInterface.class.getName());

		JValidator jValidator = new JValidator(url);

		try {
			jValidator.validate("get", new Class[]{ ValidationBean.class}, new Object[]{invalid});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
