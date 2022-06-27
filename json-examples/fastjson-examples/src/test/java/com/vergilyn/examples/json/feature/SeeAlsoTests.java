package com.vergilyn.examples.json.feature;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Sets;
import com.vergilyn.examples.json.AbstractFastjsonTests;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Set;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;
import static com.alibaba.fastjson.serializer.SerializerFeature.WriteClassName;

/**
 * <p> 1. <a href="https://github.com/alibaba/fastjson/wiki/JSONType_seeAlso_cn">JSONType_seeAlso_cn</a>
 * <p> 2. <a href="https://github.com/alibaba/fastjson/wiki/JSONType_typeKey_cn">JSONType_typeKey_cn</a>
 *
 * <p> 3. fastjson2，<a href="https://github.com/alibaba/fastjson2/wiki/fastjson2_autotype_cn">fastjson2_autotype_cn</a>
 *
 * <p>
 * AutoType存在较多漏洞的可能性。推荐细看：<a href="https://github.com/alibaba/fastjson/wiki/enable_autotype">fastjson1, enable_autotype</a>
 *
 * @author vergilyn
 * @since 2022-06-27
 */
public class SeeAlsoTests extends AbstractFastjsonTests {

	/**
	 * <p> 1. 序列化时，需要{@link SerializerFeature#WriteClassName}。
	 * <br/> 同时，可以通过 {@link SerializerFeature#NotWriteRootClassName} 减少不必要的序列化信息。
	 *
	 * <p> 2. 通过 {@link JSONType#typeName()} 可以自定义`@type`序列化时的输出值。
	 *   通过{@link JSONType#typeKey()} 指定AutoType 的key-name。
	 *
	 * <p> 3. 代码不友好，<b>父类or父接口，需要通过{@link JSONType#seeAlso()} 指定子类。</b>
	 *  当需要增加子类时，需要维护此代码。
	 */
	@Test
	public void test(){
		Dog dog1 = new Dog("dog-0001");
		Dog dog2 = new Dog("dog-0002");
		Cat cat = new Cat("cat-0001");

		AnimalDTO animalDTO = new AnimalDTO();
		animalDTO.setAnimals(Sets.newHashSet(dog1, dog2, cat));

		// 需要反序列化时输出`@type`
		String jsonStr = JSON.toJSONString(animalDTO, PrettyFormat, WriteClassName);
		System.out.println("serial >>>> " + jsonStr);

		AnimalDTO desDTO = JSON.parseObject(jsonStr, AnimalDTO.class);
		System.out.println("desDTO >>>> " + JSON.toJSONString(desDTO, true));

	}

	@Data
	public static class AnimalDTO implements Serializable {
		private String name = "animal-dto";

		private Set<Animal> animals;
	}

	@Data
	@JSONType(seeAlso={Dog.class})
	public static abstract class Animal implements Serializable{
		private String name = "animal";

	}

	@Data
	@NoArgsConstructor
	@JSONType(typeName = "dog")
	public static class Dog extends Animal {
		public String dogName;

		public Dog(String dogName) {
			this.dogName = dogName;
		}
	}

	@Data
	@NoArgsConstructor
	@JSONType(typeName = "cat")
	public static class Cat extends Animal {
		public String catName;

		public Cat(String catName) {
			this.catName = catName;
		}
	}
}
