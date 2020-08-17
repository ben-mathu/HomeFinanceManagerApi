package com.miiguar.hfms.utils;

import java.util.Base64;

/**
 * @author bernard
 */
public class GenerateCipher {
    public static void main(String[] args) {
        GenerateCipher cipher = new GenerateCipher();
        String cipherStr = cipher.encode("174379bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c91920200812075000");
        System.out.println(cipherStr);
    }

    public String encode(String plainText){
        byte[] cipher = Base64.getEncoder().encode(plainText.getBytes());
        return new String(cipher);
    }

    public String decode(String cipher) {
        byte[] plainText = Base64.getDecoder().decode(cipher);
        return new String(plainText);
    }
}
