package com.vergilyn.examples.usage.u0007;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/**
 * JAVA AES 加解密
 *
 * @author yangdongdong
 * @date 2018/2/25 下午4:13
 */
@Slf4j
public class BaiduAesUtil {
    private Cipher encCipher;
    private Cipher decCipher;

    private SecretKeySpec keySpec;
    private IvParameterSpec iv;

    /**
     * 构造函数
     *
     * @param encodingAesKey encodingAESKey
     *
     * @throws Exception 异常错误信息
     */
    private BaiduAesUtil(String encodingAesKey) throws Exception {
        byte[] aesKey = Base64.decodeBase64(encodingAesKey + "=");

        keySpec = new SecretKeySpec(aesKey, "AES");
        iv = new IvParameterSpec(aesKey, 0, 16);
        encCipher = Cipher.getInstance("AES/CBC/NoPadding");
        encCipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

        decCipher = Cipher.getInstance("AES/CBC/NoPadding");
        decCipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
    }

    /**
     * 对明文进行加密
     *
     * @param aesKey 加密key
     *
     * @return 加密后base64编码的字符串
     *
     * @throws Exception 异常错误信息
     */
    public static String encryptData(String aesKey, String content) throws Exception {
        BaiduAesUtil baiduAesUtil = BaiduAesUtil.getInstantce(aesKey);

        ByteGroup byteCollector = new ByteGroup();
        byte[] randomStrBytes = baiduAesUtil.getRandomStr().getBytes(StandardCharsets.UTF_8);
        byte[] textBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] networkBytesOrder = baiduAesUtil.getNetworkBytesOrder(textBytes.length);
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byte[] padBytes = PKCS7Encoder.encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        byte[] unencrypted = byteCollector.toBytes();
        try {
            byte[] encrypted = baiduAesUtil.encCipher.doFinal(unencrypted);
            return new Base64().encodeToString(encrypted);
        } catch (Exception e) {
            throw new Exception("ENCRYPT_AES_ERROR");
        }
    }

    /**
     * 对密文进行解密
     *
     * @param text 需要解密的密文
     *
     * @return 解密得到的明文
     *
     * @throws Exception 异常错误信息
     */
    public static String decrypt(String aesKey, String text) {
        BaiduAesUtil baiduAesUtil;
        byte[] original;
        try {
            baiduAesUtil = BaiduAesUtil.getInstantce(aesKey);

            byte[] encrypted = Base64.decodeBase64(text);
            original = baiduAesUtil.decCipher.doFinal(encrypted);

            // 去除补位字符
            byte[] bytes = PKCS7Encoder.decode(original);
            // 分离16位随机字符串,网络字节序和ClientId
            byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);
            int xmlLength = baiduAesUtil.recoverNetworkBytesOrder(networkOrder);
            return new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength),
                    StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AesUtilDecryptException ", e);
            return null;
        }
    }

    private static BaiduAesUtil getInstantce(String aesKey) throws Exception {
        return new BaiduAesUtil(aesKey);
    }

    /**
     * 随机生成16位字符串
     *
     * @return 随机串
     */
    private String getRandomStr() {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder stringBuild = new StringBuilder();
        int randStrLength = 16;
        for (int i = 0; i < randStrLength; i++) {
            int number = random.nextInt(base.length());
            stringBuild.append(base.charAt(number));
        }
        return stringBuild.toString();
    }

    /**
     * 还原4个字节的网络字节序
     *
     * @param orderBytes 字节码
     *
     * @return sourceNumber
     */
    private int recoverNetworkBytesOrder(byte[] orderBytes) {
        int sourceNumber = 0;
        int length = 4;
        int number = 8;
        for (int i = 0; i < length; i++) {
            sourceNumber <<= number;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    /**
     * 生成4个字节的网络字节序
     *
     * @param sourceNumber 文本长度
     *
     * @return orderBytes
     */
    private byte[] getNetworkBytesOrder(int sourceNumber) {
        byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }
}