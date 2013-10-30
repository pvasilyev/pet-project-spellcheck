package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class SpellCheckEngineImpl extends AbstractSpellCheckEngine {

    private long[] histograms;

    @Override
    public String[] suggestSimilar(final String word, final int suggestionsNumber, final float accuracy) {
        return new String[0];
    }

    @Override
    public void indexDictionary(final Dictionary dictionary) {
        super.indexDictionary(dictionary);
        final String[] words = dictionary.getWords();
        histograms = new long[words.length];
        for (int i = 0; i < words.length; i++) {
            histograms[i] = BitUtils.encode(words[i]);
        }
    }

    public long[] getHistograms() {
        return histograms;
    }

}
