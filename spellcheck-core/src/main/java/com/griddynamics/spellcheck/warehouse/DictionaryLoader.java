package com.griddynamics.spellcheck.warehouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class DictionaryLoader {

    public static final String RESOURCE_FILE_NAME = "dictionary.txt";

    public Dictionary reload(final String resourceFileName) throws IOException {
        final ArrayList<String> words = new ArrayList<>();
        final BufferedReader input = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(resourceFileName)));
        String next;
        while ((next = input.readLine()) != null) {
            words.add(next);
        }

        return new Dictionary(words, resourceFileName);
    }

}
