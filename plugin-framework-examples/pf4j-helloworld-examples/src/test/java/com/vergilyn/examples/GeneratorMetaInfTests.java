package com.vergilyn.examples;

import org.junit.jupiter.api.Test;
import org.pf4j.processor.ExtensionAnnotationProcessor;
import org.pf4j.processor.ExtensionStorage;
import org.pf4j.processor.LegacyExtensionStorage;
import org.pf4j.processor.ServiceProviderExtensionStorage;

import javax.annotation.processing.RoundEnvironment;
import java.util.Map;
import java.util.Set;

public class GeneratorMetaInfTests {

	/**
	 * <h3>PF4J如何在compile阶段生成 `META-INF/services`文件</h3>
	 *
	 * <p>PF4J实现了编译期注解 {@link ExtensionAnnotationProcessor}。
	 * <br/> 在编译期，生成 ` META-INF/services/{extensionPoint}` 或 `/META-INF/extensions.idx`。
	 *
	 * <p><b>具体参考：{@link ExtensionAnnotationProcessor#process(Set, RoundEnvironment)}</b>
	 * <p> 1. 得到 声明或实现 `org.pf4j.@Extension` 和 `org.pf4j.ExtensionPoint` 的 xx-class-name
	 *
	 * <p> 2. 然后调用 {@link ExtensionStorage#write(Map)}
	 *    <br/> 默认是 {@link LegacyExtensionStorage}，对应target目录 {@link LegacyExtensionStorage#EXTENSIONS_RESOURCE `/META-INF/extensions.idx`}，
	 *    <br/> 可以通过配置指定成 {@link ServiceProviderExtensionStorage}，对应 {@link ServiceProviderExtensionStorage#EXTENSIONS_RESOURCE `/META-INF/services`}
	 *
	 *
	 * <p>
	 * <h3>扩展：debug 编译期注解</h3>
	 * 例如 IDEA中 debug {@link ExtensionAnnotationProcessor#process(Set, RoundEnvironment)}
	 *
	 * <br/> 在IDEA的<b>maven窗口</b>，选中编译项目的 lifecycle-compile 右键debug。（记得先打好断点）。
	 *
	 * <p><b>特别：</b>如果已经存在`target`，记得`mvn clean`，否则不会执行编译期注解process。
	 */
	@Test
	public void test(){

	}
}
