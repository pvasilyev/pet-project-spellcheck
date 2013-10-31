package com.griddynamics.spellcheck;

/**
 * @author pvasilyev
 * @since 30 Oct 2013
 */
public final class BitUtils {

    static final int MAGIC_CONSTANT = 16;

    private BitUtils() {}

    public static long encode(final String word) {
        byte[] bits = new byte[MAGIC_CONSTANT];
        final char[] chars = word.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            bits[chars[i] % MAGIC_CONSTANT]++;
        }

        return encode(bits);
    }

    public static long encode(final byte[] bits) {
        long result = 0;
        for (int i = bits.length-1; i >= 0; i--) {
            result = result << 4;
            final long l = ((bits[i] & (byte)0xF));
            result = result | l;
        }
        return result;
    }

    public static int compareTwoHistograms(final long first, final long second) {
        // fast check:
        if (first == second) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < MAGIC_CONSTANT; ++i) {
            byte b1 = (byte) ((first >> i * 4) & 0xF);
            byte b2 = (byte) ((second >> i * 4) & 0xF);
            result += Math.abs(b1 - b2);
        }

        return result;
    }

}
