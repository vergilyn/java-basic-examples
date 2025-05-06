package com.vergilyn.examples.snakeyaml.convert;

import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

class YamlReaderCommentTest {

    @Test
    void test() {

        String yamlStr = """
                # 注释：名称
                name: abc
                # 注释：msg
                msg: 123
                """;

        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setProcessComments(true);

        Yaml yaml = new Yaml(loaderOptions);

        Object load = yaml.load(yamlStr);

        System.out.println(load);
    }
}
