package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.search.suggest.FileDictionary;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class LuceneSpellCheckEngine extends AbstractSpellCheckEngine {

    private SpellChecker spellChecker;

    public LuceneSpellCheckEngine() throws IOException {
        final Directory directory = new RAMDirectory();
        spellChecker = new SpellChecker(directory, levensteinDistance);
    }

    @Override
    public void indexDictionary(final Dictionary dictionary) {
        super.indexDictionary(dictionary);
        final FileDictionary fileDictionary = new FileDictionary(dictionary.getDictionaryResource());
        final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_45, null);
        try {
            spellChecker.indexDictionary(fileDictionary, indexWriterConfig, true);
        } catch (IOException e) {
            throw new RuntimeException("Can't index new dictionary.", e);
        }
    }

    @Override
    public String[] suggestSimilar(final String word, final int maxSuggestionsNumber, final float accuracy) {
        try {
            return spellChecker.suggestSimilar(word, maxSuggestionsNumber, accuracy);
        } catch (IOException e) {
            return new String[]{};
        }
    }

}
