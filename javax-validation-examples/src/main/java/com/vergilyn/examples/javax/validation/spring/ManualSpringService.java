package com.vergilyn.examples.javax.validation.spring;

import com.alibaba.fastjson.JSON;
import com.vergilyn.examples.javax.validation.bean.ParentValidationBean;
import org.springframework.stereotype.Service;

@Service
public class ManualSpringService {

	public void get(ParentValidationBean info){
		System.out.println(JSON.toJSONString(info));
	}
}