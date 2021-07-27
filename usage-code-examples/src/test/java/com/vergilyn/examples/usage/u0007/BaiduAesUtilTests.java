package com.vergilyn.examples.usage.u0007;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class BaiduAesUtilTests {
	/**
	 * 32chars  vergilyn
	 */
	private static final String AES_KEY = "ABCDEFGHIJKLMN0123456789vergilyn";

	@SneakyThrows
	@Test
	public void test(){
		String content = "{\"name\":\"vergilyn\",\"qq\":409839163,\"email\":\"vergilyn@vip.qq.com\"}";
		System.out.println("content >>>> " + content);

		String encrypt = BaiduAesUtil.encryptData(AES_KEY, content);
		System.out.println("encrypt >>>> " + encrypt);

		String decrypt = BaiduAesUtil.decrypt(AES_KEY, encrypt);
		System.out.println("decrypt >>>> " + decrypt);
	}

}
