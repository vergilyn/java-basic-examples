package com.vergilyn.examples.snakeyaml.convert;

import com.alibaba.fastjson.JSON;
import com.vergilyn.examples.snakeyaml.AbstractSnakeyamlTests;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.core.ResolvableType;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.Reader;
import java.util.List;
import java.util.Properties;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;

@SuppressWarnings("JavadocReference")
class YamlToPojoTests extends AbstractSnakeyamlTests {

	/**
	 * <blockquote>
	 *   By default standard JavaBean properties and public fields are included.
	 *   <b>`BeanAccess.FIELD`</b> makes is possible to use private fields directly.
	 * </blockquote>
	 *
	 * <p>
	 * 备注：肯定不如 spring 扩展后的灵活。<br/>
	 *   1) 数组不支持`filters: config,wall,stat`这样写，必须是标准的`[config,wall,stat]`或者"-config..." <br/>
	 *   2) 属性名映射必须一致，不会转换。例如`min-idle`不会转换成`minIdle`。
	 * </p>
	 *
	 * @see <a href="https://bitbucket.org/asomov/snakeyaml/wiki/Documentation#markdown-header-javabeans">Yaml Javabean</a>
	 * @see <a href="https://github.com/EsotericSoftware/yamlbeans">Github, yamlbeans</a>
	 */
	@Test
	@SneakyThrows
	public void yaml(){
		String yamlStr =
				  "username: root\n"
				+ "password: 123456\n"
			    + "#unknown: 123456\n"
				+ "druid:\n"
				+ "  minIdle: 5\n"
				+ "  testWhileIdle: true\n"
				// + "  filters: config,wall,stat\n"  // 不是标准写法，无法转换成List/Array
				// + "  filters: [config,wall,stat]\n"  // 可以
				+ "  filters: \n"
			    + "    - config\n"
			    + "    - wall\n"
			    + "    - stat\n";

		final Yaml yaml = createYaml();
		yaml.setBeanAccess(BeanAccess.FIELD);

		try (final Reader reader = readerString(yamlStr)){
			DatasourceProperties properties = yaml.loadAs(reader, DatasourceProperties.class);

			System.out.println(JSON.toJSONString(properties, PrettyFormat));
		}
	}

	/**
	 * @see com.alibaba.boot.nacos.config.util.NacosConfigPropertiesUtils#buildNacosConfigProperties
	 */
	@Test
	public void spring(){
		String yamlStr =
				  "spring:\n"
				  + "  datasource:\n"
				  + "    username: root\n"
				  + "    password: 123456\n"
				  + "    druid:\n"
				  + "      minIdle: 5\n"
				  + "      test-while-idle: true\n"
				  + "      filters: config,wall,stat\n"  // spring 支持多种数组写法
				// + "  filters: [config,wall,stat]\n"  // 可以
				;

		DatasourceProperties datasourceProperties = new DatasourceProperties();

		Properties flattenedProperties = flattenedPropertiesYamlString(yamlStr);
		MapConfigurationPropertySource propertySource = new MapConfigurationPropertySource(flattenedProperties);

		Binder binder = new Binder(propertySource);
		ResolvableType type = ResolvableType.forClass(DatasourceProperties.class);
		Bindable<?> target = Bindable.of(type).withExistingValue(datasourceProperties);
		binder.bind("spring.datasource", target);

		System.out.println(JSON.toJSONString(datasourceProperties, PrettyFormat));
	}

	@Data
	static class DatasourceProperties{
		private String username;
		private String password;

		private DruidProperties druid = new DruidProperties();
	}

	@Data
	static class DruidProperties{
		private Integer minIdle;
		private boolean testWhileIdle;
		private List<String> filters;
	}
}
