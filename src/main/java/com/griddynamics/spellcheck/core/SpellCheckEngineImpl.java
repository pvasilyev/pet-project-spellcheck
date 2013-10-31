package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class SpellCheckEngineImpl extends AbstractSpellCheckEngine {

    private static final float EPS = 1e-6F;

    private long[] histograms;

    @Override
    public String[] suggestSimilar(final String word, final int suggestionsNumber, final float accuracy) {
        final ArrayList<Integer> wordsIds = new ArrayList<>(suggestionsNumber * 10);
//        int[] wordsIds = new int[suggestionsNumber * 10];
//        int wordPointer = 0;
        final long wordHistogram = BitUtils.encode(word);
        final int maxEditDist = (int)Math.round(Math.floor((1-accuracy) * word.length()));
        for (int i = 0; i < histograms.length; i++) {
            long currentHistogram = histograms[i];
            if (BitUtils.compareTwoHistograms(wordHistogram, currentHistogram) <= maxEditDist) {
                wordsIds.add(i);
            }
        }

        return postProcess(word, suggestionsNumber, accuracy, wordsIds);
    }

    private String[] postProcess(final String word, final int suggestionsNumber,
                                          final float accuracy, final ArrayList<Integer> wordsIds) {
        final int size = Math.min(suggestionsNumber, wordsIds.size());
        if (size == 0) {
            return new String[] {};
        }
        final PriorityQueue<String> result = new PriorityQueue<>(size, new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return Float.compare(
                        levensteinDistance.getDistance(word, o1),
                        levensteinDistance.getDistance(word, o2)
                );
            }
        });
        int actualSize = 0;
        for (final Integer wordId : wordsIds) {
            final String other = dictionary.getWordByID(wordId);
            if (levensteinDistance.getDistance(word, other) > accuracy - EPS) {
                result.offer(other);
                actualSize++;
            }
        }

        String[] resultArray = result.toArray(new String[size]);
        if (actualSize == size) {
            return resultArray;
        } else {
            return Arrays.copyOf(resultArray, actualSize);
        }

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
