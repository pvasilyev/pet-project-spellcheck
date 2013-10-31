package com.griddynamics.spellcheck.generator;

import java.util.Random;

/**
 * @author pvasilyev
 * @since 01 Nov 2013
 */
public class SetCharAtStrategy extends AbstractCharAwareStrategy {

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
