package com.vergilyn.examples.monitor.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderEntity {

	private Integer orderId;

	private Long userId;

	private Integer amount;

	private LocalDateTime createTime;

	private OrderChannelEnum channel;
}
