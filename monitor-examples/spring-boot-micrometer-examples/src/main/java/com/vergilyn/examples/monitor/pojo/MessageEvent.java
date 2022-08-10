package com.vergilyn.examples.monitor.pojo;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class MessageEvent {
	private Integer orderId;

	private Long userId;

	private String content;

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
