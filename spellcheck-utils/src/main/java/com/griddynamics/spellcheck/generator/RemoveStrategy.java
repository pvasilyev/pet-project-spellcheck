package com.griddynamics.spellcheck.generator;

import java.util.Random;

/**
 * @author pvasilyev
 * @since 01 Nov 2013
 */
public class RemoveStrategy implements StrategyToMangle {

    @Override
    public StringBuilder mangle(final StringBuilder word, final Random random) {
        return word.deleteCharAt(random.nextInt(word.length()));
    }

    @Override
    public int weight() {
        return 1;
    }

}
