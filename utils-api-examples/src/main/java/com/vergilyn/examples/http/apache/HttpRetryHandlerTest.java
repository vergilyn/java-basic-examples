package com.vergilyn.examples.http.apache;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

/**
 * <pre>
 *      HttpClient-RetryHandler重试 >>>> http://blog.csdn.net/minicto/article/details/56677420
 * </pre>
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/19
 */
public class HttpRetryHandlerTest {
    public static long getContentLength(String url){

        CloseableHttpClient build = HttpClients.custom()
                .setServiceUnavailableRetryStrategy(new ServiceUnavailableRetryStrategy() {
                    @Override
                    public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
                        System.out.println("exec retryRequest()");
                        return HttpStatus.SC_OK != response.getStatusLine().getStatusCode() && executionCount <= 3;
                    }

                    @Override
                    public long getRetryInterval() {
                        System.out.println("exec getRetryInterval()");
                        return 20000;
                    }
                })
                .build();

        CloseableHttpResponse resp;
        try {
            HttpGet httpGet = new HttpGet(url);

            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(1000)
                    .build();
            httpGet.setConfig(config);
            resp = build.execute(httpGet);

            if(HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()){
                return resp.getEntity().getContentLength();
            }
            return -1;
        }catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        String music = "https://d1.music.126.net/dmusic/cloudmusicsetup_2_2_2_195462.exe";
        String qq = "http://cdn.qq.ime.sogou.com/77fd43237b2e5ee12dbfb4d5f23c9975/5a102fdd/QQPinyin_Setup_5.7.4411.400.exe";

        System.out.println(getContentLength("http://www.baid.com"));
    }
}
