package com.vergilyn.examples.json.serialize;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class SerializeFieldSortTests {
    private DTO dto = new DTO();

    @Test
    void defaultSort(){
        String def = JSON.toJSONString(dto);
        String sortField = JSON.toJSONString(dto, SerializerFeature.SortField);

        // 默认：根据fieldName序列化
        Assertions.assertThat(def).isEqualTo(sortField).isEqualTo("{\"a\":1,\"b\":2,\"c\":3,\"d\":4}");
    }

    /**
     * @see <a href="https://github.com/alibaba/fastjson/issues/3115">
     *    issues#3115, 支持按照成员变量声明顺序，做序列化字段排序</a>
     */
    @Test
    void issues_3115(){
        JSON.DEFAULT_GENERATE_FEATURE &= ~SerializerFeature.SortField.getMask();
        SerializeConfig serializeConfig = new SerializeConfig(true);
        String jsonString = JSON.toJSONString(dto, serializeConfig);
        Assertions.assertThat(jsonString).isEqualTo("{\"a\":1,\"b\":2,\"d\":4,\"c\":3}");

    }

    @Data
    private static class DTO {
        private Integer a = 1;
        private Integer b = 2;
        private Integer d = 4;
        private Integer c = 3;
    }
}
