package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;
import com.griddynamics.spellcheck.warehouse.DictionaryLoader;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author pvasilyev
 * @since 31 Oct 2013
 */
public class ComparativeSpellCheckTest {

    private static List<StrategyToMangle> STRATEGIES_TO_MANGLE = Arrays.asList(
            new RemoveStrategy(),
            new InsertStrategy(),
            new SetCharAtStrategy()
    );

    private Dictionary dictionary;
    private SpellCheckEngineImpl engineToTest;
    private NaiveSpellCheckEngine naiveSpellCheckEngine;
    private Random random;


    @Before
    public void setUp() throws Exception {
        engineToTest = new SpellCheckEngineImpl();
        naiveSpellCheckEngine = new NaiveSpellCheckEngine();

        final DictionaryLoader dictionaryLoader = new DictionaryLoader();
        dictionary = dictionaryLoader.reload("/com/griddynamics/spellcheck/warehouse/spellcheck.txt");

        engineToTest.indexDictionary(dictionary);
        naiveSpellCheckEngine.indexDictionary(dictionary);

        random = new Random(0xBADBEE);
    }

    @Test
    public void testRandomly1_3_Typos() throws Exception {
        int dictionarySize = dictionary.size();
        final int wordLenThreshold = 3;
        final float accuracy = 0.75F;
        final float weakenAccuracy = 0.6F;
        final int suggestionsNumber = 10;
        final int minTypos = 1;
        final int maxTypos = 3;

        runRandomly(dictionarySize, wordLenThreshold, accuracy, weakenAccuracy, suggestionsNumber, minTypos, maxTypos);
    }

    @Test
    public void testRandomly4_6_Typos() throws Exception {
        int dictionarySize = dictionary.size();
        final int wordLenThreshold = 8;
        final float accuracy = 0.6F;
        final float weakenAccuracy = 0.4F;
        final int suggestionsNumber = 10;
        final int minTypos = 4;
        final int maxTypos = 6;

        runRandomly(dictionarySize, wordLenThreshold, accuracy, weakenAccuracy, suggestionsNumber, minTypos, maxTypos);
    }

    @Test
    public void testRandomly7_9_Typos() throws Exception {
        int dictionarySize = dictionary.size();
        final int wordLenThreshold = 11;
        final float accuracy = 0.4F;
        final float weakenAccuracy = 0.2F;
        final int suggestionsNumber = 10;
        final int minTypos = 7;
        final int maxTypos = 9;

        runRandomly(dictionarySize, wordLenThreshold, accuracy, weakenAccuracy, suggestionsNumber, minTypos, maxTypos);
    }

    private void runRandomly(final int dictionarySize, final int wordLenThreshold, final float accuracy, final float weakenAccuracy,
                             final int suggestionsNumber, final int minTypos, final int maxTypos) {
        final int iterations = random.nextInt(500) + 400;
        for (int i = 0; i < iterations; ++i) {
            final String wordToMangle = dictionary.getWordByID(random.nextInt(dictionarySize));
            if (wordToMangle.length() <= wordLenThreshold) {
                continue;
            }

            final String mangled = mangleWordRandomly(wordToMangle, random, minTypos, maxTypos);
            String messageFormat = "Engine={0}, For mangled word={1}, Cant find initial value={2}, In suggestions={3}";

            final String[] naiveSuggestions = naiveSpellCheckEngine.suggestSimilar(mangled, suggestionsNumber, accuracy);
            final boolean naiveContains = makeAssertionsInNaiveSuggestions(wordToMangle, mangled, messageFormat, naiveSuggestions);

            String[] enginesSuggestions = engineToTest.suggestSimilar(mangled, suggestionsNumber, accuracy);
            boolean contains = makeAssertionsForSpellCheckEngine(weakenAccuracy, suggestionsNumber, wordToMangle, mangled, messageFormat, enginesSuggestions);

            if (naiveContains) {
                Assert.assertTrue(contains);
            }
        }
    }

    private boolean makeAssertionsForSpellCheckEngine(final float weakenAccuracy, final int suggestionsNumber, final String wordToMangle, final String mangled, final String messageFormat, String[] enginesSuggestions) {
        Assert.assertNotNull(enginesSuggestions);
        boolean contains = Arrays.asList(enginesSuggestions).contains(wordToMangle);
        if (!contains) {
            System.err.println("1. First Check " + MessageFormat.format(messageFormat, "newEngine", mangled, wordToMangle, Arrays.toString(enginesSuggestions)));
            enginesSuggestions = engineToTest.suggestSimilar(mangled, suggestionsNumber, weakenAccuracy);
            contains = Arrays.asList(enginesSuggestions).contains(wordToMangle);
            Assert.assertTrue(
                    "2. Weaken Check" + MessageFormat.format(messageFormat, "newEngine", mangled, wordToMangle, Arrays.toString(enginesSuggestions)),
                    contains);
        }
        return contains;
    }

    private boolean makeAssertionsInNaiveSuggestions(final String wordToMangle, final String mangled, final String messageFormat, final String[] naiveSuggestions) {
        Assert.assertNotNull(naiveSuggestions);

        final boolean naiveContains = Arrays.asList(naiveSuggestions).contains(wordToMangle);
        if (!naiveContains) {
            System.err.println(MessageFormat.format(messageFormat, "naive", mangled, wordToMangle, Arrays.toString(naiveSuggestions)));
        }
        return naiveContains;
    }

    private static String mangleWordRandomly(final String wordToMangle, final Random random, final int minTypos, final int maxTypos) {
        final StringBuilder stringBuilder = new StringBuilder(wordToMangle);
        int typosToMake = random.nextInt(maxTypos - minTypos) + minTypos;
        if (wordToMangle.length() <= 3) {
            return wordToMangle;
        }
        if (wordToMangle.length() <= 7) {
            typosToMake = 1;
        }
        for (int i = 0; i < typosToMake;) {
            final StrategyToMangle strategyToMangle = STRATEGIES_TO_MANGLE.get(random.nextInt(STRATEGIES_TO_MANGLE.size()));
            strategyToMangle.mangle(stringBuilder, random);
            i += strategyToMangle.weight();
        }
         return stringBuilder.toString();
    }

    private static interface StrategyToMangle {
        StringBuilder mangle(StringBuilder word, Random random);
        int weight();
    }

    private static final class RemoveStrategy implements StrategyToMangle {

        @Override
        public StringBuilder mangle(final StringBuilder word, final Random random) {
            return word.deleteCharAt(random.nextInt(word.length()));
        }

        @Override
        public int weight() {
            return 1;
        }
    }

    private static abstract class AbstractCharAwareStrategy implements StrategyToMangle {
        protected ArrayList<Character> charsToInsert = new ArrayList<>();

        public AbstractCharAwareStrategy() {
            for (int i = 0; i < 26; ++i) {
                charsToInsert.add((char)('a' + i));
            }
            for (int i = 0; i < 10; ++i) {
                charsToInsert.add((i+"").toCharArray()[0]);
            }
            charsToInsert.addAll(Arrays.asList(' '));
        }
    }

    private static final class InsertStrategy extends AbstractCharAwareStrategy {

        @Override
        public StringBuilder mangle(final StringBuilder word, final Random random) {

            return word.insert(
                    random.nextInt(word.length()),
                    charsToInsert.get(random.nextInt(charsToInsert.size()))
            );
        }

        @Override
        public int weight() {
            return 1;
        }
    }

    private static final class SetCharAtStrategy extends AbstractCharAwareStrategy {

        @Override
        public StringBuilder mangle(final StringBuilder word, final Random random) {
            word.setCharAt(
                    random.nextInt(word.length()),
                    charsToInsert.get(random.nextInt(charsToInsert.size()))
            );
            return word;
        }

        @Override
        public int weight() {
            return 2;
        }
    }
}
