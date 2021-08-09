package com.vergilyn.examples.jvm.allocate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

/**
 * 假设有任意数量足够多的 {@code List<Person>}，其中{@linkplain Person#encrypt}字段是加密的，
 * 现在需要通过 {@code local#decrypt()} 或 {@code rpc#decrypt()} 进行批量解密，但只支持每次最多 1000。
 *
 * 思路，先按1000分组得到{@code List<List<String>>}，验证此过程是否会产生多1倍的encrypt对象？
 *
 * @author vergilyn
 * @since 2021-08-05
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StreamAllocateObjectTests {
	private static final int LIMIT = 1_000_000;
	private static final int GROUP_COUNT = 1000;

	List<Person> persons;

	@BeforeAll
	public void beforeAll(){
		persons = Stream.generate(Person::random).limit(LIMIT).collect(Collectors.toList());
	}

	/**
	 * 可以通过 debug，查看memory中的 Person/String 对象数量没有成倍增长，所以并没有产生额外对象。
	 */
	@Test
	public void allocate(){
		List<List<Person>> partitions = Lists.partition(persons, GROUP_COUNT);

		// true: 证明`persons`和`partitions`内都是指向相同的 Person 对象，并未额为产生 Person 对象。
		Assertions.assertThat(persons.get(0) == partitions.get(0).get(0)).isTrue();
		Assertions.assertThat(persons.get(0).encrypt == partitions.get(0).get(0).encrypt).isTrue();

	}

	/**
	 * `stream#map()`前，memory中 Person、String(char[]) 都满足期望的 {@linkplain #LIMIT}。
	 * 执行完`stream#map()`均没有出现成倍的增长。<br/>
	 * 所以：以下`stream#map()` memory中并未产生成倍的String对象。
	 */
	@Test
	public void map(){
		List<String> encrypts = persons.stream().map(Person::getEncrypt).collect(Collectors.toList());

		Assertions.assertThat(persons.get(0).encrypt == encrypts.get(0));
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Person {
		private String encrypt;

		public static Person random(){
			return new Person(UUID.randomUUID().toString());
		}
	}


}
