package com.productreturn.util;

import java.util.regex.Pattern;

/**
 * Input validation utility class providing common validation methods
 * for user inputs throughout the application.
 *
 * @author Product Return Management System
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9_]{3,50}$");

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+]?[0-9\\-\\s()]{7,20}$");

    /** Maximum allowed file size in bytes (5 MB). */
    private static final long MAX_FILE_SIZE = 5L * 1024 * 1024;

    /**
     * Private constructor to prevent instantiation.
     */
    private ValidationUtil() {
    }

    /**
     * Validates an email address format.
     *
     * @param email the email string to validate
     * @return {@code true} if the email is valid
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates a username (3-50 characters, alphanumeric and underscore only).
     *
     * @param username the username to validate
     * @return {@code true} if the username is valid
     */
    public static boolean isValidUsername(String username) {
        if (isNullOrEmpty(username)) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validates a password (minimum 8 characters, at least one letter and one digit).
     *
     * @param password the password to validate
     * @return {@code true} if the password meets the requirements
     */
    public static boolean isValidPassword(String password) {
        if (isNullOrEmpty(password)) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * Validates a phone number format.
     *
     * @param phone the phone number to validate
     * @return {@code true} if the phone number format is valid
     */
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Sanitizes user input by trimming whitespace and removing
     * potentially dangerous characters.
     *
     * @param input the raw input string
     * @return the sanitized string, or an empty string if input is null
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim()
                     .replaceAll("[<>\"'&]", "");
    }

    /**
     * Checks whether a string is null or empty (after trimming).
     *
     * @param str the string to check
     * @return {@code true} if the string is null or blank
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validates that a file name has an allowed image extension
     * (.jpg, .jpeg, .png).
     *
     * @param fileName the file name to check
     * @return {@code true} if the file has an allowed image extension
     */
    public static boolean isValidImageFile(String fileName) {
        if (isNullOrEmpty(fileName)) {
            return false;
        }
        String lower = fileName.toLowerCase().trim();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".png");
    }

    /**
     * Validates that a file size does not exceed the maximum allowed size (5 MB).
     *
     * @param size the file size in bytes
     * @return {@code true} if the size is within the allowed limit
     */
    public static boolean isValidFileSize(long size) {
        return size > 0 && size <= MAX_FILE_SIZE;
    }
}
