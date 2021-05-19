package com.vergilyn.examples.comparable;

import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * `easy-rules, Rules`中看到的{@linkplain Comparable}特性，目的：
 *   在使用{@linkplain Set#add(Object)}时按“优先级”排序。
 * @author vergilyn
 * @since 2021-05-19
 */
class ComparableFeatureTests {
	final UserInfo u1 = new UserInfo(1);
	final UserInfo u2 = new UserInfo(2);
	final UserInfo u3 = new UserInfo(3);

	@Test
	void lists(){
		List<UserInfo> lists = Lists.newArrayListWithCapacity(3);
		lists.add(u3);
		lists.add(u1);
		lists.add(u2);

		System.out.println(JSON.toJSONString(lists));
		assertThat(lists).containsExactly(u3, u1, u2);
	}

	@Test
	void treeSet(){
		Set<UserInfo> sets = Sets.newTreeSet();
		sets.add(u3);
		sets.add(u1);
		sets.add(u2);

		System.out.println(JSON.toJSONString(sets));
		assertThat(sets).containsExactly(u1, u2, u3);
	}

	@Test
	void hashSet(){
		Set<UserInfo> sets = Sets.newHashSet();
		sets.add(u3);
		sets.add(u1);
		sets.add(u2);

		System.out.println(JSON.toJSONString(sets));
		assertThat(sets).containsExactly(u1, u2, u3);
	}

	@EqualsAndHashCode
	private static class UserInfo implements Comparable<UserInfo>{
		@Getter
		@EqualsAndHashCode.Include()
		private final int priority;

		public UserInfo(int priority) {
			this.priority = priority;
		}

		@Override
		public int compareTo(UserInfo other) {
			return other == null ? 0 : this.priority - other.priority;
		}
	}
}
