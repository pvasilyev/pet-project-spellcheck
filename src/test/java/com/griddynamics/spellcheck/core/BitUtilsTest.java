package com.griddynamics.spellcheck.core;

import junit.framework.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author pvasilyev
 * @since 30 Oct 2013
 */
public class BitUtilsTest {

    @Test
    public void testEncoding2ByteArray() throws Exception {
        byte[] bits = new byte[] {15, 10};

        final long encode = BitUtils.encode(bits);
        final String asBinaryString = Long.toBinaryString(encode);

        Assert.assertNotNull(asBinaryString);
//                           10     15
        Assert.assertEquals("1010"+"1111", asBinaryString);
    }

    @Test
    public void testEncoding8ByteArray() throws Exception {
        byte[] bits = new byte[] {1, 3, 5, 7, 9, 11, 13, 15};

        final long encode = BitUtils.encode(bits);
        final String asBinaryString = Long.toBinaryString(encode);

        Assert.assertNotNull(asBinaryString);
//                           15     13     11     9      7      5      3      1
        Assert.assertEquals("1111" + "1101" + "1011" + "1001" + "0111" + "0101" + "0011" + "0001", asBinaryString);
    }

    @Test
    public void testEncoding16ByteArray() throws Exception {
        byte[] bits = new byte[] {1, 3, 5, 7, 9, 11, 13, 15, 0, 2, 4, 6, 8, 10, 12, 14};

        final long encode = BitUtils.encode(bits);
        final String asBinaryString = Long.toBinaryString(encode);

        Assert.assertNotNull(asBinaryString);
//                           14     12     10     8      6      4      2      0      15     13     11     9      7      5      3      1
        Assert.assertEquals("1110"+"1100"+"1010"+"1000"+"0110"+"0100"+"0010"+"0000"+"1111"+"1101"+"1011"+"1001"+"0111"+"0101"+"0011"+"0001", asBinaryString);
    }

    @Test
    public void testRandom16ByteArray() throws Exception {
        byte[] bits = new byte[BitUtils.MAGIC_CONSTANT];
        final Random random = new Random(0xBADBEE);
        for (int k = 0; k < 100; ++k) {
            testRandomArray(bits, random);
        }
    }

    private void testRandomArray(final byte[] bits, final Random random) {
        random.nextBytes(bits);
        for (int i = 0; i < bits.length; i++) {
            bits[i] = (byte)(Math.abs(bits[i]) % BitUtils.MAGIC_CONSTANT);
        }

        final long encode = BitUtils.encode(bits);
        String asBinaryString = Long.toBinaryString(encode);

        Assert.assertNotNull(asBinaryString);
        for (int i = 0; i < Long.numberOfLeadingZeros(encode); ++i) {
            asBinaryString = "0" + asBinaryString;
        }

        for (int i = 0; i < bits.length; i++) {
            byte bit = bits[i];
            Assert.assertTrue(bit < BitUtils.MAGIC_CONSTANT);
            String bitAsBinaryString = Integer.toBinaryString(bit);
            final int length = bitAsBinaryString.length();
            for (int k = 0; k < 4 - length; ++k) {
                bitAsBinaryString = "0" + bitAsBinaryString;
            }
            Assert.assertEquals(4, bitAsBinaryString.length());

            final String substring = asBinaryString.substring(asBinaryString.length() - (i + 1) * 4, asBinaryString.length() - i * 4);
            Assert.assertEquals(4, substring.length());

            Assert.assertEquals(bitAsBinaryString, substring);
        }
    }

    @Test
    public void testTwoHistogramsComparisonIsNotNegative() throws Exception {
        final Random random = new Random(0xBADBEE);
        for (int i = 0; i < 100; ++i) {
            long firstCandidate  = random.nextLong();
            long secondCandidate = random.nextLong();

            long first  = new BigInteger(Long.toBinaryString(firstCandidate), 2).longValue();
            long second = new BigInteger(Long.toBinaryString(secondCandidate), 2).longValue();
            Assert.assertTrue("firstCandidate=" + firstCandidate + "\n"
                            + "secondCandidate=" + secondCandidate + "\n",
                    BitUtils.compareTwoHistograms(first, second) >= 0);
        }
    }

    @Test
    public void testTwoHistogramsComparison() throws Exception {
        long first  = parseLong("0001 0000 0000 0000 0010 0000 0000 0000 0000 0000 0000 0000 0000 0001 0000 0000");
        long second = parseLong("0001 0000 0000 0000 0010 0000 0000 0000 0000 0001 0000 0000 0000 0000 0000 0000");
        Assert.assertEquals(2, BitUtils.compareTwoHistograms(first, second));

        first  = parseLong("0001 0000 0000 0000 0010 0000 0000 0000 0000 0100 0000 0000 0000 0100 0000 0000");
        second = parseLong("0001 0000 0000 0000 0010 0000 0000 0000 0000 0100 0000 0000 0000 0100 0000 0000");
        Assert.assertEquals(0, BitUtils.compareTwoHistograms(first, second));

        first  = parseLong("1001 0000 0000 0000 0010 0001 0000 0000 0000 0100 0010 0000 0000 0100 0000 0000");
        second = parseLong("1001 0000 0000 0000 0010 0000 0000 0000 0001 0100 0000 0000 0000 0100 0000 0000");
        Assert.assertEquals(4, BitUtils.compareTwoHistograms(first, second));
    }

    private static long parseLong(final String asBinaryString) {
        return new BigInteger(asBinaryString.replaceAll(" ", ""), 2).longValue();
    }
}
