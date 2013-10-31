package com.griddynamics.spellcheck.generator;

import java.util.Random;

/**
 * @author pvasilyev
 * @since 01 Nov 2013
 */
public interface StrategyToMangle {

    StringBuilder mangle(StringBuilder word, Random random);

    int weight();

}
