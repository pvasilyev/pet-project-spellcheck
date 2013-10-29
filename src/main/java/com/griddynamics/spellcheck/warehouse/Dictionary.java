package com.griddynamics.spellcheck.warehouse;

import java.io.*;
import java.util.ArrayList;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class Dictionary {

    private final ArrayList<String> words;
    private final String resourceFileName;

    public Dictionary(final ArrayList<String> words, final String resourceFileName) {
        this.words = words;
        this.resourceFileName = resourceFileName;
    }

    public String[] getWords() {
        final String[] strings = new String[words.size()];
        return words.toArray(strings);
    }

    public InputStream getDictionaryResource() {
        return this.getClass().getResourceAsStream(resourceFileName);
    }

}
