package com.griddynamics.spellcheck.core;

import com.griddynamics.spellcheck.warehouse.Dictionary;
import com.griddynamics.spellcheck.warehouse.DictionaryLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author pvasilyev
 * @since 30 Oct 2013
 */
public class SpellCheckEngineImplTest {

    private SpellCheckEngineImpl engineToTest;

    @Before
    public void setUp() throws Exception {
        engineToTest = new SpellCheckEngineImpl();
    }

    @Test
    public void testHistogramsWereLoaded() throws Exception {
        final DictionaryLoader dictionaryLoader = new DictionaryLoader();
        final Dictionary dictionary = dictionaryLoader.reload("/com/griddynamics/spellcheck/core/dictionary-for-histograms.txt");
        final String[] words = dictionary.getWords();
        Assert.assertNotNull(words);
        Assert.assertArrayEquals(new String[]{"browse", "circumstances", "synchronization", "unconstitutional"}, words);
    }

    @Test
    public void testHistogramsWereEncoded() throws Exception {
        final DictionaryLoader dictionaryLoader = new DictionaryLoader();
        final Dictionary dictionary = dictionaryLoader.reload("/com/griddynamics/spellcheck/core/dictionary-for-histograms.txt");
        engineToTest.indexDictionary(dictionary);

        final long[] histograms = engineToTest.getHistograms();
        Assert.assertNotNull(histograms);
        Assert.assertEquals(4, histograms.length);

//        "browse" string:

//        char=b, ASCII code=98, {mod 16=2}
//        char=r, ASCII code=114, {mod 16=2}
//        char=o, ASCII code=111, {mod 16=15}
//        char=w, ASCII code=119, {mod 16=7}
//        char=s, ASCII code=115, {mod 16=3}
//        char=e, ASCII code=101, {mod 16=5}

//        mod 16: 0 = 0 => "0000"
//        mod 16: 1 = 0 => "0000"
//        mod 16: 2 = 2 => "0010"
//        mod 16: 3 = 1 => "0001"
//        mod 16: 4 = 0 => "0000"
//        mod 16: 5 = 1 => "0001"
//        mod 16: 6 = 0 => "0000"
//        mod 16: 7 = 1 => "0001"
//        mod 16: 8 = 0 => "0000"
//        mod 16: 9 = 0 => "0000"
//        mod 16: 10 = 0 => "0000"
//        mod 16: 11 = 0 => "0000"
//        mod 16: 12 = 0 => "0000"
//        mod 16: 13 = 0 => "0000"
//        mod 16: 14 = 0 => "0000"
//        mod 16: 15 = 1 => "0001"
        Assert.assertEquals("1"+"0000"+"0000"+"0000"+"0000"+"0000"+"0000"+"0000"+"0001"+"0000"+"0001"+"0000"+"0001"+"0010"+"0000"+"0000", Long.toBinaryString(histograms[0]));

//        "circumstances" string

//        char=c, ASCII code=99, {mod 16=3}
//        char=i, ASCII code=105, {mod 16=9}
//        char=r, ASCII code=114, {mod 16=2}
//        char=c, ASCII code=99, {mod 16=3}
//        char=u, ASCII code=117, {mod 16=5}
//        char=m, ASCII code=109, {mod 16=13}
//        char=s, ASCII code=115, {mod 16=3}
//        char=t, ASCII code=116, {mod 16=4}
//        char=a, ASCII code=97, {mod 16=1}
//        char=n, ASCII code=110, {mod 16=14}
//        char=c, ASCII code=99, {mod 16=3}
//        char=e, ASCII code=101, {mod 16=5}
//        char=s, ASCII code=115, {mod 16=3}

//        mod 16: 0 = 0 => "0000"
//        mod 16: 1 = 1 => "0001"
//        mod 16: 2 = 1 => "0001"
//        mod 16: 3 = 5 => "0101"
//        mod 16: 4 = 1 => "0001"
//        mod 16: 5 = 2 => "0010"
//        mod 16: 6 = 0 => "0000"
//        mod 16: 7 = 0 => "0000"
//        mod 16: 8 = 0 => "0000"
//        mod 16: 9 = 1 => "0001"
//        mod 16: 10 = 0 => "0000"
//        mod 16: 11 = 0 => "0000"
//        mod 16: 12 = 0 => "0000"
//        mod 16: 13 = 1 => "0001"
//        mod 16: 14 = 1 => "0001"
//        mod 16: 15 = 0 => "0000"
        Assert.assertEquals("1"+"0001"+"0000"+"0000"+"0000"+"0001"+"0000"+"0000"+"0000"+"0010"+"0001"+"0101"+"0001"+"0001"+"0000", Long.toBinaryString(histograms[1]));

//        "synchronization" string

//        char=s, ASCII code=115, {mod 16=3}
//        char=y, ASCII code=121, {mod 16=9}
//        char=n, ASCII code=110, {mod 16=14}
//        char=c, ASCII code=99, {mod 16=3}
//        char=h, ASCII code=104, {mod 16=8}
//        char=r, ASCII code=114, {mod 16=2}
//        char=o, ASCII code=111, {mod 16=15}
//        char=n, ASCII code=110, {mod 16=14}
//        char=i, ASCII code=105, {mod 16=9}
//        char=z, ASCII code=122, {mod 16=10}
//        char=a, ASCII code=97, {mod 16=1}
//        char=t, ASCII code=116, {mod 16=4}
//        char=i, ASCII code=105, {mod 16=9}
//        char=o, ASCII code=111, {mod 16=15}
//        char=n, ASCII code=110, {mod 16=14}

//        mod 16: 0 = 0 => "0000"
//        mod 16: 1 = 1 => "0001"
//        mod 16: 2 = 1 => "0001"
//        mod 16: 3 = 2 => "0010"
//        mod 16: 4 = 1 => "0001"
//        mod 16: 5 = 0 => "0000"
//        mod 16: 6 = 0 => "0000"
//        mod 16: 7 = 0 => "0000"
//        mod 16: 8 = 1 => "0001"
//        mod 16: 9 = 3 => "0011"
//        mod 16: 10 = 1 => "0001"
//        mod 16: 11 = 0 => "0000"
//        mod 16: 12 = 0 => "0000"
//        mod 16: 13 = 0 => "0000"
//        mod 16: 14 = 3 => "0011"
//        mod 16: 15 = 2 => "0010"
        Assert.assertEquals("10"+"0011"+"0000"+"0000"+"0000"+"0001"+"0011"+"0001"+"0000"+"0000"+"0000"+"0001"+"0010"+"0001"+"0001"+"0000", Long.toBinaryString(histograms[2]));

//        "unconstitutional" string

//        char=u, ASCII code=117, {mod 16=5}
//        char=n, ASCII code=110, {mod 16=14}
//        char=c, ASCII code=99, {mod 16=3}
//        char=o, ASCII code=111, {mod 16=15}
//        char=n, ASCII code=110, {mod 16=14}
//        char=s, ASCII code=115, {mod 16=3}
//        char=t, ASCII code=116, {mod 16=4}
//        char=i, ASCII code=105, {mod 16=9}
//        char=t, ASCII code=116, {mod 16=4}
//        char=u, ASCII code=117, {mod 16=5}
//        char=t, ASCII code=116, {mod 16=4}
//        char=i, ASCII code=105, {mod 16=9}
//        char=o, ASCII code=111, {mod 16=15}
//        char=n, ASCII code=110, {mod 16=14}
//        char=a, ASCII code=97, {mod 16=1}
//        char=l, ASCII code=108, {mod 16=12}

//        mod 16: 0 = 0 => "0000"
//        mod 16: 1 = 1 => "0001"
//        mod 16: 2 = 0 => "0000"
//        mod 16: 3 = 2 => "0010"
//        mod 16: 4 = 3 => "0011"
//        mod 16: 5 = 2 => "0010"
//        mod 16: 6 = 0 => "0000"
//        mod 16: 7 = 0 => "0000"
//        mod 16: 8 = 0 => "0000"
//        mod 16: 9 = 2 => "0010"
//        mod 16: 10 = 0 => "0000"
//        mod 16: 11 = 0 => "0000"
//        mod 16: 12 = 1 => "0001"
//        mod 16: 13 = 0 => "0000"
//        mod 16: 14 = 3 => "0011"
//        mod 16: 15 = 2 => "0010"
        Assert.assertEquals("10"+"0011"+"0000"+"0001"+"0000"+"0000"+"0010"+"0000"+"0000"+"0000"+"0010"+"0011"+"0010"+"0000"+"0001"+"0000", Long.toBinaryString(histograms[3]));

    }
}
