package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;
import org.apache.lucene.search.spell.LevensteinDistance;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public abstract class AbstractSpellCheckEngine implements SpellCheckEngine {

    protected final LevensteinDistance levensteinDistance = new LevensteinDistance();
    protected Dictionary dictionary;

    @Override
    public void indexDictionary(final Dictionary dictionary) {
        this.dictionary = dictionary;
    }
}
