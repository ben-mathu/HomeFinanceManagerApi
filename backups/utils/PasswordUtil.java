package com.benardmathu.hfms.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Used to encrypt passwords using base64
 * @author bernard
 */
public class PasswordUtil {
    public static final String TAG = PasswordUtil.class.getSimpleName();
    
    public static final int ITERATIONS = 10000;
    public static final int LENGTH = 512;
    
    public static byte[] hash(char[] plainPassword, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(plainPassword, salt, ITERATIONS, LENGTH);
        Arrays.fill(plainPassword, Character.MIN_VALUE);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error hashing password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static String securePassword(String password, String salt) {
        return Base64.getEncoder().encodeToString(
                hash(password.toCharArray(), salt.getBytes())
        );
    }
    
    public static boolean verifyPassword(String userPassword, String storedPassword, String salt) {
        String passwd = securePassword(userPassword, salt);
        return storedPassword.equals(passwd);
    }
}
