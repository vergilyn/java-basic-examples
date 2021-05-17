package com.vergilyn.examples.snakeyaml.convert;

import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.vergilyn.examples.snakeyaml.AbstractSnakeyamlTests;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

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
public class YamlToProperties extends AbstractSnakeyamlTests {

	@Test
	@SneakyThrows
	public void multiLevelNestedProperties(){
		Yaml yaml = createYaml();

		try (final Reader reader = defaultYml()){
			Properties properties = yaml.loadAs(reader, Properties.class);

			System.out.println(JSON.toJSONString(properties, SerializerFeature.PrettyFormat));
		}
	}

	@Test
	@SneakyThrows
	public void flattenedProperties(){
		Yaml yaml = createYaml();

		try (final Reader reader = defaultYml()){
			Object object = yaml.loadAs(reader, LinkedHashMap.class);

			Map<String, Object> asMap = asMap(object);
			Map<String, Object> flattenedMap = getFlattenedMap(asMap);

			Properties properties = new Properties();
			properties.putAll(flattenedMap);

			System.out.println(JSON.toJSONString(properties, SerializerFeature.PrettyFormat));
		}
	}


	/**
	 * SEE: spring-beans `YamlProcessor#asMap(java.lang.Object)`
	 */
	private Map<String, Object> asMap(Object object) {
		// YAML can have numbers as keys
		Map<String, Object> result = new LinkedHashMap<>();
		if (!(object instanceof Map)) {
			// A document can be a text literal
			result.put("document", object);
			return result;
		}

		Map<Object, Object> map = (Map<Object, Object>) object;
		map.forEach((key, value) -> {
			if (value instanceof Map) {
				value = asMap(value);
			}
			if (key instanceof CharSequence) {
				result.put(key.toString(), value);
			}
			else {
				// It has to be a map key in this case
				result.put("[" + key.toString() + "]", value);
			}
		});
		return result;
	}

	/**
	 * SEE: spring-beans `YamlProcessor#asMap(java.lang.Object)`
	 */
	protected final Map<String, Object> getFlattenedMap(Map<String, Object> source) {
		Map<String, Object> result = new LinkedHashMap<>();
		buildFlattenedMap(result, source, null);
		return result;
	}

	private void buildFlattenedMap(Map<String, Object> result, Map<String, Object> source, @Nullable String path) {
		source.forEach((key, value) -> {
			if (StringUtils.hasText(path)) {
				if (key.startsWith("[")) {
					key = path + key;
				}
				else {
					key = path + '.' + key;
				}
			}
			if (value instanceof String) {
				result.put(key, value);
			}
			else if (value instanceof Map) {
				// Need a compound key
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) value;
				buildFlattenedMap(result, map, key);
			}
			else if (value instanceof Collection) {
				// Need a compound key
				@SuppressWarnings("unchecked")
				Collection<Object> collection = (Collection<Object>) value;
				if (collection.isEmpty()) {
					result.put(key, "");
				}
				else {
					int count = 0;
					for (Object object : collection) {
						buildFlattenedMap(result, Collections.singletonMap(
								"[" + (count++) + "]", object), key);
					}
				}
			}
			else {
				result.put(key, (value != null ? value : ""));
			}
		});
	}

}
