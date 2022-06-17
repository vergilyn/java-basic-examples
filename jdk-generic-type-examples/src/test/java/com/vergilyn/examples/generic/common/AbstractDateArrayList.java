package com.vergilyn.examples.generic.common;

import org.springframework.core.ResolvableType;

import java.time.temporal.Temporal;
import java.util.ArrayList;

public class AbstractDateArrayList<T extends Temporal> extends ArrayList<T> {

	public Class<?> detectedActualGenericClass(){
		ResolvableType resolvableType = ResolvableType.forInstance(this);

		return resolvableType.getSuperType().getGeneric(0).resolve();
	}

}