package com.vergilyn.examples.bean;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.google.common.collect.Maps;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2018/7/25
 */
public class FastjsonTest {
    private Survey survey;

    @BeforeClass
    public void init(){
        survey = new Survey(1001L, "调查问卷");

        Subject s1 = new Subject(2001L, "题目1");
        Subject s2 = new Subject(2002L, "题目2");
        Subject s3 = new Subject(2003L, "题目3");

        survey.setLists(Lists.newArrayList(s1, s2, s3));

        Map<Long, Subject> map = Maps.newHashMap();
        map.put(s1.getId(), s1);
        map.put(s2.getId(), s1);
        map.put(s2.getId(), s1);

        survey.setMaps(map);
    }

    @Test
    public void typeReference() throws Exception {
        File file = ResourceUtils.getFile("classpath:data.json");
        String json = IOUtils.toString(FileUtils.openInputStream(file), StandardCharsets.UTF_8);

        List<List<Map<String, String>>> data = JSON
                .parseObject(json, new TypeReference<List<List<Map<String, String>>>>() {});

        System.out.println(data);
    }

    @Test
    public void json(){
        // properties指 需要序列化的属性；（可以看源码，有excludes属性，且apply中也使用了。只是不存在set）
        /* fastjson: 过滤属性的方式
         *   1. 实体类属性上@JSONField(serialize = false)
         *   2. 自定Filter
         */
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Survey.class, "id", "name", "date", "maps", "lists");
        // ComplexPropertyPreFilter filter = new ComplexPropertyPreFilter();  // 支持子对象属性过滤
        String json = JSON.toJSONString(survey, filter, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.PrettyFormat, SerializerFeature.WriteNonStringValueAsString);
        System.out.println(json);
    }

    @Test
    public void specialChar(){
        Map<String, String> map = Maps.newHashMap();
        map.put("lineChar", "包含换行符\r\n");
        map.put("httpChar", "https://www.github.com");

        System.out.println(JSON.toJSONString(map, SerializerFeature.PrettyFormat, SerializerFeature.WriteSlashAsSpecial));
    }

    @Test
    public void beanToArray(){
        Survey s = new Survey(1001L, "调查问卷");
        System.out.println(JSON.toJSONString(s, SerializerFeature.PrettyFormat, SerializerFeature.BeanToArray));
    }

    @Data
    @ToString
    private class Survey{
        private Long id;
        private String name;
        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime date;
       // @JSONField(serialize = false)
        private String exclude;
        private Map<Long, Subject> maps;
        private List<Subject> lists;

        public Survey() {
            this.date = LocalDateTime.now();
        }

        public Survey(Long id, String name) {
            this();
            this.id = id;
            this.name = name;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Subject{
        private Long id;
        private String title;
    }
}
