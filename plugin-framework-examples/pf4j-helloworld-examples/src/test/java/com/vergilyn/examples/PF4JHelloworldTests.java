package com.vergilyn.examples;

import com.vergilyn.examples.plugin.Greeting;
import org.junit.jupiter.api.Test;
import org.pf4j.DefaultExtensionFinder;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ExtensionFinder;
import org.pf4j.PluginManager;

import java.util.List;

/**
 *
 * @author vergilyn
 * @since 2022-07-27
 */
public class PF4JHelloworldTests {

	@Test
	public void test(){
		// create the plugin manager:
		//   - DefaultPluginManager
		//   - JarPluginManager
		//   - SecurePluginManagerWrapper
		//   - ZipPluginManager
		PluginManager pluginManager = new DefaultPluginManager();

		// start and load all plugins of application
		// 非必须代码
		pluginManager.loadPlugins();
		pluginManager.startPlugins();

		// retrieve all extensions for "Greeting" extension point
		List<Greeting> greetings = pluginManager.getExtensions(Greeting.class);
		for (Greeting greeting : greetings) {
			System.out.println("[vergilyn] >>>> " + greeting.getGreeting());
		}

		// stop and unload all plugins
		pluginManager.stopPlugins();
		pluginManager.unloadPlugins();
	}

	/**
	 * <a href="https://pf4j.org/doc/serviceloader.html">ServiceLoader</a>
	 * <blockquote>
	 *     `ServiceLoaderExtensionFinder`, the class that lookups for extensions stored in `META-INF/services` folder,
	 *     is not added/enabled by default. To do this please override `createExtensionFinder` from `DefaultPluginManager`
	 * </blockquote>
	 */
	@Test
	public void serviceLoader(){
		final PluginManager pluginManager = new DefaultPluginManager() {

			protected ExtensionFinder createExtensionFinder() {
				DefaultExtensionFinder extensionFinder = (DefaultExtensionFinder) super.createExtensionFinder();
				extensionFinder.addServiceProviderExtensionFinder();

				return extensionFinder;
			}

		};
	}
}
