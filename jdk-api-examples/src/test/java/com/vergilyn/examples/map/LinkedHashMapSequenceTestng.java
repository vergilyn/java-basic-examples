package com.vergilyn.examples.map;

import java.util.LinkedHashMap;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.Test;

public class LinkedHashMapSequenceTestng {

	/**
	 * LinkedHashMap#keySet() 是否可以保证有序：<b>可以保证有序</b>
	 */
	@Test
	public void seq(){
		int len = 10;
		LinkedHashMap<Integer, Integer> linked = Maps.newLinkedHashMapWithExpectedSize(len);

		System.out.println("init: ");
		int five = 0;
		for (int i = 0; i < len; i++){
			int key = RandomUtils.nextInt(0, 100);
			linked.put(key, i);
			System.out.print(key + ", ");
			if (i == 5){
				five = key;
			}
		}

		System.out.println();

		System.out.println("keySet: ");
		linked.remove(five);
		for (Integer key : linked.keySet()){
			System.out.print(key + ", ");
		}
	}
}
