package com.productreturn;

import com.productreturn.util.ValidationUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidationUtilTest {

    @Test
    public void testValidEmails() {
        assertTrue(ValidationUtil.isValidEmail("john.doe@example.com"));
        assertTrue(ValidationUtil.isValidEmail("customer_123@domain.co.in"));
        assertTrue(ValidationUtil.isValidEmail("user+tag@domain.org"));

        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("john@"));
        assertFalse(ValidationUtil.isValidEmail("@domain.com"));
    }

    @Test
    public void testValidUsernames() {
        assertTrue(ValidationUtil.isValidUsername("john_doe"));
        assertTrue(ValidationUtil.isValidUsername("customer123"));
        assertTrue(ValidationUtil.isValidUsername("admin_user"));

        assertFalse(ValidationUtil.isValidUsername(null));
        assertFalse(ValidationUtil.isValidUsername("ab")); // too short (<3)
        assertFalse(ValidationUtil.isValidUsername("invalid user name")); // spaces
        assertFalse(ValidationUtil.isValidUsername("user@name")); // special chars
    }

    @Test
    public void testValidPasswords() {
        assertTrue(ValidationUtil.isValidPassword("secret123"));
        assertTrue(ValidationUtil.isValidPassword("SecureP@ss1"));
        assertTrue(ValidationUtil.isValidPassword("customer2026"));

        assertFalse(ValidationUtil.isValidPassword(null));
        assertFalse(ValidationUtil.isValidPassword("short1")); // < 8 chars
        assertFalse(ValidationUtil.isValidPassword("onlyletters")); // no digit
        assertFalse(ValidationUtil.isValidPassword("12345678")); // no letter
    }

    @Test
    public void testImageFileValidation() {
        assertTrue(ValidationUtil.isValidImageFile("photo.jpg"));
        assertTrue(ValidationUtil.isValidImageFile("damage.JPEG"));
        assertTrue(ValidationUtil.isValidImageFile("screenshot.png"));

        assertFalse(ValidationUtil.isValidImageFile(null));
        assertFalse(ValidationUtil.isValidImageFile("file.pdf"));
        assertFalse(ValidationUtil.isValidImageFile("virus.exe"));
        assertFalse(ValidationUtil.isValidImageFile("script.js"));
    }

    @Test
    public void testFileSizeValidation() {
        assertTrue(ValidationUtil.isValidFileSize(1024)); // 1 KB
        assertTrue(ValidationUtil.isValidFileSize(5 * 1024 * 1024)); // exactly 5 MB
        assertFalse(ValidationUtil.isValidFileSize(5 * 1024 * 1024 + 1)); // > 5 MB
        assertFalse(ValidationUtil.isValidFileSize(0));
        assertFalse(ValidationUtil.isValidFileSize(-100));
    }

    @Test
    public void testSanitizeInput() {
        assertEquals("", ValidationUtil.sanitizeInput(null));
        assertEquals("clean text", ValidationUtil.sanitizeInput("   clean text   "));
        assertEquals("scriptalert(1)/script", ValidationUtil.sanitizeInput("<script>alert(1)</script>"));
    }
}
