package com.vergilyn.examples;

import com.vergilyn.examples.plugin.Greeting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ExtensionFactory;
import org.pf4j.PluginManager;
import org.pf4j.SingletonExtensionFactory;

import java.util.List;

/**
 *
 * @author vergilyn
 * @since 2022-07-27
 *
 * @see <a href="https://pf4j.org/doc/extension-instantiation.html">Extension instantiation</a>
 */
public class InstanceTests {

	/**
	 * 默认 PF4J 是 `prototype`实例化插件对象，通过{@link Class#newInstance()}。
	 * <br/>所以，每次调用{@link PluginManager#getExtensions(Class)} 会生成新的对象。
	 *
	 */
	@Test
	public void prototype(){
		PluginManager pluginManager = new DefaultPluginManager();

		List<Greeting> firsts = pluginManager.getExtensions(Greeting.class);
		List<Greeting> seconds = pluginManager.getExtensions(Greeting.class);

		Assertions.assertSame(firsts.get(0), seconds.get(0));
	}

	@Test
	public void singleton(){
		PluginManager pluginManager = new DefaultPluginManager(){
			@Override
			protected ExtensionFactory createExtensionFactory() {
				return new SingletonExtensionFactory(this);
			}
		};

		List<Greeting> firsts = pluginManager.getExtensions(Greeting.class);
		List<Greeting> seconds = pluginManager.getExtensions(Greeting.class);

		Assertions.assertSame(firsts.get(0), seconds.get(0));
	}
}
