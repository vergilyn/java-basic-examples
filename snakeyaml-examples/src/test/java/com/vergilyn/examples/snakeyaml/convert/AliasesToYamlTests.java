package com.vergilyn.examples.snakeyaml.convert;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.serializer.AnchorGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AliasesToYamlTests {


    @Test
    void test() {

        List<String> hooks = Lists.newArrayList("webhook", "email");

        Config c1 = new Config("c1", hooks);
        Config c2 = new Config("c2", hooks);

        Map<String, Config> configs = new HashMap<>();
        configs.put("c1", c1);
        configs.put("c2", c2);

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        // options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowReadOnlyProperties(false);
        // 避免多余的换行符
        options.setSplitLines(false);
        // 数组前保留缩进符
        options.setIndentWithIndicator(true);
        options.setIndicatorIndent(2);
        // 从 snakeyaml-2.3+ 之后支持
        // options.setDereferenceAliases(false);
        options.setAnchorGenerator(new AnchorGenerator() {
            @Override
            public String nextAnchor(Node node) {
                return "";
            }
        });

        Representer representer = new Representer(options);
        // 避免输出的yaml字符串内容
        representer.addClassTag(configs.getClass(), Tag.MAP);
        representer.addClassTag(Config.class, Tag.MAP);

        Yaml yaml = new Yaml(representer, options);

        String yamlString = yaml.dump(configs);
        System.out.println(yamlString);

    }

    @Setter
    @Getter
    @NoArgsConstructor
    private static class Config {

        private String name;
        private List<String> hooks;

        public Config(String name, List<String> hooks) {
            this.name = name;
            this.hooks = hooks;
        }
    }
}
