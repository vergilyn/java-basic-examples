package com.vergilyn.examples.usage.u0013.service;

import com.alibaba.fastjson.JSON;
import com.vergilyn.examples.usage.u0013.bean.ValidationBean;
import org.springframework.stereotype.Service;

@Service
public class ManualSpringService {

	public void get(ValidationBean info){
		System.out.println(JSON.toJSONString(info));
	}
}