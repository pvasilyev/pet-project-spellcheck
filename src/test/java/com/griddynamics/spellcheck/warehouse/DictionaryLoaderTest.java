package com.griddynamics.spellcheck.warehouse;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class DictionaryLoaderTest {

    private DictionaryLoader dictionaryLoaderToTest = new DictionaryLoader();

    @Test
    public void simpleDictionaryReloadTest() throws Exception {
        final Dictionary dictionary = dictionaryLoaderToTest.reload("spellcheck.txt");
        final String[] words = dictionary.getWords();
        Assert.assertNotNull(words);
        Assert.assertTrue(words.length > 50);
    }

    @Test(expected = Exception.class)
    public void wrongResourceFileTest() throws Exception {
        dictionaryLoaderToTest.reload("unknown.txt");
    }
}
