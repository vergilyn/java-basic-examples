package com.vergilyn.examples.plugin;

import org.pf4j.Extension;

@Extension
public class WelcomeGreeting implements Greeting {

	public String getGreeting() {
		return "Welcome PF4J Hellowrold.";
	}

}
