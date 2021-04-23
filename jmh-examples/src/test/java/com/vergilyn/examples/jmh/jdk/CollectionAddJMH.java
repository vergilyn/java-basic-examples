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
import org.openjdk.jmh.annotations.Level;
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
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, batchSize = CollectionAddJMH.SIZE)
@Measurement(iterations = 5, batchSize = CollectionAddJMH.SIZE)
@BenchmarkMode(Mode.SingleShotTime)
public class CollectionAddJMH {
	public static final int SIZE = 100_000;
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

	@Setup(Level.Iteration)
	public void setup(){
		arrayList.clear();
		linkedList.clear();
		vector.clear();
		copyOnWriteArrayList.clear();
		synchronizedArrayList.clear();
	}

	@Benchmark
	public void arrayList() {
		arrayList.add(ELEMENT);
	}

	@Benchmark
	public void linkedList() {
		linkedList.add(ELEMENT);
	}

	@Benchmark
	public void vector() {
		vector.add(ELEMENT);
	}

	@Benchmark
	public void copyOnWriteArrayList() {
		copyOnWriteArrayList.add(ELEMENT);
	}

	@Benchmark
	public void synchronizedArrayList() {
		synchronizedArrayList.add(ELEMENT);
	}
}
