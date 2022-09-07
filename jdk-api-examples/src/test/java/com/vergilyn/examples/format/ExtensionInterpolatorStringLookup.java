package com.vergilyn.examples.format;

import org.apache.commons.text.lookup.StringLookup;
import org.apache.commons.text.lookup.StringLookupFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * 1. 扩展：`value` 后置处理。<br/>
 * 2. 扩展：自定义分隔符。<br/>
 * @see org.apache.commons.text.lookup.InterpolatorStringLookup
 */
public class ExtensionInterpolatorStringLookup implements StringLookup {
	/** Constant for the prefix separator. */
	private static final String PREFIX_SEPARATOR = ":";

	private final String prefixSeparator;

	/** The default string lookup. */
	private final StringLookup defaultStringLookup;

	/** The map of String lookups keyed by prefix. */
	private final Map<String, StringLookup> stringLookupMap;

	/**
	 * 后置处理替换后的`value`，例如 统一处理 `value.trim()`
	 */
	private final Function<String, String> valuePostProcessor;

	/**
	 * Creates a fully customized instance.
	 *
	 * @param stringLookupMap the map of string lookups.
	 * @param defaultStringLookup the default string lookup.
	 * @param addDefaultLookups whether the default lookups should be used.
	 */
	public ExtensionInterpolatorStringLookup(final Map<String, StringLookup> stringLookupMap,
			final StringLookup defaultStringLookup,
			String prefixSeparator,
			Function<String, String> valuePostProcessor,
			final boolean addDefaultLookups) {

		this.defaultStringLookup = defaultStringLookup;
		this.stringLookupMap = new HashMap<>(stringLookupMap.size());
		this.prefixSeparator = prefixSeparator == null || prefixSeparator.isEmpty() ? PREFIX_SEPARATOR : prefixSeparator;

		this.valuePostProcessor = valuePostProcessor;

		for (final Map.Entry<String, StringLookup> entry : stringLookupMap.entrySet()) {
			this.stringLookupMap.put(toKey(entry.getKey()), entry.getValue());
		}

		if (addDefaultLookups) {
			StringLookupFactory.INSTANCE.addDefaultStringLookups(this.stringLookupMap);
		}
	}

	@Override
	public String lookup(String var) {
		if (var == null) {
			return postProcessor(null);
		}

		final int prefixPos = var.indexOf(prefixSeparator);
		int separatorLength = prefixSeparator.length();

		if (prefixPos >= 0) {
			final String prefix = toKey(var.substring(0, prefixPos));
			final String name = var.substring(prefixPos + separatorLength);
			final StringLookup lookup = stringLookupMap.get(prefix);
			String value = null;
			if (lookup != null) {
				value = lookup.lookup(name);
			}

			if (value != null) {
				return postProcessor(value);
			}
			var = var.substring(prefixPos + separatorLength);
		}

		if (defaultStringLookup != null) {
			String value = defaultStringLookup.lookup(var);
			return postProcessor(value);
		}

		return postProcessor(null);
	}

	protected String postProcessor(String value){
		if (valuePostProcessor == null){
			return value;
		}

		return valuePostProcessor.apply(value);
	}

	public StringLookup getDefaultStringLookup() {
		return defaultStringLookup;
	}

	public Map<String, StringLookup> getStringLookupMap() {
		return stringLookupMap;
	}

	@Override
	public String toString() {
		return super.toString() + " [stringLookupMap=" + stringLookupMap + ", defaultStringLookup="
				+ defaultStringLookup + "]";
	}

	public static String toKey(final String key) {
		return key.toLowerCase(Locale.ROOT);
	}
}