package com.vergilyn.examples.plugin;

import org.pf4j.ExtensionPoint;

public interface Greeting extends ExtensionPoint {
	String getGreeting();
}
