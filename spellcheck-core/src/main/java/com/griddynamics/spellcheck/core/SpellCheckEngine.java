package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public interface SpellCheckEngine {

    String[] suggestSimilar(String word, int maxSuggestionsNumber);

    String[] suggestSimilar(String word, int maxSuggestionsNumber, float accuracy);

    void indexDictionary(Dictionary dictionary);

}
