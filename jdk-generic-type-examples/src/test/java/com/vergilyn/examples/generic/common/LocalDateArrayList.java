package com.vergilyn.examples.generic.common;

import org.springframework.core.ResolvableType;

import java.time.LocalDate;

public class LocalDateArrayList extends AbstractDateArrayList<LocalDate> {

	public static Class<?> forClass(){
		ResolvableType resolvableType = ResolvableType.forClass(LocalDateArrayList.class);

		return resolvableType.getSuperType().getGeneric(0).resolve();
	}
}