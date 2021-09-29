package com.vergilyn.examples.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-09-27
 *
 */
public class Dom4jXmlBuilderTest {

	/**
	 * 例如创建如下 xml（完成的）
	 * <pre>
	 * {@code
	 * <?xml version="1.0" encoding="utf-8" ?>
	 * <DOCUMENT>
	 *     <item>
	 *         <key>key词唯一且固定</key>
	 *         <display>
	 *             <query>关键词</query>
	 *             <display_type>kg</display_type>
	 *         </display>
	 *     </item>
	 * </DOCUMENT>
	 * }
	 * </pre>
	 *
	 * @see <a href="https://dom4j.github.io/#creating-document">https://dom4j.github.io/#creating-document</a>
	 */
	@Test
	public void create(){
		Document document = DocumentHelper.createDocument();

		Element docElement = document.addElement("DOCUMENT");

		final Element item = docElement.addElement("item");

		item.addElement("key").addText("key词唯一且固定");

		Element display = item.addElement("display");
		display.addElement("query").addText("关键词");
		display.addElement("display_type").addText("kg");

		System.out.println(docElement.asXML());

		System.out.println();

		System.out.println(document.asXML());

	}

	@Test
	public void createElement(){
		Element item = DocumentHelper.createElement("item");

		item.addElement("key").addText("key词唯一且固定");

		Element display = item.addElement("display");
		display.addElement("query").addText("关键词");
		display.addElement("query").addText("关键词2");
		display.addElement("display_type").addText("kg");

		System.out.println(item.asXML());
	}
}
