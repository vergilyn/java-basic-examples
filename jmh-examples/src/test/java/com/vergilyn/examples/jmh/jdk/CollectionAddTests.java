package com.vergilyn.examples.jmh.jdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * See Also: {@linkplain CollectionAddJMH}
 *
 * <p>
 * <pre>
 *   METHOD                 		SIZE  		COST(μs)
 *   arrayList()            		100000		2127
 *   linkedList()           		100000		2740
 *   vector()               		100000		3658
 *   synchronizedArrayList()		100000		9504
 *   copyOnWriteArrayList() 		100000		3625393
 * </pre>
 *
 * @author vergilyn
 * @since 2021-04-23
 */
public class CollectionAddTests {
	private static final int SIZE = 100_000;
	private static final String ELEMENT = "element";

	private final List<String> arrayList = new ArrayList<>(SIZE);
	private final List<String> linkedList = new LinkedList<>();
	private final List<String> vector = new Vector<>(SIZE);
	private final List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
	private final List<String> synchronizedArrayList = Collections.synchronizedList(new ArrayList<>(SIZE));

	private Stopwatch stopwatch;
	private static final List<String[]> _LOG = new ArrayList<>(16);


	@BeforeEach
	public void beforeEach(){
		arrayList.clear();
		linkedList.clear();
		vector.clear();
		copyOnWriteArrayList.clear();
		synchronizedArrayList.clear();

		stopwatch = Stopwatch.createStarted();
	}

	@AfterEach
	public void afterEach(TestInfo testInfo){
		stopwatch.stop();
		_LOG.add(new String[]{testInfo.getDisplayName(), SIZE + "", stopwatch.elapsed(TimeUnit.MICROSECONDS) + "" });
	}

	@AfterAll
	public static void afterClass() {
		/**
		 * 格式化对齐输出 例如JMH-report
		 *   {@linkplain org.openjdk.jmh.results.format.TextResultFormat#writeOut(java.util.Collection)}
		 */
		String[] head = {"METHOD", "SIZE", "COST"};
		String format = formatter(head, _LOG);

		System.out.format(format, head[0], head[1], head[2] + "(μs)").println();
		for (String[] strings : _LOG) {
			System.out.format(format, strings[0], strings[1], strings[2]).println();
		}
	}

	private static String formatter(String[] head, List<String[]> trs){
		int columnLen = head.length;

		int[] max = new int[columnLen];
		for (int i = 0; i < columnLen; i++) {
			max[i] = head[i].length();
		}

		trs.sort(Comparator.comparing(o -> Long.valueOf(o[2])));

		for (String[] tr : trs) {
			for (int i = 0; i < columnLen; i++) {
				max[i] = Math.max(max[i], tr[i].length());
			}
		}

		StringBuilder format = new StringBuilder();
		for (int i : max) {
			// %-?s `-`左对齐
			format.append("%-").append(i).append("s\t\t");
		}

		return format.toString();
	}

	@Test
	public void arrayList() {
		for (int i = 0; i < SIZE; i++){
			arrayList.add(ELEMENT);
		}
	}

	@Test
	public void linkedList() {
		for (int i = 0; i < SIZE; i++){
			linkedList.add(ELEMENT);
		}
	}

	@Test
	public void vector() {
		for (int i = 0; i < SIZE; i++){
			vector.add(ELEMENT);
		}
	}

	@Test
	public void copyOnWriteArrayList() {
		for (int i = 0; i < SIZE; i++){
			copyOnWriteArrayList.add(ELEMENT);
		}
	}

	@Test
	public void synchronizedArrayList() {
		for (int i = 0; i < SIZE; i++){
			synchronizedArrayList.add(ELEMENT);
		}
	}
}
