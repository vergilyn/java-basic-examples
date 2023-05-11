package com.vergilyn.examples.collection;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author vergilyn
 * @see <a href="https://mp.weixin.qq.com/s/jiGO9rw2Tic9MUakPAyeKQ">使用 Lambda 表达式实现超强的排序功能</a>
 * @since 2021-11-19
 */
public class ListSortTest {

	@Test
	public void basic() {
		ArrayList<Integer> list = Lists.newArrayList(1, 3, 2, 3, 5, 4);

		// 正序
		list.sort(Integer::compareTo);
		// list.sort((o1, o2) -> o1.compareTo(o2));
		System.out.println("asc >>>> " + list);

		// 倒序
		// list.sort((o1, o2) -> o2.compareTo(o1));
		list.sort(Comparator.reverseOrder());
		System.out.println("desc >>>> " + list);

		User earliest = new User(LocalDate.of(2021, 11, 11));
		User latest = new User(LocalDate.of(2021, 11, 22));
		List<User> dates = Lists.newArrayList(earliest, latest, earliest, latest, latest, earliest);

		// 正序
		dates.sort(Comparator.comparing(User::getDate));
		System.out.println("asc >>>> " + dates);

		// 倒序。 看个人习惯
		// dates.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
		dates.sort(Comparator.comparing(User::getDate, Comparator.reverseOrder()));
		System.out.println("desc >>>> " + dates);

	}

	@Test
	public void sort() {

		List<User> users = Stream.iterate(new User(1), user -> new User(user.getId() + 1))
								.limit(5)
								.collect(Collectors.toList());

		Collections.shuffle(users);

		System.out.println("before >>>>" + JSON.toJSONString(users, true));

		users.sort(Comparator.comparing(User::getUsername));

		System.out.println("after >>>>" + JSON.toJSONString(users, true));



		// 指定部分，存在NULL的情况
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

	/**
	 * 根据指定 顺序重新排序。
	 */
	@Test
	public void assignSort() {
		List<User> users = Stream.iterate(new User(1), user -> new User(user.getId() + 1)).limit(5).collect(
				Collectors.toList());
		Collections.shuffle(users);
		System.out.println("before >>>>" + JSON.toJSONString(users));

		// 期望的顺序
		List<Integer> assignSort = Lists.newArrayList(3, 5, 1, 4, 2);

		users.sort((o1, o2) -> {
			int s1 = assignSort.indexOf(o1.getId());
			int s2 = assignSort.indexOf(o2.getId());

			return s1 - s2;
		});

		System.out.println("assignSort >>>>" + JSON.toJSONString(users));
	}

	@Test
	public void nullFirstLast(){
		List<User> users = Lists.newArrayList(null, new User(2), null);

		users.sort(Comparator.nullsLast(Comparator.comparing(User::getId)));

		System.out.println("nullLast >>>> " + JSON.toJSONString(users));
	}

	@Data
	@NoArgsConstructor
	private static class User implements Serializable {
		private Integer id;

		private String username;

		private LocalDate date;

		public User(Integer id) {
			this.id = id;
			this.username = "username" + id;
		}

		public User(LocalDate date) {
			this.date = date;
		}

		@Override
		public String toString() {
			return JSON.toJSONString(this);
		}
	}
}
