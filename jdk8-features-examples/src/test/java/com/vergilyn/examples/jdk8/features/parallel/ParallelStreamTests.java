package com.vergilyn.examples.jdk8.features.parallel;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vergilyn.examples.jdk8.features.Person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

/**
 * 1_000_000:
 *  REPEATED_COUNT = 1, stream = 340ms, parallelStream = 200ms
 *  REPEATED_COUNT = 2, stream = 742ms, parallelStream = 831ms
 *
 * 如果`REPEATED_COUNT >= 2`，可能`parallel`比`stream`更耗时，
 * 猜测是因为parallel是多线程，内部线程管理比`threadSafeHandle`更耗时造成。
 *
 * @author vergilyn
 * @since 2021-08-05
 */
public class ParallelStreamTests {
	private static final int REPEATED_COUNT = 2;
	private static final int LIMIT = 1_000_000;

	List<Person> persons = null;

	@BeforeEach
	public void beforeEach(){
		persons = Stream.iterate(new Person(0L, "name", "A")
									, person -> new Person(person.getId() + 1, person.getName(), person.getType()))
				.limit(LIMIT)
				.collect(Collectors.toList());
	}

	@RepeatedTest(REPEATED_COUNT)
	public void stream(){
		for (Person person : persons) {
			threadSafeHandle(person);
		}
	}

	@RepeatedTest(REPEATED_COUNT)
	public void parallelStream(){
		persons.parallelStream().forEach(this::threadSafeHandle);
	}

	private void threadSafeHandle(Person person){
		person.setName(person.getName() + "-" + person.getId());
	}
}
