package com.vergilyn.examples.monitor.controller;

import com.vergilyn.examples.monitor.pojo.OrderEntity;
import com.vergilyn.examples.monitor.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@PostMapping("/order")
	public ResponseEntity<Boolean> createOrder(@RequestBody OrderEntity order){

		if (order.getCreateTime() == null){
			order.setCreateTime(LocalDateTime.now());
		}

		return ResponseEntity.ok(orderService.createOrder(order));
	}
}
