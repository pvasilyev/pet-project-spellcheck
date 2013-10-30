package com.griddynamics.spellcheck.main;

import java.io.IOException;

/**
 * @author pvasilyev
 * @since 29 Oct 2013
 */
public class MainApp {

    public static void main(String[] args) throws IOException {
        char[] array = new char[10];
        array[0] = 'a';
        array[1] = ' ';

        System.out.println(array[0] % 16);
        System.out.println(((int)array[0]));
        System.out.println(((int)array[0]) % 16);

        System.out.println(array[1] % 16);
        System.out.println(((int)array[1]));
        System.out.println(((int)array[1]) % 16);
//        System.out.println(array[1] % 16);
    }

}
