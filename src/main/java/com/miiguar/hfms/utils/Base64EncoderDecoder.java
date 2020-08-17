package com.miiguar.hfms.utils;

import java.util.Base64;

/**
 * @author bernard
 */
public class Base64EncoderDecoder {
    public static String encode(String plainText) {
        byte[] cipher = Base64.getEncoder().encode(plainText.getBytes());
        return new String(cipher);
    }

    public static String decode(String cipher) {
        byte[] plainText = Base64.getDecoder().decode(cipher);
        return new String(plainText);
    }
}
