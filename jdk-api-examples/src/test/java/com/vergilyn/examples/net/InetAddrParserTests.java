package com.vergilyn.examples.net;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class InetAddrParserTests {

	/**
	 * @see java.net.InetSocketAddress
	 */
	@SneakyThrows
	@Test
	public void test(){
		String host = "127.0.0.1";
		int port = 8080;
		String hostPort = host + ":" + port;

		InetAddress inetAddress = InetAddress.getByAddress(host.getBytes(StandardCharsets.UTF_8));

		System.out.println(inetAddress.getHostAddress());
	}
}
