package com.griddynamics.spellcheck.benchmark;

import com.griddynamics.spellcheck.core.LuceneSpellCheckEngine;
import com.griddynamics.spellcheck.core.SpellCheckEngineImpl;
import com.griddynamics.spellcheck.generator.ManglingUtils;
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
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 5, time = 5000, timeUnit = TimeUnit.NANOSECONDS)
@Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.NANOSECONDS)
public class JMHTest {

    private static final int MAX_SIZE = 1000;

    private SpellCheckEngineImpl engineToTest;
    private LuceneSpellCheckEngine luceneEngine;
    private String[] inputQueries;

    Random random;
    int pointer;

    @Setup
    public void setUp() throws IOException {
        engineToTest = new SpellCheckEngineImpl();
        luceneEngine = new LuceneSpellCheckEngine();
        final DictionaryLoader dictionaryLoader = new DictionaryLoader();
        final Dictionary dictionary = dictionaryLoader.reload("/com/griddynamics/spellcheck/warehouse/dictionary.txt");
        engineToTest.indexDictionary(dictionary);
        luceneEngine.indexDictionary(dictionary);

        random = new Random();
        inputQueries = generateInputQueries(random, dictionary);
        pointer = 0;
    }

    private String[] generateInputQueries(final Random random, final Dictionary dictionary) {
        final String[] strings = new String[MAX_SIZE];
        for (int i = 0; i < strings.length; i++) {
            final int id = random.nextInt(dictionary.size());
            strings[i] = ManglingUtils.mangleWordRandomly(dictionary.getWordByID(id), random, 2, 5);
        }
        return strings;
    }

    @GenerateMicroBenchmark
    @Group("g_new_thread")
    public String[] measurePerformance1() {
        final String[] strings = engineToTest.suggestSimilar(inputQueries[pointer++], 10);

        if (pointer >= MAX_SIZE) {
            pointer = 0;
        }

        return strings;
    }


//    @GenerateMicroBenchmark
//    @Group("g_new_thread")
//    public String[] measurePerformance2() {
//        final String[] strings = engineToTest.suggestSimilar(inputQueries[pointer++], 10);
//
//        if (pointer >= MAX_SIZE) {
//            pointer = 0;
//        }
//
//        return strings;
//    }
//
//    @GenerateMicroBenchmark
//    @Group("g_new_thread")
//    public String[] measurePerformance3() {
//        final String[] strings = engineToTest.suggestSimilar(inputQueries[pointer++], 10);
//
//        if (pointer >= MAX_SIZE) {
//            pointer = 0;
//        }
//
//        return strings;
//    }
//
//    @GenerateMicroBenchmark
//    @Group("g_new_thread")
//    public String[] measurePerformance4() {
//        final String[] strings = engineToTest.suggestSimilar(inputQueries[pointer++], 10);
//
//        if (pointer >= MAX_SIZE) {
//            pointer = 0;
//        }
//
//        return strings;
//    }

    @GenerateMicroBenchmark
    @Group("g_lucene_thread")
    public String[] measureLucenePerformance1() {
        final String[] strings = luceneEngine.suggestSimilar(inputQueries[pointer++], 10);

        if (pointer >= MAX_SIZE) {
            pointer = 0;
        }

        return strings;
    }


//    @GenerateMicroBenchmark
//    @Group("g_lucene_thread")
//    public String[] measureLucenePerformance2() {
//        final String[] strings = luceneEngine.suggestSimilar(inputQueries[pointer++], 10);
//
//        if (pointer >= MAX_SIZE) {
//            pointer = 0;
//        }
//
//        return strings;
//    }
//
//    @GenerateMicroBenchmark
//    @Group("g_lucene_thread")
//    public String[] measureLucenePerformance3() {
//        final String[] strings = luceneEngine.suggestSimilar(inputQueries[pointer++], 10);
//
//        if (pointer >= MAX_SIZE) {
//            pointer = 0;
//        }
//
//        return strings;
//    }
//
//    @GenerateMicroBenchmark
//    @Group("g_lucene_thread")
//    public String[] measureLucenePerformance4() {
//        final String[] strings = luceneEngine.suggestSimilar(inputQueries[pointer++], 10);
//
//        if (pointer >= MAX_SIZE) {
//            pointer = 0;
//        }
//
//        return strings;
//    }

}

