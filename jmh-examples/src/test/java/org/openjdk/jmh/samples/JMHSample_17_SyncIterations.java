/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openjdk.jmh.samples;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.TimeUnit;

/**
 * <a href="https://www.jianshu.com/p/a5b9865257f4">JMHSample_17_SyncIterations</a>
 * <p>
 * 本例阐述了在多线程条件下，线程池的启动与销毁都会影响基准测试的准确性，如果自己来实现需要让线程同时开始启动工作，
 * 但这又比较难做到，如果在启动和关闭线程池时，无法做到同时，那么测量必定不准确，因为无法确定开始和结束时间；<br/>
 *
 * JMH提供了多线程基准测试的方法，<b>先让线程池预热，都预热完成后让所有线程同时进行基准测试</b>，测试完等待所有线程都结束再关闭线程池。 <br/>
 *
 * `syncIterations`是否需要<b>同步预热</b>，看了下代码才知道`syncIterations = true`代表等所有线程预热完成，
 * 后所有线程一起进入测量阶段，等所有线程执行完测试后，再一起进入关闭；<br/>
 *
 * 当`syncIterations = true`时<b>更准确地</b>反应了多线程下被测试方法的性能，这个参数默认为true，无需手动设置。
 * </p>
 *
 * 个人：
 * 1) 文章说 `syncIterations`指的是 同步预热。 “都预热完成后让所有线程同时进行基准测试”！
 *
 * @author vergilyn
 * @since 2022-02-24
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JMHSample_17_SyncIterations {

    /*
     * This is the another thing that is enabled in JMH by default.
     *
     * Suppose we have this simple benchmark.
     */

    private double src;

    @Benchmark
    public double test() {
        double s = src;
        for (int i = 0; i < 1000; i++) {
            s = Math.sin(s);
        }
        return s;
    }

    /*
     * It turns out if you run the benchmark with multiple threads,
     * the way you start and stop the worker threads seriously affects
     * performance.
     *
     * The natural way would be to park all the threads on some sort
     * of barrier, and the let them go "at once". However, that does
     * not work: there are no guarantees the worker threads will start
     * at the same time, meaning other worker threads are working
     * in better conditions, skewing the result.
     *
     * The better solution would be to introduce bogus iterations,
     * ramp up the threads executing the iterations, and then atomically
     * shift the system to measuring stuff. The same thing can be done
     * during the rampdown. This sounds complicated, but JMH already
     * handles that for you.
     *

     */

    /*
     * ============================== HOW TO RUN THIS TEST: ====================================
     *
     * You will need to oversubscribe the system to make this effect
     * clearly visible; however, this effect can also be shown on the
     * unsaturated systems.*
     *
     * Note the performance of -si false version is more flaky, even
     * though it is "better". This is the false improvement, granted by
     * some of the threads executing in solo. The -si true version more stable
     * and coherent.
     *
     * -si true is enabled by default.
     *
     * Say, $CPU is the number of CPUs on your machine.
     *
     * You can run this test with:
     *
     * a) Via the command line:
     *    $ mvn clean install
     *    $ java -jar target/benchmarks.jar JMHSample_17 \
     *        -w 1s -r 1s -f 1 -t ${CPU*16} -si {true|false}
     *    (we requested shorter warmup/measurement iterations, single fork,
     *     lots of threads, and changeable "synchronize iterations" option)
     *
     * b) Via the Java API:
     *    (see the JMH homepage for possible caveats when running from IDE:
     *      http://openjdk.java.net/projects/code-tools/jmh/)
     */

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHSample_17_SyncIterations.class.getSimpleName())
                .warmupTime(TimeValue.seconds(1))
                .measurementTime(TimeValue.seconds(1))
                .threads(Runtime.getRuntime().availableProcessors()*16)
                .forks(1)
                .syncIterations(true) // try to switch to "false"
                .build();

        new Runner(opt).run();
    }

}