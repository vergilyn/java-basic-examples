package com.vergilyn.examples.collection;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

public class ListSortTest {

	@Test
	public void sort(){

		List<User> users = Stream.iterate(new User(1), user -> new User(user.getId() + 1))
				.limit(10).collect(Collectors.toList());

		Collections.shuffle(users);

		System.out.println("before >>>>" + JSON.toJSONString(users, true));

		List<Integer> sort = Lists.newArrayList(1, 3, 4, 5);

		// 若只是`s1 - s2`，当sort中不包含userId时，其会排在 users 最前面。
		users.sort(new Comparator<User>() {
			private int notIndexOf = sort.size();
			@Override
			public int compare(User o1, User o2) {
				int s1 = sort.indexOf(o1.getId());
				int s2 = sort.indexOf(o2.getId());

				s1 = s1 == -1 ? (++notIndexOf) : s1;
				s2 = s2 == -1 ? (++notIndexOf) : s2;

				return s1 - s2;
			}
		});

		System.out.println("sort >>>>" + JSON.toJSONString(users, true));

	}

	@Test
	public void pair(){
		List<User> users = Lists.newArrayList(new User(2), new User(1));

		System.out.println("before >>>>" + JSON.toJSONString(users));

		List<Integer> sort = Lists.newArrayList(1, 2);

		users.sort((o1, o2) -> {
			int s1 = sort.indexOf(o1.getId());
			int s2 = sort.indexOf(o2.getId());

			return s1 - s2;
		});

		System.out.println("sort >>>>" + JSON.toJSONString(users));
	}


	@Data
	@NoArgsConstructor
	private static class User implements Serializable{
		private Integer id;

		private String username;

		public User(Integer id) {
			this.id = id;
			this.username = "username-" + id;
		}
	}
}
