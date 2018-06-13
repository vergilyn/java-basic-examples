package com.vergilyn.examples.jvm;

import java.math.BigDecimal;

/**
 * 根据 参数的类型 去匹配方法声明/定义中，符合的方法；
 * 	<br>当未找到时，会根据 参数类型 的父类再去找可调用方法，找到则调用，否则一直找到顶层父类。
 * <br>example: 
 * <pre>1、当传入参数是null，会调用invoke(String)、invoke(Object)的哪一个？
 * 	<br>   会调用String，因为String是Object的子类，且Object特殊（调用时不会编译不通过）
 * </pre>
 * <pre>2、当传入参数是null，会调用invoke(String)、invoke(Integer)的哪一个？
 * 	<br>   编译不通过，因为无法确认调用！error: The method invoke(Integer) is ambiguous for the type SubclassInvoke
 * </pre> 
 * <pre>3、当声明/定义 int i = 123; Object o = i; 它们会调用什么；
 * 	<br>   i的调用层级：invoke(int) -> invoke(Integer) -> invoke(Number) -> invoke(Object) ； 
 * 	<br>   而o只会调用invoke(Object)； 
 *  <br>   所以，<b>变量的声明/定义类型决定了方法的调用！</b>
 * </pre> 
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2016/9/5
 * @see com.vergilyn.examples.generic.GenericLimit
 */
public class SubclassInvoke {
	public static void main(String[] args) {
		int iNum = 1234;
		Integer integer = new Integer(iNum);
		Double dNum = new Double(iNum);
		BigDecimal bigNum = new BigDecimal(iNum+"");
		String strNum = iNum+"";
		
		Object oInt = iNum;
		Number nNum = iNum;
		
		//调用方法不明确，因为Integer、Double、BigDecimal等都可以传null，无法确认调用哪个方法.
		/* 如果只剩其中之一  和Object，那么会调用更明确 subClass;即不会调用Object
		 */
//		invoke(null); //编译不通过error: The method invoke(Integer) is ambiguous for the type SubclassInvoke
	
		invoke(iNum);	//int; 未声明的调用层级 invoke(int) -> invoke(Integer) -> invoke(Number) -> invoke(Object)
		invoke(oInt);	//Object
		invoke(nNum);	//Number
		
		invoke(integer);//Integer
		/** 根据定义变量的类型，决定调用哪个方法；
		 * 	如果未找到，则找寻父类可调用方法，直到顶层；
		 */
		
		
	}
	
	private static void invoke(int param){
		System.out.println("int");
	}
	private static void invoke(Integer param){
		System.out.println("Integer");
	}
	private static void invoke(Double param){
		System.out.println("Double");
	}
	private static void invoke(String param){
		System.out.println("String");
	}
	private static void invoke(BigDecimal param){
		System.out.println("BigDecimal");
	}
	private static void invoke(Number param){
		System.out.println("Number");
	}
	private static void invoke(Object param){
		System.out.println("Object");
	}
	
}
