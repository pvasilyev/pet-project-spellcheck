package com.griddynamics.spellcheck.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author pvasilyev
 * @since 01 Nov 2013
 */
public final class ManglingUtils {

    private static List<StrategyToMangle> STRATEGIES_TO_MANGLE = Arrays.asList(
            new RemoveStrategy(),
            new InsertStrategy(),
            new SetCharAtStrategy()
    );

    public static String mangleWordRandomly(final String wordToMangle, final Random random, final int minTypos, final int maxTypos) {
        final StringBuilder stringBuilder = new StringBuilder(wordToMangle);
        int typosToMake = random.nextInt(maxTypos - minTypos) + minTypos;
        if (wordToMangle.length() <= 3) {
            return wordToMangle;
        }
        if (wordToMangle.length() <= 7) {
            typosToMake = 1;
        }
        for (int i = 0; i < typosToMake; ) {
            final StrategyToMangle strategyToMangle = STRATEGIES_TO_MANGLE.get(random.nextInt(STRATEGIES_TO_MANGLE.size()));
            strategyToMangle.mangle(stringBuilder, random);
            i += strategyToMangle.weight();
        }
        return stringBuilder.toString();
    }

    private ManglingUtils() {}

}
