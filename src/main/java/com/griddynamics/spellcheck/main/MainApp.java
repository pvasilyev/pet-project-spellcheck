package com.griddynamics.spellcheck.main;

import com.griddynamics.spellcheck.core.NaiveSpellCheckEngine;

import java.io.File;
import java.io.IOException;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class MainApp {

    public static void main(String[] args) throws IOException {
        final File file = new File("/Users/nepank/Documents/grid/pet-project-spellcheck/index");
        final NaiveSpellCheckEngine engine = new NaiveSpellCheckEngine();

    }

}
