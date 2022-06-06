package com.vergilyn.examples.javax.validation.spring;

import com.alibaba.fastjson.JSON;
import com.vergilyn.examples.javax.validation.bean.ValidationBean;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class AdviceSpringService {

	public void get(ValidationBean info1, @Valid ValidationBean info2){
		System.out.println("AdviceSpringService#get >>>>" + JSON.toJSONString(info1));
		System.out.println("AdviceSpringService#get >>>>" + JSON.toJSONString(info2));
	}
}