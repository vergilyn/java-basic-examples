package com.vergilyn.examples.snakeyaml.convert;

import java.io.Reader;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.vergilyn.examples.snakeyaml.AbstractSnakeyamlTests;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import static com.alibaba.fastjson.serializer.SerializerFeature.PrettyFormat;

/**
 * spring-boot load yaml/properties: <br/>
 *   `ConfigFileApplicationListener.Loader
 *      #load(String location, String name, Profile profile, DocumentFilterFactory filterFactory, DocumentConsumer consumer)`
 *  找到yaml/properties resource，并确定对应的 Yaml/Properties SourceLoader <br/>
 *  --> {@linkplain org.springframework.boot.env.YamlPropertySourceLoader#load(String, Resource)}<br/>
 *  --> {@linkplain org.springframework.boot.env.PropertiesPropertySourceLoader#load(String, Resource)}<br/>
 *
 *
 * @author vergilyn
 * @since 2021-04-29
 */
public class YamlToPropertiesTests extends AbstractSnakeyamlTests {

	@Test
	@SneakyThrows
	public void multiLevelNestedProperties(){
		Yaml yaml = createYaml();

		try (final Reader reader = defaultYml()){
			Properties properties = yaml.loadAs(reader, Properties.class);

			System.out.println(JSON.toJSONString(properties, PrettyFormat));
		}
	}

	@Test
	@SneakyThrows
	public void flattenedProperties(){
		final Properties properties = defaultFlattenedProperties();
		System.out.println(JSON.toJSONString(properties, PrettyFormat));
	}


}
