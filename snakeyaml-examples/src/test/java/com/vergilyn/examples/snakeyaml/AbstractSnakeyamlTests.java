package com.vergilyn.examples.snakeyaml;

import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.vergilyn.examples.snakeyaml.utils.YamlPropertySourceLoader;

import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 *
 * @author vergilyn
 * @since 2021-04-27
 */
public abstract class AbstractSnakeyamlTests {
	protected static final String DEFAULT_YAML = "application-datasource.yml";

	protected Reader defaultYml(){
		return readerResource(DEFAULT_YAML);
	}

	protected Properties defaultFlattenedProperties(){
		return flattenedPropertiesResource(DEFAULT_YAML);
	}

	protected Properties flattenedPropertiesResource(String yamlResourcePath){
		try(Reader reader = readerResource(yamlResourcePath)) {
			Yaml yaml = createYaml();

			return flattenedProperties(yaml.loadAs(reader, LinkedHashMap.class));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected Properties flattenedPropertiesYamlString(String yamlStr){
		try(Reader reader =readerString(yamlStr)) {
			Yaml yaml = createYaml();

			return flattenedProperties(yaml.loadAs(reader, LinkedHashMap.class));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected Properties flattenedProperties(Map map){
		return YamlPropertySourceLoader.flattenedProperties(map);
	}

	protected Reader readerResource(String yamlResourcePath){
		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(yamlResourcePath);
		return new UnicodeReader(stream);
	}

	protected Reader readerString(String yamlStr){
		return new UnicodeReader(toInputStream(yamlStr));
	}

	protected InputStream toInputStream(String input){
		return IOUtils.toInputStream(input, Charset.defaultCharset());
	}

	/**
	 * Public YAML interface. This class is not thread-safe. Which means that all the methods of the same
	 * instance can be called only by one thread.
	 * It is better to create an instance for every YAML stream.
	 */
	protected Yaml createYaml(){
		return new Yaml();
	}

}
