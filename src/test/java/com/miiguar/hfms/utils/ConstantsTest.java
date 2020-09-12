package com.miiguar.hfms.utils;

import com.benardmathu.hfms.utils.Patterns;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author bernard
 */
public class ConstantsTest {

    @Test
    public void Pattern_InvalidEmail_Returns_False() {
        assertFalse(Patterns.EMAIL_VERIFICATION_PATTERN.matcher("mathu@gmail").matches());
        assertFalse(Patterns.EMAIL_VERIFICATION_PATTERN.matcher("mathu@.co.ke").matches());
        assertFalse(Patterns.EMAIL_VERIFICATION_PATTERN.matcher("mathugmail.co.ke").matches());
        assertFalse(Patterns.EMAIL_VERIFICATION_PATTERN.matcher("@gmail.co.ke").matches());
        assertFalse(Patterns.EMAIL_VERIFICATION_PATTERN.matcher("mathu@gmail.co.").matches());
        assertFalse(Patterns.EMAIL_VERIFICATION_PATTERN.matcher("mathu@gmail..ke").matches());
    }

    @Test
    public void Pattern_ValidEmail_Returns_True() {
        assertTrue(Patterns.EMAIL_VERIFICATION_PATTERN.matcher("mathu@gmail.co.ke").matches());
    }

    @Test
    public void test_ValidParam_ReturnsTrue() {
        assertTrue(Patterns.VALID_PASSWORD_2DG.matcher("1hb23").find());
//        assertTrue(Constants.VALID_PASSWORD_2LC.matcher("a7Gb").matches());
//        assertTrue(Constants.VALID_PASSWORD_2UC.matcher("A9C").matches());
//        assertTrue(Constants.VALID_PASSWORD_2SC.matcher("&P(").matches());
    }

    @Test
    public void test_InvalidParam_ReturnsFalse() {
        assertFalse(Patterns.VALID_PASSWORD_2DG.matcher("1").matches());
        assertFalse(Patterns.VALID_PASSWORD_2LC.matcher("a").matches());
        assertFalse(Patterns.VALID_PASSWORD_2UC.matcher("C").matches());
    }

    @Test
    public void isPasswordValid_ValidPassword_ReturnEmptyString() {
        assertEquals("", Patterns.isPasswordValid("123abcABC*&%$"));
    }

    @Test
    public void UsernameValidation_ValidUsername_ReturnsTrue() {
        assertTrue(Patterns.USERNAME_VALIDATION.matcher("b_matt").matches());
    }

    @Test
    public void UsernameValidation_InvalidUsername_ReturnsFalse() {
        assertFalse(Patterns.USERNAME_VALIDATION.matcher("@b_matt").matches());
    }

    @Test
    public void isUsernameValid_InvalidUsername_ReturnsError() {
        String usernameError = "Your username should have these properties:</br>" +
                "- between 6 and 12 letters,</br>" +
                "- and not contain these characters:</br>" +
                "<span style=\"color: #FEC800;font-size:18px;\">[]()=,\"/?@:;</span>";
        assertEquals(usernameError, Patterns.isUsernameValid("@b_matt"));
    }

    @Test
    public void isUsernameValid_ValidUsername_ReturnsEmptyString() {
        assertEquals("", Patterns.isUsernameValid("b_matt"));
    }

    @Test
    public void isPasswordValid_EmptyPassword_ReturnError() {
        String sb = "- At least 7 characters</br>" +
                "- At least 2 Lowercase letters</br>" +
                "- At least 2 Uppercase letters</br>" +
                "- At least 1 special characters</br>" +
                "- And at least 2 digits.";
        assertEquals(sb, Patterns.isPasswordValid(""));
    }
}