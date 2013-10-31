package com.griddynamics.spellcheck.generator;

import java.util.Random;

/**
 * @author pvasilyev
 * @since 01 Nov 2013
 */
public class InsertStrategy extends AbstractCharAwareStrategy {

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
