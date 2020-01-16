package com.vergilyn.examples;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author VergiLyn
 * @date 2020-01-15
 */
@Slf4j
public class JsoupBasicTest {

    /**
     * 移除指定的html标签 <br>
     * E.g. HTML:
     *   {@code <div> <p>Hello</p> <img /> <p>there</p> <img /></div>}<br>
     *
     * remove `img` element, except: <br>
     *   {@code <div> <p>Hello</p>  <p>there</p> </div>}<br>
     */
    @Test
    public void removeElement(){
        String content = "<div> <p>Hello</p> <img /> <p>there</p> <img /></div>";

        Document document = Jsoup.parse(content);
        // select(...) -> elements that match the query (empty if none match)
        Elements img = document.select("img");
        img.remove();

        // System.out.println(document.toString());    // 包含了 html、head、body等标签。
        // System.out.println(document.body().toString());  // 包含了自身 body标签
        System.out.println(document.body().html());  // except
    }

    /**
     * 在指定html标签内部的最前端追加指定文本 <br>
     * E.g. HTML:
     *   {@code <div> <p>Hello</p> <p>there</p> </div>}<br>
     *
     * `p`标签内追加`append-`，except:
     *    {@code <div> <p>append-Hello</p> <p>append-there</p> </div>}<br>
     */
    @Test
    public void prepend(){
        String content = "<div> <p>Hello</p> <p>there</p> </div>";

        Document document = Jsoup.parse(content);
        document.select("p").prepend("append-");

        System.out.println(document.body().html());
    }

    /**
     * E.g. HTML:
     *   {@code <div> <p>Hello</p> <p>there</p> </div>}<br>
     *
     * 在给定html的最前端追加html标签（实际就是 html标签 放在body的内部最前端），except:
     *    {@code <img /><div> <p>Hello</p> <p>there</p> </div>}<br>
     *
     * 备注: jsoup解析后，会是完整的html（包含 html、head、body）等元素。
     */
    @Test
    public void appendElement(){
        String content = "<div> <p>Hello</p> <p>there</p> </div>";

        Document document = Jsoup.parse(content);
        document.body().prepend("<img />");

        System.out.println(document.html());
    }

    /**
     * 添加css <br>
     * E.g. HTML: <br>
     *   {@code <div> <p>Hello</p> <p style="text-indent: 1em; width: 100%;">there</p> </div>}<br>
     *
     * 给每个`p`添加css `text-indent: 2em`（如果存在相同，则替换），except: <br>
     *    {@code <div> <p style="text-indent: 2em;">Hello</p> <p style="text-indent: 2em; width: 100%;">there</p> </div>}<br>
     *
     * TODO 2020-01-16 未达到替换相同css的效果，而是覆写attr，`width: 100%;` 丢失！
     */
    @Test
    public void addCss(){
        String content = "<div> <p>Hello</p> <p style='text-indent: 1em;'>there</p> </div>";

        Document document = Jsoup.parse(content);
        document.select("p").attr("style", "text-indent: 2em;");

        System.out.println(document.html());
    }

    @DataProvider(name = "testdp", parallel = true)
    public Object[][] testdp() {
        String content = "";
        try {
            InputStream in = this.getClass().getResourceAsStream("/content_html.txt");
            content = IOUtils.toString(in, Charset.defaultCharset());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return new Object[][]{{content}};
    }
}
