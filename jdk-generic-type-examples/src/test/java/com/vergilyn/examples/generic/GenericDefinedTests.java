package com.vergilyn.examples.generic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericDefinedTests {

	/**
	 * @see <a href="https://www.jb51.net/article/218911.htm">了解Java泛型的super和extends</a>
	 */
	@Test
	public void test(){
		Biological biological = new Biological();

		Animal animal = new Animal();
		Dog dog = new Dog();
		Cat cat = new Cat();

		Plant plant = new Plant();
		Flower flower = new Flower();
		Tree tree = new Tree();

		List<? super Animal> superAnimal = new ArrayList<>();

		// ERROR: capture of ? super Animal
		// superAnimal.add(biological);

		superAnimal.add(animal);
		superAnimal.add(dog);
		superAnimal.add(cat);

		// ERROR: capture of ? super Animal
		// superAnimal.add(flower);

		Object object = superAnimal.get(0);
		//listB.add(new Plant());
		//listB.add(new Flower());

		List<? extends Animal> extendsAnimal = new ArrayList<>();
		// extendsAnimal.add(dog);

	}

	/**
	 * 生物
	 */
	static class Biological{
	}

	/**
	 * 动物
	 */
	static class Animal extends Biological{

	}
	/**
	 * 植物
	 */
	static class Plant extends Biological{
	}

	static class Dog extends Animal{
	}
	static class Cat extends Animal{
	}
	static class Flower extends Plant{
	}
	static class Tree extends Plant{
	}

}
