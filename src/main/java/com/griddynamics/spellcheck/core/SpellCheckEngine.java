package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public interface SpellCheckEngine {

    String[] suggestSimilar(String word, int suggestionsNumber);

    String[] suggestSimilar(String word, int suggestionsNumber, float accuracy);

    void indexDictionary(Dictionary dictionary);

}
