package com.vergilyn.examples.forkjoinpool;

import java.util.Set;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vergilyn
 * @since 2021-09-13
 */
@SuppressWarnings("JavadocReference")
public class StreamParallelForkJoinTests {

	/**
	 * <pre>
	 *   `parallel()` -> `sourceStage.parallel = true`
	 *   `sum()`
	 *     -> {@link java.util.stream.AbstractPipeline#evaluate(java.util.stream.TerminalOp)}
	 *     -> `isParallel() == true`, {@link java.util.stream.ReduceOps.ReduceOp#evaluateParallel(java.util.stream.PipelineHelper, java.util.Spliterator)}
	 *     -> {@link ForkJoinTask#doInvoke()}
	 *     -> {@link ForkJoinTask#externalAwaitDone()}
	 *     -> `ForkJoinPool.common.externalHelpComplete((CountedCompleter<?>)this, 0)`, <b>{@code `this`}</b> equals to {@link java.util.stream.ReduceOps.ReduceTask}
	 * </pre>
	 */
	@Test
	public void parallel(){
		int start = 1, end = 100;
		int expected = (start + end) * end / 2;

		IntStream intStream = IntStream.range(start, end + 1);
		int actual = intStream.parallel().sum();

		Assertions.assertEquals(expected, actual);
	}

	/**
	 * <pre>
	 *   `Collectors.toList()`: 不支持concurrent
	 * </pre>
	 *
	 * @see java.util.stream.ReferencePipeline#collect(java.util.stream.Collector)
	 */
	@Test
	public void parallelMap(){
		final Stream<Integer> stream = Stream.of(1, 2, 3);

		final Set<String> strings = stream.parallel()
									.map(integer -> "s" + integer)
									.collect(Collectors.toSet());

		System.out.println(strings);
	}
}
