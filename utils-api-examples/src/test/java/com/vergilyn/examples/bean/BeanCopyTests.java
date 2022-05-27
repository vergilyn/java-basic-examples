package com.vergilyn.examples.bean;

import org.springframework.cglib.core.Converter;

import java.beans.PropertyDescriptor;

/**
 * 常见方式：
 * <pre>
 * - 硬编码setter/getter
 * - {@linkplain org.apache.commons.beanutils.BeanUtils}
 * - {@linkplain org.springframework.beans.BeanUtils}
 * - {@linkplain org.springframework.cglib.beans.BeanCopier}
 * - {@linkplain net.sf.cglib.beans.BeanCopier}
 * - {@linkplain org.mapstruct.factory.Mappers}
 * </pre>
 *
 * 性能，从高到低：
 * <pre>
 *   硬编码 > (BeanCopier = MapStruct) > spring-BeanUtils > apache-BeanUtils。
 * </pre>
 *
 * 使用简便程度，从简单到复杂（MapStruct没实际用过，但是感觉挺麻烦的）：
 * <pre>
 *   BeanUtils > BeanCopier > 硬编码 > MapStruct
 * </pre>
 *
 * <p>
 * <h3>备注</h3>
 * <p>1. BeanCopier/MapStruct 内部原理都是 <b>生成class，接近 硬编码</b>!
 *
 * <p>2.{@linkplain org.springframework.cglib.beans.BeanCopier#create(Class, Class, boolean)} 其实相当耗时，
 * 所以建议全局初始化一次，{@linkplain org.springframework.cglib.beans.BeanCopier#copy(Object, Object, Converter)}
 * 已经保证了线程安全。
 *
 * @author vergilyn
 * @since 2022-05-25
 *
 * @see <a href="https://dude6.com/article/346320.html">Java Bean Copy框架性能对比</a>

 */
public class BeanCopyTests {

	/**
	 * spring/apache BeanUtils 内部实现原理都是类似，
	 * 都是通过{@linkplain java.beans.PropertyDescriptor} 找到 read/write {@linkplain java.lang.reflect.Method}，
	 * 然后再{@linkplain java.lang.reflect.Method#invoke(Object, Object...)}.
	 *
	 * <p>
	 * <h3>{@linkplain org.springframework.beans.BeanUtils}</h3>
	 * <p>1. 获取 class 的`PropertyDescriptor[]` {@linkplain org.springframework.beans.BeanUtils#getPropertyDescriptors(Class)}
	 *   <br/>（内部有 map-cache 机制）
	 *
	 * <p>2. 获取 read/write Method <br/>
	 *  - {@linkplain PropertyDescriptor#getReadMethod()} <br/>
	 *  - {@linkplain PropertyDescriptor#getWriteMethod()} <br/>
	 *
	 * <p>
	 * <h3>{@linkplain org.apache.commons.beanutils.BeanUtils}</h3>
	 * <p>1. 获取 class 的`PropertyDescriptor[]` {@linkplain org.apache.commons.beanutils.PropertyUtilsBean#getPropertyDescriptors(Class)}
	 *
	 * <p>2. 获取 read/write Method <br/>
	 *  - {@linkplain org.apache.commons.beanutils.PropertyUtilsBean#getReadMethod(PropertyDescriptor)} <br/>
	 *  - {@linkplain org.apache.commons.beanutils.PropertyUtilsBean#getWriteMethod(PropertyDescriptor)} <br/>
	 */
	public void beanUtils(){

	}
}
