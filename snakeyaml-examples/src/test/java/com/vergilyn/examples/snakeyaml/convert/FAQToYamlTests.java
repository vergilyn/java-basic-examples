package com.vergilyn.examples.snakeyaml.convert;

import lombok.Data;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class FAQToYamlTests {

    /**
     * 空数组或者空集合怎么输出{@code []}，而不是{@code null}?
     */
    @Test
    void test_formatNullArrays() {

        Pojo pojo = new Pojo();
        pojo.setStr("abc");
        pojo.setArrays(new String[0]);

        Yaml defaultPrintYaml = initDefaultPrintYaml();
        System.out.println("[yaml] Default print >>>> ");
        System.out.println(defaultPrintYaml.dump(pojo));

        // 期望数组或集合不要输出null，而是输出 []
        DumperOptions dumperOptions = initDefaultDumperOptions();

        Yaml yaml = new Yaml(dumperOptions);
        System.out.println("[yaml] Custom print >>>> ");
        System.out.println(yaml.dump(pojo));

    }

    /**
     * 输出结果中排除空值。
     */
    @Test
    void test_excludeNullValue() {
        Pojo pojo = new Pojo();
        pojo.setStr("abc");
        pojo.setArrays(new String[0]);

        Yaml defaultPrintYaml = initDefaultPrintYaml();
        System.out.println("[yaml] Default print >>>> ");
        System.out.println(defaultPrintYaml.dump(pojo));

        DumperOptions dumperOptions = initDefaultDumperOptions();
        Representer representer = new Representer(dumperOptions) {
            @Override
            protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue,
                                                          Tag customTag) {

                if (propertyValue == null) {
                    return null;
                }

                return super.representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        };

        Yaml yaml = new Yaml(representer, dumperOptions);
        System.out.println("[yaml] Custom print >>>> ");
        System.out.println(yaml.dump(pojo));
    }

    /**
     * TODO 2025-01-06 数据源是{@link Map}类型，需要怎么处理？
     */
    @Test
    void test_mapExcludeNullValue() {
        Map<String , Object> map = ofMap(
                "type", "map",
                "str", "abc",
                "lists", null,
                "arrays", null,
                "maps", null

        );

        Yaml defaultPrintYaml = initDefaultPrintYaml();
        System.out.println("[yaml] Default print >>>> ");
        System.out.println(defaultPrintYaml.dump(map));

        DumperOptions dumperOptions = initDefaultDumperOptions();
        Representer representer = new Representer(dumperOptions) {

            @Override
            protected Node representMapping(Tag tag, Map<?, ?> mapping, DumperOptions.FlowStyle flowStyle) {
                return super.representMapping(tag, mapping, flowStyle);
            }

        };


        Yaml yaml = new Yaml(representer, dumperOptions);

        System.out.println("[yaml] Custom print >>>> ");
        System.out.println(yaml.dump(map));
    }


    private Yaml initDefaultPrintYaml() {

        // 默认输出格式
        return new Yaml(initDefaultDumperOptions());
    }

    private DumperOptions initDefaultDumperOptions() {

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(false);
        // options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowReadOnlyProperties(false);
        options.setSplitLines(false);

        return options;
    }

    private Map<String, Object> ofMap(Object... kvs) {

        Map<String, Object> ret = new LinkedHashMap<>();

        for (int i = 0; i < kvs.length; i += 2) {
            ret.put((String) kvs[i], kvs[i + 1]);
        }

        return ret;
    }

    @Data
    private static class Pojo {

        private final String type = "pojo";

        private String str;
        private Integer integer;
        private int _int;

        private List<String> lists;

        private String[] arrays;

        private Map<String, String> maps;

    }
}
