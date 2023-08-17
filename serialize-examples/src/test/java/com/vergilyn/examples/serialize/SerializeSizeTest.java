package com.vergilyn.examples.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <h3>背景</h3>
 * 例如，存在上亿用户基础信息，此时希望将这些信息全部根据userId写入到 redis中。
 * 即，1个redis的 string-key 对应1个用户。
 *
 * <p> 如果使用常规的json，那么其实会有接近一半的内存消耗都是由于json-key导致。
 * <p> 因此，主要需求是：如何减少redis内存消耗。考虑使用非json序列化（或者相关压缩算法）
 *
 * @author vergilyn
 * @since 2023-07-25
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SerializeSizeTest {
    private static final UserInfo _userInfo = UserInfo.newInstance();
    private String string = null;
    private byte[] bytes = null;

    private int stringLength = 0;
    private int bytesLength = 0;

    @AfterEach
    public void afterEach(TestInfo testInfo){
        System.out.printf("%s >>>> string.length: %d, bytes.length: %d\n\t%s\n\n",
                          testInfo.getDisplayName(), string.length(), bytes.length, string);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(bytes.length).isEqualTo(bytesLength);
        softAssertions.assertThat(string.length()).isEqualTo(stringLength);
        softAssertions.assertAll();
    }

    @Test
    @Disabled
    void test_jdk_serialize(){

    }

    /**
     * key 大约就占用了一半的大小
     */
    @Test
    void test_fastjson_serialize(){
        string = JSON.toJSONString(_userInfo);
        bytes = string.getBytes();

        stringLength = string.length();
        bytesLength = bytes.length;
    }

    /**
     * 通过结果可知，保存了class的信息，所以会占用更多的空间。
     */
    @Test
    void test_redis_json_serialize(){
        bytes = RedisSerializer.json().serialize(_userInfo);
        string = new String(bytes, StandardCharsets.UTF_8);

        bytesLength = 1069;
        stringLength = 1049;
    }

    @Test
    void test_redis_java_serialize(){
        bytes = RedisSerializer.java().serialize(_userInfo);
        string = new String(bytes, StandardCharsets.UTF_8);

        bytesLength = 1373;
        stringLength = 1351;
    }

    @Test
    void test_fastjson_redis_serializer(){
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 由于json中写入了class信息，所以会导致json占用更多的空间
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteClassName);
        fastJsonConfig.setFeatures(Feature.SupportAutoType);

        FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<>(Object.class);
        serializer.setFastJsonConfig(fastJsonConfig);

        bytes = serializer.serialize(_userInfo);
        string = new String(bytes, StandardCharsets.UTF_8);

        bytesLength = 646;
        stringLength = 626;
    }

    @Test
    void test_jackson2_redis_serializer(){
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        bytes = jackson2JsonRedisSerializer.serialize(_userInfo);
        string = new String(bytes, StandardCharsets.UTF_8);

        bytesLength = 1065;
        stringLength = 1045;
    }

    @SneakyThrows
    @Test
    void test_hessian_serialize(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output hessianOutput = new Hessian2Output(outputStream);
        hessianOutput.startCall();
        hessianOutput.writeObject(_userInfo);
        hessianOutput.completeCall();

        bytes = outputStream.toByteArray();
        // string = new String(bytes, StandardCharsets.UTF_8);


    }

    public static void main(String[] args) throws IOException {
        String string1 = "测试hessian2序列化，时间：2023-07-26 09:22:12";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Hessian2Output hessianOutput = new Hessian2Output(outputStream);
        hessianOutput.writeObject(string1);
        hessianOutput.flush();

        System.out.println(string1.length());
        System.out.println(string1.getBytes().length);

        byte[] byteArray = outputStream.toByteArray();

        System.out.println(byteArray.length);
        System.out.println(new String(byteArray));

        // 反序列化
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        Hessian2Input hessianInput = new Hessian2Input(inputStream);
        String deserializedPerson = (String) hessianInput.readObject();
        System.out.println(deserializedPerson);
    }
}
