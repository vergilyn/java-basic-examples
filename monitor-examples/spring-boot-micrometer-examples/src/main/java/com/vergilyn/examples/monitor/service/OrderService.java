package com.vergilyn.examples.monitor.service;

import com.vergilyn.examples.monitor.monitor.MetricsMonitor;
import com.vergilyn.examples.monitor.pojo.MessageEvent;
import com.vergilyn.examples.monitor.pojo.OrderEntity;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

	@Autowired
	private MessageService messageService;

	public Boolean createOrder(OrderEntity order) {
		// 1. 模拟耗时
		long random = RandomUtils.nextLong(1000, 3000);
		try {
			TimeUnit.MILLISECONDS.sleep(random);
		}catch (Exception ignore){
		}

		// 2. 记录下单数
		MetricsMonitor.getOrderCount(order.getChannel()).increment();

		// 3. 异步消息
		MessageEvent message = new MessageEvent();
		message.setOrderId(order.getOrderId());
		message.setUserId(order.getUserId());
		message.setContent(String.format("模拟异步消息。random: %s，time: %s", random, LocalDateTime.now()));

		messageService.sendMessage(message);

		return Boolean.TRUE;
	}
}
