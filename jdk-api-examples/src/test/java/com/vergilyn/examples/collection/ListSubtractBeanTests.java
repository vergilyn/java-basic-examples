package com.vergilyn.examples.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Objects;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 需要重写 {@linkplain Object#hashCode()} & {@linkplain Object#equals(Object)}
 * @author vergilyn
 * @since 2021-05-06
 */
public class ListSubtractBeanTests {
	/** [100, 1099] */
	private List<UserInfo> first;
	/** [100, 149] ∪ [200, 1199] */
	private List<UserInfo> second;

	@BeforeEach
	private void beforeEach(){
		first = buildList(100, 1000);

		second = buildList(100, 50);
		second.addAll(buildList(200, 1000));

		Collections.shuffle(first);
		Collections.shuffle(second);
	}

	@Test
	public void subtract(){
		Collection<UserInfo> subtract = CollectionUtils.subtract(second, first);
		System.out.printf("size: %d, subtract: %s", subtract.size(), subtract);

		// expected: [1100, 1199]
		List<UserInfo> expected = buildList(1100, 100);
		assertThat(subtract).containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	public void disjunction(){
		Collection<UserInfo> subtract = CollectionUtils.disjunction(second, first);
		System.out.printf("size: %d, disjunction: %s", subtract.size(), subtract);

		// expected: [150, 199] ∪ [1100, 1199]
		List<UserInfo> expected = buildList(150, 50);
		expected.addAll(buildList(1100, 100));

		assertThat(subtract).containsExactlyInAnyOrderElementsOf(expected);
	}

	private List<UserInfo> buildList(Integer seed, long limit){
		return Stream.iterate(new UserInfo(seed), prev -> new UserInfo(prev.userid + 1))
						.limit(limit)
						.collect(Collectors.toList());
	}

	@Setter
	@Getter
	private static class UserInfo {
		private Integer userid;

		public UserInfo(Integer userid) {
			this.userid = userid;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof UserInfo))
				return false;
			UserInfo userInfo = (UserInfo) o;
			return Objects.equal(userid, userInfo.userid);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(userid);
		}

		@Override
		public String toString() {
			return "UserInfo{" + "userid=" + userid + '}';
		}
	}
}
