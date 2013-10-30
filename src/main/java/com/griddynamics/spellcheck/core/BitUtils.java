package com.griddynamics.spellcheck.core;

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
            result = result + l;
        }
        return result;
    }

}
