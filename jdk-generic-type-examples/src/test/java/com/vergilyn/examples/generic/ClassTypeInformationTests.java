package com.vergilyn.examples.generic;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vergilyn.examples.generic.common.AbstractDateArrayList;
import com.vergilyn.examples.generic.common.LocalDateArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.support.DefaultRepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryComposition;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <b>期望：</b>获取 Class 上定义的泛型。
 *
 * mybatis-plus / spring-data 最终都是 {@link ResolvableType}
 *
 * @author vergilyn
 * @since 2022-06-15
 *
 * @see org.springframework.core.ResolvableType
 * @see org.springframework.data.util.ClassTypeInformation
 */
@SuppressWarnings("JavadocReference")
public class ClassTypeInformationTests {

	/**
	 * mybatis-plus 主要参考：<br/>
	 * - {@link ServiceImpl#currentModelClass()} <br/>
	 * - {@link ServiceImpl#currentMapperClass()} <br/>
	 *
	 * <b>其底层核心主要还是基于`spring`的 {@link GenericTypeResolver#resolveTypeArguments(Class, Class)}。</b>
	 * 然后其spring核心：{@link ResolvableType}
	 *
	 * <p>
	 * <h3>特别</h3>
	 * <p> 1. {@link ResolvableType} 内部不一定都会使用 {@link ResolvableType#cache}，具体要看实际的方法：<br/>
	 *  - {@link ResolvableType#forClass(Class)}, 不存在map-cache。<br/>
	 *  - {@link ResolvableType#forType(Type)}, 存在map-cache。 <br/>
	 */
	@Test
	@SuppressWarnings("rawtypes")
	public void mybatisPlus(){
		Class<LocalDateArrayList> thisClass = LocalDateArrayList.class;
		Class<AbstractDateArrayList> genericClass = AbstractDateArrayList.class;

		Class<?>[] generics = GenericTypeResolver.resolveTypeArguments(thisClass, genericClass);

		// 等价于
		// ResolvableType resolvableType = ResolvableType.forClass(thisClass).as(genericClass);
		// Class<?>[] springGeneric = resolvableType.resolveGenerics(Object.class);

		System.out.println(Arrays.stream(generics).map(Class::getName).collect(Collectors.joining()));

		Assertions.assertEquals(LocalDate.class, generics[0]);
	}

	/**
	 * {@link SimpleJpaRepository#getDomainClass()}:
	 * <pre>
	 *   - {@link RepositoryFactorySupport#getRepository(Class, RepositoryComposition.RepositoryFragments)}
	 *   - {@link RepositoryFactorySupport#getRepositoryMetadata(Class)}
	 *   - {@link DefaultRepositoryMetadata#DefaultRepositoryMetadata(Class)}
	 *   - <b>core: </b> {@link ClassTypeInformation#from(Class)}
	 *   - {@link GenericTypeResolver#resolveType(Type, Map)}
	 * </pre>
	 *
	 * 1. {@link ClassTypeInformation#CACHE} 存在 map-cache。
	 */
	@Test
	public void springData(){
		Class<LocalDateArrayList> thisClass = LocalDateArrayList.class;
		Class<AbstractDateArrayList> genericClass = AbstractDateArrayList.class;

		// spring-data-jpa 获取 类似`CustomerRepository extends CrudRepository<Customer, Long>`
		// `entity-class = Customer.class` 的核心代码。
		List<TypeInformation<?>> arguments = ClassTypeInformation.from(thisClass)
				.getRequiredSuperTypeInformation(genericClass)
				.getTypeArguments();

		System.out.println(arguments.stream().map(typeInformation -> typeInformation.getType().getName()).collect(Collectors.joining()));

		Assertions.assertEquals(LocalDate.class, arguments.get(0).getType());
	}
}
