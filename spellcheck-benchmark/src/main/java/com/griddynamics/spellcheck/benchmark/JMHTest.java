package com.griddynamics.spellcheck.benchmark;

import com.griddynamics.spellcheck.core.SpellCheckEngineImpl;
import com.griddynamics.spellcheck.warehouse.Dictionary;
import com.griddynamics.spellcheck.warehouse.DictionaryLoader;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author pvasilyev
 * @since 01 Nov 2013
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 5000, timeUnit = TimeUnit.NANOSECONDS)
@Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.NANOSECONDS)
public class JMHTest {

    private static final int MAX_SIZE = 50000;
    private static final long SEED = 0xBADBEE;

    private SpellCheckEngineImpl engineToTest;

    Random random;
    long[] array;
    int guard;
    int[] result;
    int pointer;

    @Setup
    public void setUp() throws IOException {
        random = new Random(SEED);
        array = initializeArray();
        guard = random.nextInt();
        result = new int[MAX_SIZE];
        pointer = 0;

        engineToTest = new SpellCheckEngineImpl();
        final DictionaryLoader dictionaryLoader = new DictionaryLoader();
        final Dictionary dictionary = dictionaryLoader.reload("/com/griddynamics/spellcheck/warehouse/dictionary.txt");
        engineToTest.indexDictionary(dictionary);

    }

    private long[] initializeArray() {
        final long[] longs = new long[MAX_SIZE];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = random.nextLong();
        }
        return longs;
    }

    @GenerateMicroBenchmark
    @Group("g_1_thread")
    public int measurePerformance() {
        pointer = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] % this.guard == 0) {
                result[pointer++] = i;
            }
        }

        return pointer;
    }

    @GenerateMicroBenchmark
    @Group("g_1_thread_inverse")
    public int measurePerformanceInverse() {
        pointer = 0;
        for (int i = array.length-1; i > 0; i--) {
            if (array[i] % this.guard == 0) {
                result[pointer++] = i;
            }
        }

        return pointer;
    }


}

