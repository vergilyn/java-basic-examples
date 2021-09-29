package com.vergilyn.examples.file;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;

/**
 * 在 文件中的 指定位置 插入指定的内容。
 *
 * @author vergilyn
 * @since 2021-09-27
 *
 * @see java.io.RandomAccessFile
 */
public class FileInsertPositionTests {
	private static final String XML_ROOT = "DOCUMENT";
	private static final String END_CHARS = "</sitemapindex></DOCUMENT>";
	private static final String FILENAME = "bytedance-sitemap.xml";

	private String filepath(){
		String filepath = "";

		// `this.getClass().getResource(FILENAME).toURI()`得到的是 `/target/test-class/...` 中的文件。

		filepath += System.getProperty("user.dir");
		filepath += File.separator + "src" + File.separator + "test" + File.separator + "java";
		filepath += File.separator + this.getClass().getPackage().getName().replace('.', File.separatorChar);
		filepath += File.separator + FILENAME;

		return filepath;
	}

	/**
	 * xml内容(格式化后)：
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="UTF-8"?>
	 * <DOCUMENT>
	 *     <sitemapindex>
	 *         <sitemap>
	 *             <loc>http://host:port/xml_0.xml</loc>
	 *             <lastmod><![CDATA[2021-09-28]]></lastmod>
	 *         </sitemap>
	 *     </sitemapindex>
	 * </DOCUMENT>
	 * }
	 * </pre>
	 */
	@SneakyThrows
	@Test
	public void createFullXml(){
		final Document document = DocumentHelper.createDocument();
		final Element root = DocumentHelper.createElement(XML_ROOT);
		document.setRootElement(root);

		final Element sitemapindex = root.addElement("sitemapindex");
		final Element sitemap = sitemapindex.addElement("sitemap");
		sitemap.addElement("loc").addText("http://host:port/xml_0.xml");
		sitemap.addElement("lastmod").addCDATA(LocalDate.now().toString());

		// 并未格式化
		final String xmlString = document.asXML();

		final File file = new File(filepath());
		System.out.println("准备写入xml文件内容 >>>> " + file.getAbsolutePath());
		System.out.println(xmlString);
		try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")){
			// 清空原来的文件内容
			randomAccessFile.setLength(0);

			randomAccessFile.write(xmlString.getBytes(StandardCharsets.UTF_8));
		}

	}

	@SneakyThrows
	@Test
	public void insertPosition(){
		final Element sitemap = DocumentHelper.createElement("sitemap");
		sitemap.addElement("loc").addText("http://host:port/insert-position-中文.xml");
		sitemap.addElement("lastmod").addCDATA(LocalDate.now().toString());

		try(RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filepath()), "rw")){
			// 指定位置处开始 **覆盖写**。 （如果写的 `write-bytes < surplus-bytes`，之后的 内容其实会被保留）
			randomAccessFile.seek(randomAccessFile.length() - END_CHARS.length());

			// seek-position 之后的数据会丢失（被覆盖）
			// randomAccessFile.writeBytes(sitemap.asXML());
			// `writeBytes`不一定是`utf-8`，可能导致最终的xml文件乱码
			randomAccessFile.write(sitemap.asXML().getBytes(StandardCharsets.UTF_8));

			// 还原 被覆盖 的内容
			randomAccessFile.write(END_CHARS.getBytes(StandardCharsets.UTF_8));
		}
	}

	@SneakyThrows
	@Test
	public void clearContent(){

		try(RandomAccessFile randomAccessFile = new RandomAccessFile(new File(filepath()), "rw")) {
			// 可以清除内容
			randomAccessFile.setLength(0);
		}
	}
}
