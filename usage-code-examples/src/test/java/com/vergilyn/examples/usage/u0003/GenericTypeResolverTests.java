package com.vergilyn.examples.usage.u0003;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author vergilyn
 * @since 2022-06-02
 *
 * @see org.springframework.boot.SpringApplication#createSpringFactoriesInstances(Class, Class[], ClassLoader, Object[], Set)
 */
public class GenericTypeResolverTests{

	public static class AbstractDateArrayList<T extends Temporal> extends ArrayList<T>{

		public Class<?> detectedActualGenericClass(){
			ResolvableType resolvableType = ResolvableType.forInstance(this);

			return resolvableType.getSuperType().getGeneric(0).resolve();
		}

	}

	public static class LocalDateArrayList extends AbstractDateArrayList<LocalDate>{

		public static Class<?> forClass(){
			ResolvableType resolvableType = ResolvableType.forClass(LocalDateArrayList.class);

			return resolvableType.getSuperType().getGeneric(0).resolve();
		}
	}

	/**
	 * 个人：这种写法可以拿到 实际的泛型类型，应该是因为 泛型信息是在 LocalDateArrayList.class 上保存的有，所以可以获取。
	 *
	 * @see ResolvableType#forClass(Class)
	 */
	@Test
	public void forInstance(){
		LocalDateArrayList instance = new LocalDateArrayList();

		Assertions.assertEquals(LocalDate.class, instance.detectedActualGenericClass());

		System.out.println("actual-generic-class >>>> " + instance.detectedActualGenericClass().getName());
	}
	@Test
	public void forClass(){
		Class<?> clazz = LocalDateArrayList.forClass();

		Assertions.assertEquals(LocalDate.class, clazz);

		System.out.println("actual-generic-class >>>> " + clazz.getName());
	}

	/**
	 * 抽象的时候可能有场景会这么获取泛型。
	 *
	 * <p>
	 * 个人：
	 *   2022-06-02，貌似无法获取到真实的泛型`LocalDateTime.class`（特别是在 source = empty/null）
	 *
	 * @see org.springframework.boot.SpringApplication#createSpringFactoriesInstances(Class, Class[], ClassLoader, Object[], Set)
	 */
	@Test
	public void instance(){
		// TODO 2022-03-22 如何获取 真实类型 `LocalDateTime.class`？
		// 如果存在，可以通过 `source.get(0).getClass()`
		AbstractDateArrayList<LocalDateTime> source = newInstance();

		// java.lang.IllegalArgumentException: Instance must not be null
		ResolvableType resolvableType = ResolvableType.forInstance(source);

		System.out.println("getSuperType() >>>> " + resolvableType.getSuperType().toString());
	}

	private <T extends Temporal> AbstractDateArrayList<T> newInstance(){
		return new AbstractDateArrayList<T>();
	}

	@Test
	public void sample(){
		List<Integer> integerList = new ArrayList<Integer>();
		integerList.add(1);

		// 如果`!= null`，那么可以随便取一个值。 例如`list.get(0)` 或 `map.values[0]`
		Class<? extends Integer> clazz = integerList.get(0).getClass();
		Assertions.assertEquals(Integer.class, clazz);
	}

	/**
	 * SEE: spring-data `org.springframework.core.GenericTypeResolver`
	 * 以下写法只能是在  class内（ArrayList.class 内）
	 */
	@Test
	public void error(){
		List<Integer> integerList = new ArrayList<Integer>();

		ParameterizedType parameterizedType = ((ParameterizedType) integerList.getClass().getGenericSuperclass());

		Type[] types = parameterizedType.getActualTypeArguments();
		System.out.println(types[0]);
		Assertions.assertEquals(Integer.class, types[0]);
	}

}
