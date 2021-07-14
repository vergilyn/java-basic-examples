package com.vergilyn.examples.jdk8.features.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

public class ListFlatMapTests {

	@Test
	public void flatmap(){
		List<User> users = Lists.newArrayList();
		users.add(new User(1, 10));
		users.add(new User(2, 20));

		List<Integer> result = users.stream()
				.flatMap(user -> Stream.of(user.userId, user.age))
				.collect(Collectors.toList());

		System.out.println(result);
	}

	@Data
	@NoArgsConstructor

	private static class User{
		private Integer userId;

		private Integer age;

		public User(Integer userId, Integer age) {
			this.userId = userId;
			this.age = age;
		}
	}
}
