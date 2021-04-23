package com.vergilyn.examples.usage.u0001;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-04-23
 */
class TextOutputFormatTests {

	/**
	 * <pre>
	 *   Benchmark                               Mode  Cnt     Score     Error  Units
	 *   CollectionAddJMH.arrayList                ss    5     0.612 ±   1.338  ms/op
	 *   CollectionAddJMH.copyOnWriteArrayList     ss    5  2644.711 ± 152.747  ms/op
	 *   CollectionAddJMH.linkedList               ss    5     1.237 ±   0.313  ms/op
	 *   CollectionAddJMH.synchronizedArrayList    ss    5     2.002 ±   0.480  ms/op
	 *   CollectionAddJMH.vector                   ss    5     1.270 ±   0.882  ms/op
	 * </pre>
	 */
	@Test
	public void formatOutput(){
		String[] head = {"Benchmark", "Mode", "Cnt", "Score", "Error", "Units"};
		List<String[]> table = new ArrayList<>(16);

		table.add(new String[]{"CollectionAddJMH.arrayList", "ss", "5", "0.612 ±", "1.338", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.copyOnWriteArrayList", "ss", "5", "2644.711 ±", "152.747", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.linkedList", "ss", "5", "1.237 ±", "0.313", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.synchronizedArrayList", "ss", "5", "2.002 ±", "0.480", "ms/op"});
		table.add(new String[]{"CollectionAddJMH.linkedList", "ss", "5", "1.270 ±", "0.882", "ms/op"});

		TextOutputFormat.print(head, table);
	}
}