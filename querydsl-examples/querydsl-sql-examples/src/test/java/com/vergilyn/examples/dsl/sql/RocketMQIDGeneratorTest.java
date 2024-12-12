package com.vergilyn.examples.dsl.sql;

import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.message.MessageClientIDSetter;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * <p>参考：
 * <ol>
 *     <li><a href="https://github.com/apache/rocketmq/blob/rocketmq-all-5.2.0/common/src/main/java/org/apache/rocketmq/common/message/MessageClientIDSetter.java">rocketmq-all-5.2.0，MessageClientIDSetter.java</a></li>
 *     <li><a href="https://github.com/apache/rocketmq/blob/rocketmq-all-5.2.0/common/src/main/java/org/apache/rocketmq/common/message/MessageClientIDSetter.java">rocketmq-all-5.2.0，MessageClientIDSetter.java</a></li>
 * </ol>
 *
 * @see MessageClientIDSetter
 */
public class RocketMQIDGeneratorTest {


    @RepeatedTest(1)
    public void test(){
        // 总共 32byte，
        // 固定 10byte：4(ip/currentTimeMillis) + 2(pid) + 4(classLoader.hashCode) 转换成 20byte的String。
        // 0~19： 固定字符串
        // 20~27: 时间差
        // 28~31: 自增counter
        // C0A861353D9863947C6B0CBC10840000
        String uniqID = MessageClientIDSetter.createUniqID();
        System.out.println(uniqID);
    }

    /**
     *
     * <p> {@link Short} 占用 2byte 范围：-2<sup>15</sup> ~ 2<sup>15</sup>-1.
     * <p> {@link Integer} 占用 4byte 范围：-2<sup>31</sup> ~ 2<sup>31</sup>-1.
     *
     * @see Inet4Address#getAddress()
     * @see MessageClientIDSetter
     * @see org.apache.rocketmq.common.UtilAll#bytes2string(byte[])
     */
    @RepeatedTest(5)
    void test_fixPrefix(){
        // 16进制
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

        // IP：192.168.97.53
        //   简单理解因为 byte -128~127, 因此 -64 表示 256 - 64 = 192，256 - 88 = 168.具体参考 Inet4Address#getAddress()
        byte[] ip = {-64, -88, 97, 53};

        ByteBuffer tempBuffer = ByteBuffer.allocate(ip.length + 2 + 4);
        tempBuffer.put(ip);
        tempBuffer.putShort((short) 10086);
        tempBuffer.putInt(this.getClass().getClassLoader().hashCode());

        byte[] src = tempBuffer.array();
        // 因为 1byte=8bit，1个16进制=4bit，所以 1个byte 转换成 2个16进制。
        char[] hexChars = new char[src.length * 2];
        for (int j = 0; j < src.length; j++) {
            int v = src[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        String result = new String(hexChars);

        System.out.println(result);
    }

    /**
     * {@link Long} 占8byte，取后4byte
     *
     * @see MessageClientIDSetter#createUniqID()
     * @see MessageClientIDSetter#createFakeIP()
     */
    @Test
    public void createFakeIP() {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(System.currentTimeMillis());
        bb.position(4);

        System.out.println("before >>>> " + Arrays.toString(bb.array()));

        byte[] fakeIP = new byte[4];
        bb.get(fakeIP);

        System.out.println("after >>>> " + Arrays.toString(fakeIP));
    }

    /**
     * @see MessageClientIDSetter#createUniqID()
     * @see UtilAll#writeInt(char[], int, int)
     * @see UtilAll#writeShort(char[], int, int)
     */
    @Test
    public void test_byteBuffer() {

        char[] hexArray = "0123456789ABCDEF".toCharArray();

        char[] buffer = new char[32];
        int pos = 28;  // 固定 28~31表示自增counter
        int value = 10086;
        for (int moveBits = 12; moveBits >= 0; moveBits -= 4) {
            // >>> 无符号右移运算符（高位补0，所以结果总是非负数）；>> 有符号右移运算符（结果 正负与原正数保持一致）
            buffer[pos++] = hexArray[(value >>> moveBits) & 0x0F];
        }

    }


}
