package com.vergilyn.examples.http.jdk;

import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSON;

import sun.net.www.protocol.http.HttpURLConnection;


/**
 * 针对URLConnection的学习
 * <pre>参考:
 *  URLConnection的连接、超时、关闭用法总结 >>>> https://www.cnblogs.com/daishuguang/archive/2014/05/03/3705568.html
 *  HTTP Header 详解 >>>> http://kb.cnblogs.com/page/92320/
 * <pre/>
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
public class URLConnectionInfo {
    public static void main(String[] args) {
        String qq = "http://cdn.qq.ime.sogou.com/77fd43237b2e5ee12dbfb4d5f23c9975/5a102fdd/QQPinyin_Setup_5.7.4411.400.exe";
        String baidu = "http://www.baidu.com";
        try {
            info(baidu);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String url) throws IOException {

        // 返回一个URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
        // 每次调用此 URL 的协议处理程序的 openConnection方法都打开一个新的连接(并未实际发送请求)。
        URLConnection urlConnection = new URL(url).openConnection();

        // sun.net.www.protocol.http.HttpURLConnection;
        // 或 java.net.HttpURLConnection;
        HttpURLConnection conn = (HttpURLConnection) urlConnection;

        conn = setRequest(conn);

        // 显示调用连接, 所有request property配置必须要在connect之前设置才有效.
        // 也可以不显示调用, 之后的所有的conn.getXXX操作都会去检测并隐式调用getInputStream();
        conn.connect();

        conn.getInputStream(); // <== 实际发送请求的代码段在这里面
        responseInfo(conn);

        /* 摘自:
         * a) HttpURLConnection的connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。
         *      无论是post还是get，http请求实际上直到HttpURLConnection的getInputStream()这个函数里面才正式发送出去;
         * b) 在用POST方式发送URL请求时，URL请求参数的设定顺序是重中之重;
         * c) 对connection对象的一切配置(那一堆set函数)都必须要在connect()函数执行之前完成。
         * d) 对outputStream的写操作，必须要在inputStream的读操作之前。
         * 这些顺序实际上是由http请求的格式决定的。
         */
    }

    private static void responseInfo(HttpURLConnection conn) throws IOException {

        System.out.println("getResponseCode(): " + conn.getResponseCode());
        System.out.println("getResponseMessage(): " + conn.getResponseMessage());
        System.out.println("getContentType(): " + conn.getContentType());
        System.out.println("getContentEncoding(): " + conn.getContentEncoding());
        System.out.println("getContentLengthLong(): " + conn.getContentLengthLong()); // 建议用此
        System.out.println("getContentLength(): " + conn.getContentLength()); // 最大到Integer.MAX
        System.out.println("getConnectTimeout(): " + conn.getConnectTimeout());
        System.out.println("getReadTimeout(): " + conn.getReadTimeout());
        System.out.println("getHeaderFields(): " + JSON.toJSONString(conn.getHeaderFields()));
    }

    /**
     *
     * @param conn
     * @return
     * @throws ProtocolException
     */
    private static HttpURLConnection setRequest(HttpURLConnection conn) throws ProtocolException {
        // 设置是否向httpUrlConnection输出因
        // 如果是POST, 参数要放在http正文内,因此需要设为true, 默认情况下是false;
        conn.setDoOutput(false);

        // 设置是否从httpUrlConnection读入，默认情况下是true;
        conn.setDoInput(true);

        // POST 请求不能使用缓存
        conn.setUseCaches(false);

        // 设置RequestProperty
        // 参考 >> (HTTP Header 详解) http://kb.cnblogs.com/page/92320/
        conn.setRequestProperty("Content-type", "application/x-www-form-urlencodedt");

        // 设定请求的方法为"POST", 默认是GET
        conn.setRequestMethod("GET");

        // 0表示无限.
        conn.setConnectTimeout(0); // 设置连接主机超时（单位：毫秒）
        conn.setReadTimeout(0); // 设置从主机读取数据超时（单位：毫秒）

        // 省略更多set... 详见API
        // conn.setChunkedStreamingMode();

        return conn;
    }
}
