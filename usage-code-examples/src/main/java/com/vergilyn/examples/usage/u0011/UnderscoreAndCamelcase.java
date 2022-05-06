package com.vergilyn.examples.usage.u0011;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class UnderscoreAndCamelcase {

	/**
	 * Convert a column name with underscores to the corresponding property name using "camel case".
	 * A name like "customer_number" would match a "customerNumber" property name.
	 * @param name the column name to be converted
	 * @return the name using "camel case"
	 *
	 * @see org.springframework.jdbc.support.JdbcUtils#convertUnderscoreNameToPropertyName(String)
	 */
	public static String convertUnderscoreNameToPropertyName(@Nullable String name) {
		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (name != null && name.length() > 0) {
			if (name.length() > 1 && name.charAt(1) == '_') {
				result.append(Character.toUpperCase(name.charAt(0)));
			}
			else {
				result.append(Character.toLowerCase(name.charAt(0)));
			}
			for (int i = 1; i < name.length(); i++) {
				char c = name.charAt(i);
				if (c == '_') {
					nextIsUpper = true;
				}
				else {
					if (nextIsUpper) {
						result.append(Character.toUpperCase(c));
						nextIsUpper = false;
					}
					else {
						result.append(Character.toLowerCase(c));
					}
				}
			}
		}
		return result.toString();
	}

	/**
	 * Convert a name in camelCase to an underscored name in lower case.
	 * Any upper case letters are converted to lower case with a preceding underscore.
	 *
	 * @see org.springframework.jdbc.core.BeanPropertyRowMapper#underscoreName
	 */
	public static String underscoreName(String name) {
		if (!StringUtils.hasLength(name)) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		result.append(lowerCaseName(name.substring(0, 1)));
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			String slc = lowerCaseName(s);
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			}
			else {
				result.append(s);
			}
		}
		return result.toString();
	}

	private static String lowerCaseName(String name) {
		return name.toLowerCase(Locale.US);
	}
}
