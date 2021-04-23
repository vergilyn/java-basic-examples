package com.vergilyn.examples.jmh.jdk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * See Also: {@linkplain CollectionAddTests}
 *
 * <p>
 * `SIZE = 100_000`:
 * <pre>
 *   Benchmark                               Mode  Cnt      Score       Error  Units
 *   CollectionAddJMH.arrayList                ss    5      1.202 ±     3.798  ms/op
 *   CollectionAddJMH.copyOnWriteArrayList     ss    5  24365.204 ± 35612.028  ms/op
 *   CollectionAddJMH.linkedList               ss    5      1.614 ±     0.778  ms/op
 *   CollectionAddJMH.synchronizedArrayList    ss    5      2.614 ±     2.683  ms/op
 *   CollectionAddJMH.vector                   ss    5      1.625 ±     2.834  ms/op
 * </pre>
 *
 * @author vergilyn
 * @since 2021-04-23
 *
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
// Warmup: 2 iterations, 1000 ms each
@Warmup(iterations = 2, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
// Measurement: 5 iterations, 1000 ms each
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.SingleShotTime)
public class CollectionAddJMH {
	private static final int SIZE = 100_000;
	private static final String ELEMENT = "element";

	private static final List<String> arrayList = new ArrayList<>(SIZE);
	private static final List<String> linkedList = new LinkedList<>();
	private static final List<String> vector = new Vector<>(SIZE);
	private static final List<String> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
	private static final List<String> synchronizedArrayList = Collections.synchronizedList(new ArrayList<>(SIZE));

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(CollectionAddJMH.class.getSimpleName())
				.forks(1)
				.build();

		new Runner(opt).run();
	}

	@Setup
	public void setup(){
		arrayList.clear();
		linkedList.clear();
		vector.clear();
		copyOnWriteArrayList.clear();
		synchronizedArrayList.clear();
	}



	@Benchmark
	public void arrayList() {
		for (int i = 0; i < SIZE; i++){
			arrayList.add(ELEMENT);
		}
	}

	@Benchmark
	public void linkedList() {
		for (int i = 0; i < SIZE; i++){
			linkedList.add(ELEMENT);
		}
	}

	@Benchmark
	public void vector() {
		for (int i = 0; i < SIZE; i++){
			vector.add(ELEMENT);
		}
	}

	@Benchmark
	public void copyOnWriteArrayList() {
		for (int i = 0; i < SIZE; i++){
			copyOnWriteArrayList.add(ELEMENT);
		}
	}

	@Benchmark
	public void synchronizedArrayList() {
		for (int i = 0; i < SIZE; i++){
			synchronizedArrayList.add(ELEMENT);
		}
	}
}
