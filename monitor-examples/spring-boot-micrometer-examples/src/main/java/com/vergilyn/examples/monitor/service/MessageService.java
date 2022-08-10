package com.vergilyn.examples.monitor.service;

import com.vergilyn.examples.monitor.monitor.MetricsMonitor;
import com.vergilyn.examples.monitor.pojo.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
@Service
public class MessageService extends Thread {
	public final BlockingQueue<MessageEvent> messageQueue;

	public MessageService() {
		setName("vergilyn-message-");

		messageQueue = new ArrayBlockingQueue<>(1000);
		MetricsMonitor.getMessageGauge(messageQueue);

		start();
	}

	public boolean sendMessage(MessageEvent message) {
		return messageQueue.offer(message);
	}

	@Override
	public void run() {
		while (true){
			MessageEvent message = null;
			try {
				message = messageQueue.take();
				log.info("[vergilyn] 模拟消息发送：{}", message);

			}catch (Exception e){
				log.error("[vergilyn] 消息发送异常，message: {}，errMsg: {}", message, e.getMessage(), e);
			}
		}
	}
}
