package com.vergilyn.examples.snakeyaml.convert;

import com.google.common.collect.Lists;
import com.vergilyn.examples.snakeyaml.AbstractSnakeyamlTests;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

class PojoToYamlTests extends AbstractSnakeyamlTests {

    @Test
    public void test_yamlString() {

        AlarmRule alarmRule = initAlarmRule();

        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        // options.setDefaultScalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setAllowReadOnlyProperties(false);
        // 避免多余的换行符
        // options.setWidth(20000);
        options.setSplitLines(false);
        // 数组或集合的元素前添加缩进符
        options.setIndicatorIndent(2);
        options.setIndentWithIndicator(true);

        Representer representer = new Representer(options);
        // 避免输出的yaml字符串内容
        representer.addClassTag(alarmRule.getClass(), Tag.MAP);

        Yaml yaml = new Yaml(representer, options);

        String yamlString = yaml.dump(alarmRule);
        System.out.println(yamlString);
    }

    private AlarmRule initAlarmRule() {

        AlarmRule alarmRule = new AlarmRule();
        alarmRule.setExpression("sum((endpoint_sla / 100) < 75) >= 3");
        alarmRule.setIncludeNames(Lists.newArrayList("include-names-a", "include-names-b"));
        alarmRule.setIncludeNamesRegex("includeNamesRegex");
        alarmRule.setExcludeNames(Lists.newArrayList("exclude-names-a", "exclude-names-b"));
        alarmRule.setExcludeNamesRegex("ExcludeNamesRegex");
        // alarmRule.setPeriod(5);
        alarmRule.setSilencePeriod(10);
        alarmRule.setMessage("Successful rate of endpoint {name} is lower than 75%");
        // alarmRule.setTags(Map.of("level", "WARNING"));
        // alarmRule.setHooks();

        return alarmRule;
    }

    @Data
    public static class AlarmRule {

        private String expression;

        private ArrayList<String> includeNames;

        private String includeNamesRegex;

        private ArrayList<String> excludeNames;

        private String excludeNamesRegex;

        private int period;

        private int silencePeriod;

        private String message;

        private Map<String, String> tags;

        private Set<String> hooks;
    }
}
