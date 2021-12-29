package com.vergilyn.examples.usage.u0009;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 1. 不支持 嵌套表达式。（原本是支持的） <br/>
 * 2. 支持默认值，默认值未调用 `String.trim()`，例如`${name: vergilyn}`的默认值是` vergilyn`。 <br/>
 * 3. 同时 返回被替换的 actual-placeholders <br/>
 *
 * @author vergilyn
 * @since 2021-12-28
 *
 * @see org.springframework.util.PropertyPlaceholderHelper
 */
public class SimplePlaceholderHelper {

	private static final Log logger = LogFactory.getLog(SimplePlaceholderHelper.class);

	/** Prefix for system property placeholders: "${". */
	public static final String PLACEHOLDER_PREFIX = "${";

	/** Suffix for system property placeholders: "}". */
	public static final String PLACEHOLDER_SUFFIX = "}";

	/** Value separator for system property placeholders: ":". */
	public static final String VALUE_SEPARATOR = ":";

	private static final Map<String, String> wellKnownSimplePrefixes = new HashMap<>(4);

	static {
		wellKnownSimplePrefixes.put("}", "{");
		wellKnownSimplePrefixes.put("]", "[");
		wellKnownSimplePrefixes.put(")", "(");
	}


	private final String placeholderPrefix;

	private final String placeholderSuffix;

	private final String simplePrefix;

	private final String valueSeparator;

	private final boolean ignoreUnresolvablePlaceholders;

	private final String unresolvableValue;


	/**
	 * Creates a new {@code SimplePlaceholderHelper} that uses the supplied prefix and suffix.
	 * Unresolvable placeholders are ignored.
	 * @param placeholderPrefix the prefix that denotes the start of a placeholder
	 * @param placeholderSuffix the suffix that denotes the end of a placeholder
	 */
	public SimplePlaceholderHelper(String placeholderPrefix, String placeholderSuffix) {
		this(placeholderPrefix, placeholderSuffix, null, true, null);
	}

	/**
	 * Creates a new {@code SimplePlaceholderHelper} that uses the supplied prefix and suffix.
	 * @param placeholderPrefix the prefix that denotes the start of a placeholder
	 * @param placeholderSuffix the suffix that denotes the end of a placeholder
	 * @param valueSeparator the separating character between the placeholder variable
	 * and the associated default value, if any
	 * @param ignoreUnresolvablePlaceholders indicates whether unresolvable placeholders should
	 * be ignored ({@code true}) or cause an exception ({@code false})
	 */
	public SimplePlaceholderHelper(String placeholderPrefix, String placeholderSuffix,
			 String valueSeparator, boolean ignoreUnresolvablePlaceholders, String unresolvableValue) {

		Assert.notNull(placeholderPrefix, "'placeholderPrefix' must not be null");
		Assert.notNull(placeholderSuffix, "'placeholderSuffix' must not be null");
		this.placeholderPrefix = placeholderPrefix;
		this.placeholderSuffix = placeholderSuffix;
		String simplePrefixForSuffix = wellKnownSimplePrefixes.get(this.placeholderSuffix);
		if (simplePrefixForSuffix != null && this.placeholderPrefix.endsWith(simplePrefixForSuffix)) {
			this.simplePrefix = simplePrefixForSuffix;
		}
		else {
			this.simplePrefix = this.placeholderPrefix;
		}
		this.valueSeparator = valueSeparator;
		this.ignoreUnresolvablePlaceholders = ignoreUnresolvablePlaceholders;
		this.unresolvableValue = unresolvableValue;
	}

	public static SimplePlaceholderHelper buildDefaultStrictHelper(){
		return new SimplePlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, false, null);
	}

	public static SimplePlaceholderHelper buildDefaultNonStrictHelper(String unresolvableValue){
		return new SimplePlaceholderHelper(PLACEHOLDER_PREFIX, PLACEHOLDER_SUFFIX, VALUE_SEPARATOR, true, unresolvableValue);
	}

	/**
	 * Replaces all placeholders of format {@code ${name}} with the corresponding
	 * property from the supplied {@link Properties}.
	 * @param value the value containing the placeholders to be replaced
	 * @param properties the {@code Properties} to use for replacement
	 * @return
	 *   - key, the supplied value with placeholders replaced inline  <br/>
	 *   - value, all placeholders
	 */
	public Pair<String, List<String>>  replacePlaceholders(String value, final Properties properties) {
		Assert.notNull(properties, "'properties' must not be null");
		return replacePlaceholders(value, properties::getProperty);
	}

	/**
	 * Replaces all placeholders of format {@code ${name}} with the value returned
	 * from the supplied {@link PlaceholderResolver}.
	 * @param value the value containing the placeholders to be replaced
	 * @param placeholderResolver the {@code PlaceholderResolver} to use for replacement
	 * @return
	 *   - key, the supplied value with placeholders replaced inline <br/>
	 *   - value, all placeholders
	 */
	public Pair<String, List<String>> replacePlaceholders(String value, SimplePlaceholderHelper.PlaceholderResolver placeholderResolver) {
		Assert.notNull(value, "'value' must not be null");
		return parseStringValue(value, placeholderResolver);
	}

	/**
	 * @return
	 *   - key, the supplied value with placeholders replaced inline  <br/>
	 *   - value, all placeholders
	 */
	protected Pair<String, List<String>> parseStringValue(String value, PlaceholderResolver placeholderResolver) {
		List<String> placeholders = Lists.newArrayList();

		int startIndex = value.indexOf(this.placeholderPrefix);
		if (startIndex == -1) {
			return Pair.of(value, placeholders);
		}

		StringBuilder result = new StringBuilder(value);
		while (startIndex != -1) {
			int endIndex = findPlaceholderEndIndex(result, startIndex);
			if (endIndex != -1) {
				// 得到带`value_separator`的 placeholder
				String placeholder = result.substring(startIndex + this.placeholderPrefix.length(), endIndex);
				String actualPlaceholder = placeholder;
				String defaultValue = null;
				if (this.valueSeparator != null){
					int separatorIndex = placeholder.indexOf(this.valueSeparator);
					if (separatorIndex != -1){
						actualPlaceholder = placeholder.substring(0, separatorIndex);
						// 默认值未进行`String.trim()`
						defaultValue = placeholder.substring(separatorIndex + this.valueSeparator.length());
					}
				}

				placeholders.add(actualPlaceholder);
				// Now obtain the value for the fully resolved key...
				String propVal = placeholderResolver.resolvePlaceholder(actualPlaceholder);
				if (propVal == null){
					propVal = defaultValue;
				}

				if (propVal != null) {
					result.replace(startIndex, endIndex + this.placeholderSuffix.length(), propVal);
					if (logger.isTraceEnabled()) {
						logger.trace("Resolved placeholder '" + placeholder + "'");
					}
					startIndex = result.indexOf(this.placeholderPrefix, startIndex + propVal.length());
				}
				else if (this.ignoreUnresolvablePlaceholders) {
					// 无法解析的占位符替换成指定的默认值。否则保留placeholder。
					if (this.unresolvableValue != null){
						result.replace(startIndex, endIndex + this.placeholderSuffix.length(), this.unresolvableValue);
					}

					// Proceed with unprocessed value.
					startIndex = result.indexOf(this.placeholderPrefix, endIndex + this.placeholderSuffix.length());
				}
				else {
					throw new IllegalArgumentException("Could not resolve placeholder '" +
							                                   placeholder + "'" + " in value \"" + value + "\"");
				}
			}
			else {
				startIndex = -1;
			}
		}
		return Pair.of(result.toString(), placeholders);
	}

	private int findPlaceholderEndIndex(CharSequence buf, int startIndex) {
		int index = startIndex + this.placeholderPrefix.length();
		int withinNestedPlaceholder = 0;
		while (index < buf.length()) {
			if (StringUtils.substringMatch(buf, index, this.placeholderSuffix)) {
				if (withinNestedPlaceholder > 0) {
					withinNestedPlaceholder--;
					index = index + this.placeholderSuffix.length();
				}
				else {
					return index;
				}
			}
			else if (StringUtils.substringMatch(buf, index, this.simplePrefix)) {
				withinNestedPlaceholder++;
				index = index + this.simplePrefix.length();
			}
			else {
				index++;
			}
		}
		return -1;
	}


	/**
	 * Strategy interface used to resolve replacement values for placeholders contained in Strings.
	 */
	@FunctionalInterface
	public interface PlaceholderResolver {

		/**
		 * Resolve the supplied placeholder name to the replacement value.
		 * @param placeholderName the name of the placeholder to resolve
		 * @return the replacement value, or {@code null} if no replacement is to be made
		 */
		String resolvePlaceholder(String placeholderName);
	}

}
