package com.vergilyn.examples.http.apache;

import java.io.IOException;
import java.util.Locale;

import com.alibaba.fastjson.JSON;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * 针对apache-HttpClient学习.<br/>
 * <pre>
 *   HttpClient是Apache Jakarta Common下的子项目，用来提供高效的、最新的、功能丰富的支持HTTP协议的客户端编程工具包.
 * <pre/>
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
public class HttpClientTest {

    public static void main(String[] args) {
        String url = "http://www.baidu.com";
        String qq = "http://cdn.qq.ime.sogou.com/77fd43237b2e5ee12dbfb4d5f23c9975/5a102fdd/QQPinyin_Setup_5.7.4411.400.exe";

        // 1. 创建http-client
        CloseableHttpClient build = HttpClientBuilder.create().build();

        try {
            // 2. 构建GET请求
            HttpGet httpGet = new HttpGet(qq);
            // 请求配置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(0)
                    .setConnectTimeout(0)
                    .build();
            httpGet.setConfig(requestConfig);
            // 请求头部
            httpGet.setHeader(HttpHeaders.TIMEOUT,"0");

            // 3. 发送请求
            CloseableHttpResponse resp = build.execute(httpGet);
            // 4. 获取响应
            StatusLine respStatus = resp.getStatusLine();
            System.out.println("getStatusLine(): " + JSON.toJSONString(respStatus));

            ProtocolVersion protocolVersion = resp.getProtocolVersion();
            System.out.println("getProtocolVersion(): " + JSON.toJSONString(protocolVersion));

            Locale locale = resp.getLocale();
            System.out.println("getLocale(): " + JSON.toJSONString(locale));


            HttpEntity respEntity = resp.getEntity();
            String bodyAsString = EntityUtils.toString(respEntity,"utf-8");
            System.out.println("getEntity(): " + bodyAsString);

            Header headers = resp.getFirstHeader(HttpHeaders.CONTENT_LENGTH);
            System.out.println(headers.getValue());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
