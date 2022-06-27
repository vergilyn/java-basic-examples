package com.vergilyn.examples.json.feature;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.TypeUtils;
import com.google.common.collect.Sets;
import com.vergilyn.examples.json.AbstractFastjsonTests;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Set;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;

/**
 * 相较于 {@link SeeAlsoTests }:
 * <p> 1. 不需要在 父类or父接口 中维护 {@link JSONType#seeAlso()}
 * <p> 2. 通过在 父类or父接口 定义 {@link JSONType#typeName()}，可以有效减少序列化后的json字符串大小。<br/>
 *   一定程度也避免了需要显示指定 {@link SerializerFeature#WriteClassName}
 *
 * <p> 3. <b>注意：</b> 不管是哪种写法，都需要维护 `typeName & targetClass` 的关系，只是如何维护而已！<br/>
 *   特别，相同的 typeName 也可能对应不同的 targetClass。比如 `typeName = dog`，
 *   在 A.class 表示 DogEntity.class，但在 B.class 表示 DogDTO.class。（具体根据 json的最外层对象决定）
 *
 *
 * <p>
 * <p> 2022-06-27，当子类增加时，手动维护父类的 {@link JSONType#seeAlso()} 或者 HashMap，<b>是最简单易懂且高效的</b>。
 * 否则，如果 通过父类 找到所有的子类，相对不是那么容易就可以实现。
 *
 * @author vergilyn
 * @since 2022-06-27
 */
public class SeeAlsoCustomTests extends AbstractFastjsonTests {
	private String _jsonString;

	@BeforeEach
	void beforeEach() {
		Dog dog1 = new Dog("dog-0001");
		Dog dog2 = new Dog("dog-0002");
		Cat cat = new Cat("cat-0001");

		AnimalDTO animalDTO = new AnimalDTO();
		animalDTO.setAnimals(Sets.newHashSet(dog1, dog2, cat));

		// 需要反序列化时输出`@type`
		_jsonString = JSON.toJSONString(animalDTO, PrettyFormat);
		System.out.println("origin-json >>>> " + _jsonString);
	}

	void afterDefault(TestInfo testInfo) {
		String prefix = testInfo.getDisplayName() != null ? testInfo.getDisplayName() : testInfo.getTestMethod().get().getName();

		AnimalDTO desDTO = JSON.parseObject(_jsonString, AnimalDTO.class);

		String desDtoJSONString = JSON.toJSONString(desDTO, true);
		System.out.println(prefix + " >>>> " + desDtoJSONString);

		Assertions.assertEquals(_jsonString, desDtoJSONString);
	}

	/**
	 * 源码主要参考：{@link ParserConfig#checkAutoType(String, Class, int)}, LINE: 1418 开始。
	 * 根据源码想到的几种解决方式。
	 * <pre>
	 *   - {@link #resolve_by_ParserConfig_autoTypeCheckHandler}, 推荐全局写法。
	 *   - {@link #resolve_by_ParserConfig_typeMapping}, 推荐独立配置写法。
	 *   - {@link #resolve_by_TypeUtils_Mapping}, <b>不推荐</b>。
	 *   - {@link #resolve_by_Deserializers_findClass}, 未找到如何写，但也不是很推荐。
	 * </pre>
	 *
	 * <pre>
	 * com.alibaba.fastjson.JSONException: autoType is not support. DOG
	 * 	at com.alibaba.fastjson.parser.ParserConfig.checkAutoType(ParserConfig.java:1542)
	 * 	at com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(JavaBeanDeserializer.java:823)
	 * 	at com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(JavaBeanDeserializer.java:291)
	 * 	at com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(JavaBeanDeserializer.java:287)
	 * 	at com.alibaba.fastjson.parser.deserializer.FastjsonASMDeserializer_1_AnimalDTO.deserialze(Unknown Source)
	 * 	at com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(JavaBeanDeserializer.java:287)
	 * 	at com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(DefaultJSONParser.java:705)
	 * 	at com.alibaba.fastjson.JSON.parseObject(JSON.java:394)
	 * 	at com.alibaba.fastjson.JSON.parseObject(JSON.java:298)
	 * 	at com.alibaba.fastjson.JSON.parseObject(JSON.java:588)
	 * 	at com.vergilyn.examples.json.feature.SeeAlsoCustomTests.test(SeeAlsoCustomTests.java:46)
	 * </pre>
	 *
	 */
	@Test
	public void exception(TestInfo testInfo){
		// 默认会解析异常

		afterDefault(testInfo);
	}

	/**
	 * 相对于{@link #resolve_by_ParserConfig_typeMapping()}, `autoTypeCheckHandler` 适合写成 {@link ParserConfig#getGlobalInstance()}。
	 * 因为 存在 `typeName & expectClass`，可以区分出 相同typeName 需要的不同的class。
	 */
	@Test
	public void resolve_by_ParserConfig_autoTypeCheckHandler(TestInfo testInfo){
		// 全局写法。  当然也可以 独立配置。
		//
		ParserConfig globalInstance = ParserConfig.getGlobalInstance();
		globalInstance.addAutoTypeCheckHandler(new ParserConfig.AutoTypeCheckHandler() {
			@Override
			public Class<?> handler(String typeName, Class<?> expectClass, int features) {
				if (expectClass != Animal.class){
					return null;
				}

				if (AnimalEnum.DOG.toString().equals(typeName)){
					return Dog.class;
				}

				if (AnimalEnum.CAT.toString().equals(typeName)){
					return Cat.class;
				}

				return null;
			}
		});


		afterDefault(testInfo);
	}

	/**
	 * <p> 1. <b>不推荐 全局设置 的方式</b>；原因类似 {@link #resolve_by_TypeUtils_Mapping}
	 * <p> 2. 相对推荐 单独指定。
	 * <p>
	 * <p> <b>不友好的地方</b>：反序列化时，需要单独指定 {@link ParserConfig}，否则会导致反序列化异常。
	 * <p> <b>解决方式</b>: 可以将 parser写到 `AnimalDTO.class`中，这样更友好。<br/>
	 *   但是{@link AnimalDTO} 中要如何维护 `typeName & targetClass` 的映射关系 需要考虑一下。
	 *   （不希望每增加一个子类，还要在手动去 AnimalDTO 维护一个Map。也绝对不能维护到{@link AnimalEnum} 中）
	 *
	 *
	 * @see #resolve_by_ParserConfig_typeMapping()
	 */
	@Test
	public void resolve_by_ParserConfig_typeMapping(){
		// 全局设置。 不推荐
		// ParserConfig.getGlobalInstance().register(AnimalEnum.DOG.toString(), Dog.class);
		// ParserConfig.getGlobalInstance().register(AnimalEnum.CAT.toString(), Cat.class);

		// 相对推荐：单独指定。
		ParserConfig parserConfig = new ParserConfig();
		parserConfig.register(AnimalEnum.DOG.toString(), Dog.class);
		parserConfig.register(AnimalEnum.CAT.toString(), Cat.class);

		AnimalDTO desDTO = JSON.parseObject(_jsonString, AnimalDTO.class, parserConfig);
		String desDtoJSONString = JSON.toJSONString(desDTO, true);

		System.out.println("resolve_by_ParserConfig_typeMapping >>>> " + desDtoJSONString);

		Assertions.assertEquals(_jsonString, desDtoJSONString);
	}

	/**
	 *
	 * <b>不推荐</b>，原因：
	 * <p> 1. {@link TypeUtils#mappings} 是fastjson全局的。
	 * <p> 2. 业务项目中，可能针对 不同的java-bean，相同的`typeName`实际上对应很多不同的class。
	 *
	 * <p> 参考：{@link ParserConfig#checkAutoType(String, Class, int)}, LINE: 1418。
	 */
	@Test
	public void resolve_by_TypeUtils_Mapping(TestInfo testInfo){
		TypeUtils.addMapping(AnimalEnum.DOG.toString(), Dog.class);
		TypeUtils.addMapping(AnimalEnum.CAT.toString(), Cat.class);

		afterDefault(testInfo);
	}

	/**
	 * TODO 2022-06-27，没想到要怎么实现。
	 * @see IdentityHashMap#findClass(String)
	 */
	@Test
	public void resolve_by_Deserializers_findClass(TestInfo testInfo){
		IdentityHashMap<Type, ObjectDeserializer> deserializers = ParserConfig.getGlobalInstance().getDeserializers();

		afterDefault(testInfo);
	}

	@Data
	public static class AnimalDTO implements Serializable {
		private String name = "animal-dto";

		private Set<Animal> animals;
	}

	public static enum AnimalEnum {
		DOG, CAT;
	}

	@Data
	@JSONType(/* seeAlso={Dog.class, Cat.class},  */typeKey = Animal.FASTJSON_TYPE_KEY)
	public static abstract class Animal implements Serializable{
		public static final String FASTJSON_TYPE_KEY = "_animal_type_";

		@JSONField(name = FASTJSON_TYPE_KEY)
		public abstract AnimalEnum getAnimalType();
	}

	@Data
	@NoArgsConstructor
	// @JSONType(typeName = "dOG")
	public static class Dog extends Animal {
		public String dogName;

		public Dog(String dogName) {
			this.dogName = dogName;
		}

		@Override
		public AnimalEnum getAnimalType() {
			return AnimalEnum.DOG;
		}
	}

	@Data
	@NoArgsConstructor
	// @JSONType(typeName = "cAT")
	public static class Cat extends Animal {
		public String catName;

		public Cat(String catName) {
			this.catName = catName;
		}

		@Override
		public AnimalEnum getAnimalType() {
			return AnimalEnum.CAT;
		}
	}
}
