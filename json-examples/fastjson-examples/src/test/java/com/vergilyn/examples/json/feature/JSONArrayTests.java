package com.vergilyn.examples.json.feature;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JSONArrayTests {

    @Test
    void test(){
        Map<String, Object> row1 = Maps.newHashMap();
        row1.put("id", 1);
        row1.put("mobile", "138xxxx0001");
        row1.put("updateTime", "2023-07-21 08:00:00");

        Map<String, Object> row2 = Maps.newHashMap();
        row2.put("id", 2);
        row2.put("mobile", "138xxxx0002");
        row2.put("updateTime", "2023-07-21 09:00:00");

        List<Map<String, Object>> rows = Lists.newArrayList(row1, row2);

        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(rows);

        List<Person> personList = jsonArray.toJavaList(Person.class);

        assertThat(personList).hasSize(2)
                .containsExactly(
                        new Person(1, "138xxxx0001", "2023-07-21 08:00:00"),
                        new Person(2, "138xxxx0002", "2023-07-21 09:00:00")
                );
    }

    @Data
    @NoArgsConstructor
    @EqualsAndHashCode
    private static class Person implements Serializable {
        private Integer id;

        private String mobile;

        private Date updateTime;

        public Person(Integer id, String mobile, String updateTime) {
            this.id = id;
            this.mobile = mobile;

            try {
                this.updateTime = DateUtils.parseDate(updateTime, "yyyy-MM-dd HH:mm:ss");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
