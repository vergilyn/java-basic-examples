package com.vergilyn.examples;

import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.Test;

public class BeanCloneTests {

	@SneakyThrows
	@Test
	public void test(){
		Child child = new Child();
		child.setUsername("child-01");

		Person person = new Person();
		person.setUsername("person-01");
		person.setNumber(1);
		person.setChild(child);

		Person cloneBean = (Person) BeanUtils.cloneBean(person);

		System.out.println(cloneBean);
	}

	@Data
	public static class Person {
		private String username;

		private Integer number;

		private Child child;

	}

	@Data
	public static class Child {
		private String username;
	}
}
