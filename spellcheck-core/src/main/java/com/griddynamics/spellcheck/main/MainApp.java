package com.griddynamics.spellcheck.main;

import com.griddynamics.spellcheck.core.SpellCheckEngineImpl;
import com.griddynamics.spellcheck.warehouse.Dictionary;
import com.griddynamics.spellcheck.warehouse.DictionaryLoader;

import java.io.IOException;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class MainApp {

    public static void main(String[] args) throws IOException {
        printAllHistogramsWithWords();
    }

    private static void printAllHistogramsWithWords() throws IOException {
        final Dictionary dictionary = new DictionaryLoader().reload("/com/griddynamics/spellcheck/warehouse/dictionary.txt");
        final SpellCheckEngineImpl spellCheckEngine = new SpellCheckEngineImpl();
        spellCheckEngine.indexDictionary(dictionary);

        final long[] histograms = spellCheckEngine.getHistograms();
        for (int i = 0; i < histograms.length; i++) {
            final String s = asBinaryWithLeadingZeroes(histograms[i]);
            System.out.println("Histogram : " + s + " for word=" + dictionary.getWordByID(i));
        }
    }

    private static String asBinaryWithLeadingZeroes(final long histogram) {
        final String s = Long.toBinaryString(histogram);
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 64 - s.length(); ++i) {
            stringBuilder.append('0');
        }

        return stringBuilder.append(s).toString();
    }

}
