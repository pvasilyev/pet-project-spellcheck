package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;
import com.griddynamics.spellcheck.warehouse.DictionaryLoader;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author pvasilyev
 * @since 31 Oct 2013
 */
public class SpellCheckEngineImplTest {

    private SpellCheckEngineImpl engineToTest;

    @Before
    public void setUp() throws Exception {
        engineToTest = new SpellCheckEngineImpl();
        final DictionaryLoader dictionaryLoader = new DictionaryLoader();
        final Dictionary dictionary = dictionaryLoader.reload("/com/griddynamics/spellcheck/warehouse/spellcheck.txt");
        engineToTest.indexDictionary(dictionary);
    }

    @Test
    public void simpleSpellCheckTest() throws Exception {
        Assert.assertNotNull(engineToTest);

        final String[] suggests1 = engineToTest.suggestSimilar("goverment", 1);
        Assert.assertNotNull(suggests1);
        Assert.assertEquals(1, suggests1.length);
        Assert.assertEquals("government", suggests1[0]);
    }

    @Test
    public void testSpellCheckWithAccuracy() throws Exception {
        Assert.assertNotNull(engineToTest);

        final String[] suggests1 = engineToTest.suggestSimilar("isatop", 1);
        Assert.assertNotNull(suggests1);
        Assert.assertEquals(0, suggests1.length);

        final String[] suggests2 = engineToTest.suggestSimilar("isatop", 1, 0.5F);
        Assert.assertNotNull(suggests2);
        Assert.assertEquals(1, suggests2.length);
        Assert.assertEquals("isotope", suggests2[0]);
    }

    @Test
    public void testName() throws Exception {
        Assert.assertNotNull(engineToTest);

        final String[] suggests1 = engineToTest.suggestSimilar("cub", 10);
        Assert.assertNotNull(suggests1);
        Assert.assertEquals(1, suggests1.length);
        Assert.assertEquals("club", suggests1[0]);

    }
}
