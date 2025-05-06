package com.vergilyn.examples.http;

import com.google.common.base.Stopwatch;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;

public class HttpInputStreamTest {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) {

        // 150mb
        String url = "https://dlcdn.apache.org/skywalking/10.1.0/apache-skywalking-apm-10.1.0.tar.gz";
        // 二维码图片，只有2~3kb
        url = "https://mass.alipay.com/wsdk/img?fileid=A*rPJ3S58GL1cAAAAAAAAAAAAAAQAAAQ&bz=am_afts_openhome&zoom=227w_277h";

        HttpInputStreamTest test = new HttpInputStreamTest();

        Stopwatch stopwatch = Stopwatch.createStarted();

        InputStream inputStream = test.fetch(url);

        System.out.printf("[DEMO] >>>> fetch cost: %s \n", stopwatch.stop());

        try {

            FileUtils.copyInputStreamToFile(inputStream, new java.io.File("d:/tmp/mock-http-is.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream fetch(String url) {

        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            // EntityUtils.toByteArray(response.getEntity());

            return  response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // httpGet.releaseConnection();
            IOUtils.closeQuietly(response);
        }

        return null;
    }


}
