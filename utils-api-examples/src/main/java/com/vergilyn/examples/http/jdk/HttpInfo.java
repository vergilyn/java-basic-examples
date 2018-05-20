package com.vergilyn.examples.http.jdk;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSON;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
public class HttpInfo {

    public static void urlInfo(String url) throws IOException {
        URL uri = new URL(url);

        System.out.println("getFile(): " + uri.getFile());
        System.out.println("getAuthority(): " + uri.getAuthority());
        System.out.println("getHost(): " + uri.getHost());
        System.out.println("getProtocol(): " + uri.getProtocol());
        System.out.println("getPath(): " + uri.getPath());
        System.out.println("getQuery(): " + uri.getQuery());
        System.out.println("getRef(): " + uri.getRef());
        System.out.println("getUserInfo(): " + uri.getUserInfo());

    }

    public static void URLConnection(String url) throws IOException {
        // 返回一个URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
        // 每次调用此 URL 的协议处理程序的 openConnection方法都打开一个新的连接。
        URLConnection conn = new URL(url).openConnection();
        System.out.println(conn.getClass());
        // A timeout of zero is interpreted as an infinite timeout.
        conn.setReadTimeout(0);
        conn.setConnectTimeout(100);

//        System.out.println("getRequestProperties(): " + JSON.toJSONString(conn.getRequestProperties()));
        System.out.println("getContentType(): " + conn.getContentType());
        System.out.println("getContentEncoding(): " + conn.getContentEncoding());
        System.out.println("getContentLengthLong(): " + conn.getContentLengthLong()); // 建议用此
        System.out.println("getContentLength(): " + conn.getContentLength()); // 最大到Integer.MAX
        System.out.println("getConnectTimeout(): " + conn.getConnectTimeout());
        System.out.println("getReadTimeout(): " + conn.getReadTimeout());
        System.out.println("getHeaderFields(): " + JSON.toJSONString(conn.getHeaderFields()));

    }

    public static void main(String[] args) {
        String url = "http://downloadcdn.quklive.com/userDownload/1504852699996767/1510826400469/1510814908816773.mp4";
        String qq = "http://cdn.qq.ime.sogou.com/77fd43237b2e5ee12dbfb4d5f23c9975/5a102fdd/QQPinyin_Setup_5.7.4411.400.exe";
        String qqs = "https://www.qq.ime.sogou.com/77fd43237b2e5ee12dbfb4d5f23c9975/5a102fdd/QQPinyin_Setup_5.7.4411.400.exe";
        try {
            URLConnection(qq);
            System.out.println("===============");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
