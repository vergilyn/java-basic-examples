package com.vergilyn.examples.reflect;

import com.alibaba.fastjson.JSON;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 动态设置 java-annotation 的属性值。
 *
 * <p> SpEL: 并不是动态的，而是在<b>使用时</b>，在解析占位符获取真实的值。
 *
 *
 * @author vergilyn
 * @since 2022-02-25
 */
public class DynamicSetAnnotationPropertyTests {
	private final Person person = new Person();

	@AfterEach
	public void test(){
		Person.printAnnotation("target ", person);
		System.out.println();
		Person.printAnnotation("another", new Person());
	}

	/**
	 * 1. 有看到说，有缺陷，还是可能获取到`init-annotation-value`. <br/>
	 * 2. <b>如下，并不是 class-object级别，还是针对的 class-method！</b> <br/>
	 * <pre>
	 * [target ][class ] CsvFileSource.resources[0] = dynamic-set-by-reflect
	 * [target ][object] CsvFileSource.resources[0] = dynamic-set-by-reflect
	 *
	 * [another][class ] CsvFileSource.resources[0] = dynamic-set-by-reflect
	 * [another][object] CsvFileSource.resources[0] = dynamic-set-by-reflect
	 * </pre>
	 */
	@SneakyThrows
	@Test
	public void setByReflect(){
		Class<? extends Person> clazz = person.getClass();
		Method method = clazz.getMethod(Person.ANNOTATION_METHOD_NAME);

		CsvFileSource csvFileSource = method.getAnnotation(CsvFileSource.class);
		InvocationHandler invocationHandler = Proxy.getInvocationHandler(csvFileSource);
		Field field = invocationHandler.getClass().getDeclaredField("memberValues");
		field.setAccessible(true);

		Map memberValues = (Map) field.get(invocationHandler);
		memberValues.put("resources", new String[]{"dynamic-set-by-reflect"});
	}

	/**
	 * <a href="https://blog.csdn.net/jly4758/article/details/44774217">javassist---动态修改注解</a>
	 *
	 * <p>个人：貌似针对 整个class的，无法精确到 class-object。
	 */
	@Test
	@SneakyThrows
	public void setByJavassist(){
		Class<? extends Person> clazz = person.getClass();
		String classname = clazz.getName();
		final String dynamicAnnotationValue = "set-by-javassist";

		ClassPool classPool = ClassPool.getDefault();
		CtClass ctClass = classPool.get(classname);

		CtMethod method = ctClass.getDeclaredMethod(Person.ANNOTATION_METHOD_NAME);
		MethodInfo methodInfo = method.getMethodInfo();
		ConstPool methodInfoConstPool = methodInfo.getConstPool();

		AttributeInfo attribute = methodInfo.getAttribute(AnnotationsAttribute.visibleTag);
		AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) attribute;

		Annotation csvFileSourceAnnotation = annotationsAttribute.getAnnotation(CsvFileSource.class.getName());

		StringMemberValue stringMemberValue = new StringMemberValue(dynamicAnnotationValue, methodInfoConstPool);
		csvFileSourceAnnotation.addMemberValue("resources", new ArrayMemberValue(stringMemberValue, methodInfoConstPool));

		// FIXME 2022-02-28 上述确实修改了，但后续获取注解值时，并不是此修改后的值
		System.out.println("fixme....");
	}

	private void setBySpring(Person person){
		Method method = ReflectionUtils.findMethod(person.getClass(), "method");

		CsvFileSource annotation = AnnotationUtils.getAnnotation(method, CsvFileSource.class);

		Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);

		System.out.println(JSON.toJSONString(annotationAttributes));

		annotationAttributes.put("resources", new String[]{"dynamic-set-by-spring"});

	}

	private static class Person{
		private static final String ANNOTATION_METHOD_NAME = "method";

		@CsvFileSource(resources = "init-annotation-value")
		public void method(){

		}

		@SneakyThrows
		public static void printAnnotation(String prefix, Person person){
			printClassAnnotation(prefix);
			printObjectAnnotation(prefix, person);

		}

		@SneakyThrows
		private static void printClassAnnotation(String prefix){
			Method method = Person.class.getMethod(ANNOTATION_METHOD_NAME);

			CsvFileSource annotation = method.getAnnotation(CsvFileSource.class);
			String[] resources = annotation.resources();
			System.out.printf("[%s][class ] CsvFileSource.resources[0] = %s \n", prefix, resources[0]);
		}

		@SneakyThrows
		private static void printObjectAnnotation(String prefix, Person person){
			Method method = person.getClass().getMethod(ANNOTATION_METHOD_NAME);

			CsvFileSource annotation = method.getAnnotation(CsvFileSource.class);
			String[] resources = annotation.resources();
			System.out.printf("[%s][object] CsvFileSource.resources[0] = %s \n", prefix, resources[0]);
		}
	}
}
