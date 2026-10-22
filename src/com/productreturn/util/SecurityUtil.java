package com.productreturn.util;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Security utility class providing password hashing, CSRF token generation,
 * HTML escaping, and unique identifier generation.
 *
 * @author Product Return Management System
 */
public class SecurityUtil {

    /** BCrypt cost factor. */
    private static final int BCRYPT_COST = 12;

    private static final Random RANDOM = new Random();

    /**
     * Private constructor to prevent instantiation.
     */
    private SecurityUtil() {
    }

    /**
     * Hashes a plaintext password using BCrypt with a cost factor of 12.
     *
     * @param password the plaintext password to hash
     * @return the BCrypt hash string
     */
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST));
    }

    /**
     * Verifies a plaintext password against a BCrypt hash.
     *
     * @param password the plaintext password
     * @param hash     the BCrypt hash to check against
     * @return {@code true} if the password matches the hash
     */
    public static boolean checkPassword(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

    /**
     * Generates a random UUID-based CSRF token.
     *
     * @return a CSRF token string
     */
    public static String generateCSRFToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Escapes basic HTML entities to prevent XSS attacks.
     *
     * @param input the raw input string
     * @return the HTML-escaped string, or an empty string if input is null
     */
    public static String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                     .replace("<", "&lt;")
                     .replace(">", "&gt;")
                     .replace("\"", "&quot;")
                     .replace("'", "&#x27;");
    }

    /**
     * Generates a unique return number in the format {@code RET-yyyyMMdd-NNNN}.
     *
     * @return the generated return number
     */
    public static String generateReturnNumber() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int randomPart = 1000 + RANDOM.nextInt(9000);
        return "RET-" + datePart + "-" + randomPart;
    }

    /**
     * Generates a unique order number in the format {@code ORD-yyyyMMdd-NNNN}.
     *
     * @return the generated order number
     */
    public static String generateOrderNumber() {
        String datePart = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int randomPart = 1000 + RANDOM.nextInt(9000);
        return "ORD-" + datePart + "-" + randomPart;
    }
}
