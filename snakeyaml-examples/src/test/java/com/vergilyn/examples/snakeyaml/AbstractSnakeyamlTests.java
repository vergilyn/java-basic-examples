package com.vergilyn.examples.snakeyaml;

import java.io.InputStream;
import java.io.Reader;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.reader.UnicodeReader;

/**
 *
 * @author vergilyn
 * @since 2021-04-27
 */
public abstract class AbstractSnakeyamlTests {
	private static final String DEFAULT_YAML = "application-datasource.yml";

	protected Reader defaultYml(){
		InputStream stream = AbstractSnakeyamlTests.class.getResourceAsStream(DEFAULT_YAML);
		return new UnicodeReader(stream);
	}

	protected Yaml createYaml(){
		Yaml yaml = new Yaml();
		return yaml;
	}

}
