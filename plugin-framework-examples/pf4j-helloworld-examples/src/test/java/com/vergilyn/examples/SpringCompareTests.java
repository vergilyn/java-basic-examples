package com.vergilyn.examples;

import com.vergilyn.examples.plugin.Greeting;
import org.junit.jupiter.api.Test;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.util.List;

public class SpringCompareTests {

	@Test
	public void pf4j(){
		PluginManager pluginManager = new DefaultPluginManager();

		/* 如果是 spring：
		 * ```
		 * @Autowired(required=false)
		 * private List<Greeting> greetings;
		 *
		 * greetings.forEach(...)
		 * ```
		 *
		 * 2022-07-27，所以感觉没必要通过PF4J。
		 */
		List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);
		for (Greeting greeting : greetings) {
			System.out.println("[vergilyn][PF4J] >>>> " + greeting.getGreeting());
		}
	}
}
