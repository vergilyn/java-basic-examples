package com.vergilyn.examples.guava.base;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 参考：`guava-retrying, RetryBuilder.java`
 *
 * @author vergilyn
 * @since 2023-02-13
 */
public class PredicatesTests {
	final List<User> _users = Lists.newArrayList(
			new User("wang", 24),
			new User("chen", 26),
			new User("sun", 24));

	@Test
	public void test() {
		// 保留userName 是`chen`的user
		Predicate<User> username = user -> StringUtils.equalsIgnoreCase("chen", user.userName);

		// 保留age 不为26的user
		Predicate<User> age = input -> input.age != 26;

		Predicate<User> predicate = Predicates.and(username, age);

		// Predicates.in();
		// Predicates.instanceOf();
	}

	@Data
	static class User {
		private String userName;

		private int age;

		public User(String userName, int age) {
			this.userName = userName;
			this.age = age;
		}
	}

}
