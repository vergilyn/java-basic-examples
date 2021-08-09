package com.vergilyn.examples.jdk8.features.parallel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vergilyn.examples.jdk8.features.Person;

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
 * parallelStream 不一定比 stream 效率更高。
 *
 * <pre>
 * Benchmark                         Mode  Cnt    Score     Error  Units
 * ParallelStreamJMH.parallelStream    ss    5  208.425 ± 473.951  ms/op
 * ParallelStreamJMH.stream            ss    5  229.563 ± 408.682  ms/op
 * </pre>
 *
 * @author vergilyn
 * @since 2021-08-05
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
// Warmup: 2 iterations, 1000 ms each
@Warmup(iterations = 2, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
// Measurement: 5 iterations, 1000 ms each
@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.SingleShotTime)
public class ParallelStreamJMH {
	private static final int SIZE = 1_000_000;
	private List<Person> streamDB = null;
	private List<Person> parallelDB = null;

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder().include(ParallelStreamJMH.class.getSimpleName())
				.forks(1)  // Number of forks to use in the run
				.build();

		new Runner(opt).run();
	}

	@Setup(Level.Trial)
	public void setup(){
		streamDB = Stream.iterate(new Person(0L, "name", "S")
				, person -> new Person(person.getId() + 1, person.getName(), person.getType()))
				.limit(SIZE)
				.collect(Collectors.toList());

		parallelDB = Stream.iterate(new Person(0L, "name", "P")
				, person -> new Person(person.getId() + 1, person.getName(), person.getType()))
				.limit(SIZE)
				.collect(Collectors.toList());
	}


	@Benchmark
	public void stream() {
		streamDB.forEach(this::threadSafeHandle);
	}

	@Benchmark
	public void parallelStream() {
		parallelDB.parallelStream().forEach(this::threadSafeHandle);
	}

	private void threadSafeHandle(Person person){
		person.setName(person.getName() + "-" + person.getId());
	}
}
