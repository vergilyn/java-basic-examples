package com.vergilyn.examples.usage.u0001;

import java.util.List;

/**
 *
 * 格式化输出，例如（JMH report）：
 * <pre>
 *   Benchmark                               Mode  Cnt     Score     Error  Units
 *   CollectionAddJMH.arrayList                ss    5     0.612 ±   1.338  ms/op
 *   CollectionAddJMH.copyOnWriteArrayList     ss    5  2644.711 ± 152.747  ms/op
 *   CollectionAddJMH.linkedList               ss    5     1.237 ±   0.313  ms/op
 *   CollectionAddJMH.synchronizedArrayList    ss    5     2.002 ±   0.480  ms/op
 *   CollectionAddJMH.vector                   ss    5     1.270 ±   0.882  ms/op
 * </pre>
 *
 * @author vergilyn
 * @since 2021-04-23
 *
 * @see org.openjdk.jmh.results.format.TextResultFormat#writeOut(java.util.Collection)
 */
@SuppressWarnings("JavadocReference")
public class TextOutputFormat {

	public static void print(String[] head, List<String[]> trs){
		String formatter = formatter(head, trs);

		System.out.format(formatter, head).println();
		for (String[] tr : trs) {
			System.out.format(formatter, tr).println();
		}
	}

	public static String formatter(String[] head, List<String[]> trs){
		int columnLen = head.length;

		int[] max = new int[columnLen];
		for (int i = 0; i < columnLen; i++) {
			max[i] = head[i].length();
		}

		// XXX 2022-01-26，忘记做什么用的了
		// trs.sort(Comparator.comparing(o -> Long.valueOf(o[2])));

		for (String[] tr : trs) {
			for (int i = 0; i < columnLen; i++) {
				max[i] = Math.max(max[i], tr[i].length());
			}
		}

		StringBuilder format = new StringBuilder();
		for (int i : max) {
			// %-?s `-`左对齐
			// `\t` 解决 中英文对齐 （`\t\t`是为了满足排版）
			format.append("%-").append(i).append("s\t\t");
		}

		return format.toString();
	}
}
