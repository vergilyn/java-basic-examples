package com.vergilyn.examples.escape;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.api.Test;

public class EscapeTests {

	@Test
	public void stringEscape(){
		String str = "*2\r\n$4\r\nINCR\r\n$14\r\nlettuce:manual\r\n";

		String escapeJava = StringEscapeUtils.escapeJava(str);
		System.out.println(escapeJava);
	}

}
