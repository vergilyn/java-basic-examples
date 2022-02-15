package com.vergilyn.examples.jdk8.features.stream;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupSumTests {

	@Test
	public void groupSum(){
		// `sum(count) GROUP BY id, type`, 且用`List<DataEntity>`作为返回对象
		List<DataEntity> datas = new ArrayList<DataEntity>(){{
			// expected: A = 3, B = 4, C = 10
			add(new DataEntity("A", 1));
			add(new DataEntity( "A", 2));
			add(new DataEntity( "B", 3));
			add(new DataEntity( "B", 1));
			add(new DataEntity( "C", 10));
		}};

		Collections.shuffle(datas);

		// 结果正确，但是 数据结构不是期望的`List<DataEntity>`
		Map<String, Integer> m1 = datas.stream().collect(
				Collectors.groupingBy(DataEntity::getType, Collectors.summingInt(DataEntity::getCount)));
		System.out.println(JSON.toJSONString(m1));

		// 1. 先分组， 再求和  (感觉效率不高，但容易理解 TODO 2022-02-11 待优化)
		Map<String, List<DataEntity>> m2 = datas.stream().collect(
				Collectors.groupingBy(DataEntity::getType, Collectors.toList()));
		List<DataEntity> r2 = m2.values().stream().flatMap(dataEntities -> {
			DataEntity dataEntity = new DataEntity();

			for (DataEntity entity : dataEntities) {
				dataEntity.setType(entity.getType());
				dataEntity.setCount(dataEntity.count + entity.count);
			}

			return Stream.of(dataEntity);
		}).collect(Collectors.toList());
		System.out.println(JSON.toJSONString(r2));

	}

	@Getter
	@Setter
	private static class DataEntity{
		private String type;

		private Integer count = 0;

		public DataEntity() {
		}

		public DataEntity(String type, Integer count) {
			this.type = type;
			this.count = count;
		}
	}
}
