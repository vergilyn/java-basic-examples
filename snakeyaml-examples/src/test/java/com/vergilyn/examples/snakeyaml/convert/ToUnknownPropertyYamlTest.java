package com.vergilyn.examples.snakeyaml.convert;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ToUnknownPropertyYamlTest {

    @Test
    void test_unknown() {

        String yaml = """
            arrays:
              - a
              - b
              - c
            str: abc
            number: 1024
            map:
              level: WARN
              env: test
            #unknown: pojo未定义的字段-01
            nested:
              str: 嵌套对象
              #unknown: pojo未定义的字段-02
            """;

        Yaml yamlParser = new Yaml(new SafeConstructor(new LoaderOptions()));
        yamlParser.setBeanAccess(BeanAccess.FIELD);

        TestPojo testPojo = yamlParser.loadAs(yaml, TestPojo.class);

        Assertions.assertThat(testPojo).isNotNull();
    }


    @Data
    @NoArgsConstructor
    public static class TestPojo implements Serializable {

        private static final long serialVersionUID = 1L;

        private String str;
        private Integer number;
        private List<String> arrays;
        private Map<String, String> map;

        private TestPojo nested;
    }
}
