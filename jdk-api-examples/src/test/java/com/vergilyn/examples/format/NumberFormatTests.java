package com.vergilyn.examples.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.junit.jupiter.api.Test;

class NumberFormatTests {

	@Test
	public void format() {
		DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
		format.setGroupingSize(3);

		// 默认是','
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		symbols.setGroupingSeparator('_');
		format.setDecimalFormatSymbols(symbols);

		System.out.println(format.format(100000));
	}
}