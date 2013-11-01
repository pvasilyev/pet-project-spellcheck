package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.BitUtils;
import com.griddynamics.spellcheck.warehouse.Dictionary;

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
        final long wordHistogram = BitUtils.encode(word);
        final int maxEditDist = (int)Math.round(Math.ceil((1-accuracy) * word.length()));

        final int maxQueueSize = 10 * suggestionsNumber;
        final PriorityQueue<HistogramDistancePair> queue = new PriorityQueue<HistogramDistancePair>(maxQueueSize, new Comparator<HistogramDistancePair>() {
            @Override
            public int compare(final HistogramDistancePair o1, final HistogramDistancePair o2) {
                final int x = o1.distance;
                final int y = o2.distance;
                return (x < y) ? -1 : ((x == y) ? 0 : 1);
            }
        });
        final int i1 = 2 * maxEditDist;
        for (int i = 0; i < histograms.length; i++) {
            long currentHistogram = histograms[i];
            final int hashDistance = BitUtils.compareTwoHistograms(wordHistogram, currentHistogram);
            if (hashDistance <= i1) {
                queue.offer(new HistogramDistancePair(i, hashDistance));
            }
        }

//        System.err.println("Word length=" + word.length() + ", maxEditDist=" + maxEditDist + ", found wordIds=" + wordsIds.size());
        return postProcess(word, suggestionsNumber, accuracy, queue);
    }

    private String[] postProcess(final String word, final int maxSuggestionsNumber,
                                          final float accuracy, final PriorityQueue<HistogramDistancePair> wordsIds) {
        final int wordIdsSize = wordsIds.size();
        final int size = Math.min(maxSuggestionsNumber, wordIdsSize);

        if (size == 0) {
            return new String[] {};
        }
        WordDistancePair[] pairs = new WordDistancePair[wordIdsSize];
        for (int i = 0; i < wordIdsSize; i++) {
            final String currentWord = dictionary.getWordByID(wordsIds.poll().id);
            final float distance = levensteinDistance.getDistance(word, currentWord);
//            System.err.println("Levenstein Distance: source=" + word + ", target=" + currentWord + ", dist=" + distance);
            pairs[i]  = new WordDistancePair(currentWord, distance);
        }
        final PriorityQueue<WordDistancePair> queue = new PriorityQueue<WordDistancePair>(size, new Comparator<WordDistancePair>() {
            @Override
            public int compare(final WordDistancePair o1, final WordDistancePair o2) {
                return Float.compare(o2.distance, o1.distance);
            }
        });

        int actualSize = 0;
        for (int i = 0; i < pairs.length; ++i) {
            if (pairs[i].distance > accuracy - EPS) {
                queue.offer(pairs[i]);
                actualSize++;
            }
        }
        String[] resultArray = new String[actualSize];
        for (int i = 0; i < resultArray.length; i++) {
            resultArray[i] = queue.poll().word;
        }
        return resultArray;
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

    private static final class WordDistancePair {
        String word;
        float distance;

        private WordDistancePair(final String word, final float distance) {
            this.word = word;
            this.distance = distance;
        }
    }

    private static final class HistogramDistancePair {
        int id;
        int distance;

        private HistogramDistancePair(final int id, final int distance) {
            this.id = id;
            this.distance = distance;
        }
    }

}
