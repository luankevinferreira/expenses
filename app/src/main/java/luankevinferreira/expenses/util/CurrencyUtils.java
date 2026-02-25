package luankevinferreira.expenses.util;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Utility class for locale-aware currency formatting and parsing.
 * Supports formatting values as currency strings and parsing them back,
 * using the locale derived from app string resources.
 */
public class CurrencyUtils {

    private static final int CENTS_FACTOR = 100;
    private static final char NON_BREAKING_SPACE = '\u00A0';

    /**
     * Resolves a locale to ensure it has a valid country code for currency formatting.
     * Maps non-standard country codes (e.g., "EN") to valid ones (e.g., "US").
     *
     * @param locale the locale to resolve
     * @return a locale with a valid country code for currency operations
     */
    public static Locale resolveLocale(Locale locale) {
        if (locale == null) {
            return Locale.US;
        }
        String language = locale.getLanguage();

        // "EN" is not a valid ISO 3166 country code; map English to US
        if ("en".equals(language) && !isValidCurrencyCountry(locale)) {
            return new Locale("en", "US");
        }
        return locale;
    }

    /**
     * Checks whether the given locale has a valid currency association.
     *
     * @param locale the locale to check
     * @return true if the locale can produce a valid Currency instance
     */
    private static boolean isValidCurrencyCountry(Locale locale) {
        try {
            Currency.getInstance(locale);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Formats a double value as a locale-aware currency string.
     *
     * @param value  the monetary value
     * @param locale the locale for formatting
     * @return formatted currency string (e.g., "R$ 1.234,56" for pt-BR)
     */
    public static String formatCurrency(double value, Locale locale) {
        Locale resolved = resolveLocale(locale);
        NumberFormat format = NumberFormat.getCurrencyInstance(resolved);
        return format.format(value).replace(NON_BREAKING_SPACE, ' ');
    }

    /**
     * Formats a cents value as a locale-aware currency string.
     *
     * @param cents  the value in cents (e.g., 12345 for 123.45)
     * @param locale the locale for formatting
     * @return formatted currency string
     */
    public static String formatCents(long cents, Locale locale) {
        double value = centsToDouble(cents);
        return formatCurrency(value, locale);
    }

    /**
     * Parses a formatted currency string back to a double value.
     * Returns 0.0 for null or empty input.
     *
     * @param formatted the formatted currency string
     * @param locale    the locale used for parsing
     * @return the parsed monetary value as a double
     */
    public static double parseCurrency(String formatted, Locale locale) {
        if (formatted == null || formatted.trim().isEmpty()) {
            return 0.0;
        }
        Locale resolved = resolveLocale(locale);
        NumberFormat format = NumberFormat.getCurrencyInstance(resolved);
        try {
            // Normalize regular spaces to non-breaking spaces for parser compatibility
            String normalized = formatted.replace(' ', NON_BREAKING_SPACE);
            Number number = format.parse(normalized);
            return number != null ? number.doubleValue(): 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Parses a string by stripping all non-digit characters and returning the
     * resulting number as cents. Returns 0 for null or empty input.
     *
     * @param rawInput the input string (may contain digits and formatting characters)
     * @return the numeric value of the digits as a long
     */
    public static long parseToCents(String rawInput) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return 0;
        }
        String digitsOnly = rawInput.replaceAll("[^0-9]", "");
        if (digitsOnly.isEmpty()) {
            return 0;
        }
        try {
            return Long.parseLong(digitsOnly);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Converts a cents value to a double (e.g., 12345 -> 123.45).
     *
     * @param cents the value in cents
     * @return the double value
     */
    public static double centsToDouble(long cents) {
        return (double) cents / CENTS_FACTOR;
    }

    /**
     * Converts a double value to cents (e.g., 123.45 -> 12345).
     *
     * @param value the double value
     * @return the value in cents
     */
    public static long doubleToCents(double value) {
        return Math.round(value * CENTS_FACTOR);
    }
}
