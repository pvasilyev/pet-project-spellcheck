package com.griddynamics.spellcheck.generator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author pvasilyev
 * @since 01 Nov 2013
 */
public abstract class AbstractCharAwareStrategy implements StrategyToMangle {

    protected ArrayList<Character> charsToInsert = new ArrayList<Character>();

    public AbstractCharAwareStrategy() {
        for (int i = 0; i < 26; ++i) {
            charsToInsert.add((char) ('a' + i));
        }
        for (int i = 0; i < 10; ++i) {
            charsToInsert.add((i + "").toCharArray()[0]);
        }
        charsToInsert.addAll(Arrays.asList(' '));
    }

}
