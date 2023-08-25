package com.vergilyn.examples.maven;

import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class PomDependencyParserTests {

    /**
     * `mvn dependency:list`
     */
    @SneakyThrows
    @Test
    void parser(){
        Map<String, String> dubbo_3_0_11 = parserDependencies("dubbo-dependencies-bom-3.0.11.pom");
        Map<String, String> dubbo_3_1_11 = parserDependencies("dubbo-dependencies-bom-3.1.11.pom");

        for (Map.Entry<String, String> entry : dubbo_3_1_11.entrySet()) {
            String dependency = entry.getKey();
            String version_3_1_11 = entry.getValue();

            String version_3_0_11 = dubbo_3_0_11.remove(dependency);
            version_3_0_11 = StringUtils.isBlank(version_3_0_11) ? "" : version_3_0_11;

            System.out.printf("%s\t%s\t%s", dependency, version_3_0_11, version_3_1_11).println();
        }

        System.out.println("dubbo_3_0_11 >>>>>>>>>>>>  ");
        for (Map.Entry<String, String> entry : dubbo_3_0_11.entrySet()) {
            System.out.printf("%s\t%s", entry.getKey(), entry.getValue()).println();

        }
    }

    @SneakyThrows
    private Map<String, String> parserDependencies(String filename){
        String filepath = filepath(filename);
        String xmlContent = FileUtils.readFileToString(new File(filepath), StandardCharsets.UTF_8);

        Document document = DocumentHelper.parseText(xmlContent);

        Element rootElement = document.getRootElement();

        Map<String, String> properties = parserProperties(rootElement);

        Element dependencies = rootElement.element("dependencyManagement").element("dependencies");
        List<Element> elements = dependencies.elements();

        Map<String, String> result = Maps.newLinkedHashMapWithExpectedSize(elements.size());
        for (Element dependency : elements) {
            String groupId = dependency.element("groupId").getText();
            String artifactId = dependency.element("artifactId").getText();
            String version =  dependency.element("version").getText();

            if (version.startsWith("${") && version.endsWith("}")){
                version = version.substring(2, version.length() - 1);
                version = properties.get(version);
            }

            result.put(groupId + ": " + artifactId, version);
        }

        return result;
    }

    private Map<String, String> parserProperties(Element rootElement){
        Element properties = rootElement.element("properties");

        List<Element> elements = properties.elements();

        Map<String, String> result = Maps.newHashMapWithExpectedSize(elements.size());
        for (Element element : elements) {
            result.put(element.getName(), element.getText());
        }

        return result;
    }

    private String filepath(String filename){
        String filepath = "";

        // `this.getClass().getResource(FILENAME).toURI()`得到的是 `/target/test-class/...` 中的文件。

        filepath += System.getProperty("user.dir");
        filepath += File.separator + "src" + File.separator + "test" + File.separator + "java";
        filepath += File.separator + this.getClass().getPackage().getName().replace('.', File.separatorChar);
        filepath += File.separator + filename;

        return filepath;
    }
}
