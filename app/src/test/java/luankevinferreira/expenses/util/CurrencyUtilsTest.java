package luankevinferreira.expenses.util;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class CurrencyUtilsTest {

    private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final double DELTA = 0.001;

    // --- formatCurrency(double, Locale) ---

    @Test
    public void testShouldFormatZeroValueForBrazilianLocale() {
        // Prepare
        double value = 0.0;

        // Action
        String result = CurrencyUtils.formatCurrency(value, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,00", result);
    }

    @Test
    public void testShouldFormatValueForBrazilianLocale() {
        // Prepare
        double value = 1234.56;

        // Action
        String result = CurrencyUtils.formatCurrency(value, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 1.234,56", result);
    }

    @Test
    public void testShouldFormatValueForEnglishLocale() {
        // Prepare
        double value = 1234.56;

        // Action
        String result = CurrencyUtils.formatCurrency(value, LOCALE_EN_US);

        // Verify
        assertEquals("$1,234.56", result);
    }

    @Test
    public void testShouldFormatZeroValueForEnglishLocale() {
        // Prepare
        double value = 0.0;

        // Action
        String result = CurrencyUtils.formatCurrency(value, LOCALE_EN_US);

        // Verify
        assertEquals("$0.00", result);
    }

    @Test
    public void testShouldFormatSmallValueForBrazilianLocale() {
        // Prepare
        double value = 0.99;

        // Action
        String result = CurrencyUtils.formatCurrency(value, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,99", result);
    }

    @Test
    public void testShouldFormatLargeValueForBrazilianLocale() {
        // Prepare
        double value = 99999.99;

        // Action
        String result = CurrencyUtils.formatCurrency(value, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 99.999,99", result);
    }

    @Test
    public void testShouldFormatNineDigitIntegerPartForBrazilianLocale() {
        // Prepare
        double value = 123456789.01;

        // Action
        String result = CurrencyUtils.formatCurrency(value, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 123.456.789,01", result);
    }

    // --- formatCents(long, Locale) ---

    @Test
    public void testShouldFormatCentsForBrazilianLocale() {
        // Prepare
        long cents = 12345;

        // Action
        String result = CurrencyUtils.formatCents(cents, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 123,45", result);
    }

    @Test
    public void testShouldFormatCentsForEnglishLocale() {
        // Prepare
        long cents = 12345;

        // Action
        String result = CurrencyUtils.formatCents(cents, LOCALE_EN_US);

        // Verify
        assertEquals("$123.45", result);
    }

    @Test
    public void testShouldFormatZeroCentsForBrazilianLocale() {
        // Prepare
        long cents = 0;

        // Action
        String result = CurrencyUtils.formatCents(cents, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,00", result);
    }

    @Test
    public void testShouldFormatZeroCentsForEnglishLocale() {
        // Prepare
        long cents = 0;

        // Action
        String result = CurrencyUtils.formatCents(cents, LOCALE_EN_US);

        // Verify
        assertEquals("$0.00", result);
    }

    @Test
    public void testShouldFormatSingleDigitCents() {
        // Prepare
        long cents = 1;

        // Action
        String result = CurrencyUtils.formatCents(cents, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,01", result);
    }

    @Test
    public void testShouldFormatTwoDigitCents() {
        // Prepare
        long cents = 12;

        // Action
        String result = CurrencyUtils.formatCents(cents, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,12", result);
    }

    @Test
    public void testShouldFormatCentsWithThousandSeparator() {
        // Prepare
        long cents = 1234567;

        // Action
        String result = CurrencyUtils.formatCents(cents, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 12.345,67", result);
    }

    // --- centsToDouble / doubleToCents ---

    @Test
    public void testShouldConvertCentsToDouble() {
        // Prepare
        long cents = 12345;

        // Action
        double result = CurrencyUtils.centsToDouble(cents);

        // Verify
        assertEquals(123.45, result, DELTA);
    }

    @Test
    public void testShouldConvertZeroCentsToDouble() {
        // Prepare
        long cents = 0;

        // Action
        double result = CurrencyUtils.centsToDouble(cents);

        // Verify
        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void testShouldConvertDoubleToCents() {
        // Prepare
        double value = 123.45;

        // Action
        long result = CurrencyUtils.doubleToCents(value);

        // Verify
        assertEquals(12345, result);
    }

    @Test
    public void testShouldConvertZeroDoubleToCents() {
        // Prepare
        double value = 0.0;

        // Action
        long result = CurrencyUtils.doubleToCents(value);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testShouldConvertSmallDoubleToCents() {
        // Prepare
        double value = 0.01;

        // Action
        long result = CurrencyUtils.doubleToCents(value);

        // Verify
        assertEquals(1, result);
    }

    @Test
    public void testShouldConvertAndRoundTripLargeValueWithoutChangingCents() {
        // Prepare
        double value = 123456789.01;

        // Action
        long cents = CurrencyUtils.doubleToCents(value);
        double roundTrip = CurrencyUtils.centsToDouble(cents);

        // Verify
        assertEquals(12345678901L, cents);
        assertEquals(value, roundTrip, DELTA);
    }

    // --- parseCurrency(String, Locale) ---

    @Test
    public void testShouldParseFormattedBrazilianCurrency() {
        // Prepare
        String formatted = "R$ 1.234,56";

        // Action
        double result = CurrencyUtils.parseCurrency(formatted, LOCALE_PT_BR);

        // Verify
        assertEquals(1234.56, result, DELTA);
    }

    @Test
    public void testShouldParseFormattedEnglishCurrency() {
        // Prepare
        String formatted = "$1,234.56";

        // Action
        double result = CurrencyUtils.parseCurrency(formatted, LOCALE_EN_US);

        // Verify
        assertEquals(1234.56, result, DELTA);
    }

    @Test
    public void testShouldParseZeroBrazilianCurrency() {
        // Prepare
        String formatted = "R$ 0,00";

        // Action
        double result = CurrencyUtils.parseCurrency(formatted, LOCALE_PT_BR);

        // Verify
        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void testShouldParseEmptyStringToZero() {
        // Prepare
        String formatted = "";

        // Action
        double result = CurrencyUtils.parseCurrency(formatted, LOCALE_PT_BR);

        // Verify
        assertEquals(0.0, result, DELTA);
    }

    @Test
    public void testShouldParseNullToZero() {
        // Prepare / Action
        double result = CurrencyUtils.parseCurrency(null, LOCALE_PT_BR);

        // Verify
        assertEquals(0.0, result, DELTA);
    }

    // --- parseToCents(String) ---

    @Test
    public void testShouldParseRawDigitsToCents() {
        // Prepare
        String rawDigits = "12345";

        // Action
        long result = CurrencyUtils.parseToCents(rawDigits);

        // Verify
        assertEquals(12345, result);
    }

    @Test
    public void testShouldParseEmptyStringToCentsAsZero() {
        // Prepare
        String rawDigits = "";

        // Action
        long result = CurrencyUtils.parseToCents(rawDigits);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testShouldParseNullToCentsAsZero() {
        // Prepare / Action
        long result = CurrencyUtils.parseToCents(null);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testShouldParseFormattedStringToCentsByStrippingNonDigits() {
        // Prepare
        String formatted = "R$ 123,45";

        // Action
        long result = CurrencyUtils.parseToCents(formatted);

        // Verify
        assertEquals(12345, result);
    }

    @Test
    public void testShouldParseSingleDigitToCents() {
        // Prepare
        String rawDigits = "1";

        // Action
        long result = CurrencyUtils.parseToCents(rawDigits);

        // Verify
        assertEquals(1, result);
    }

    // --- resolveLocale ---

    @Test
    public void testShouldResolveValidBrazilianLocale() {
        // Prepare
        Locale locale = new Locale("pt", "BR");

        // Action
        Locale result = CurrencyUtils.resolveLocale(locale);

        // Verify
        assertEquals("BR", result.getCountry());
        assertEquals("pt", result.getLanguage());
    }

    @Test
    public void testShouldResolveInvalidEnglishCountryToUS() {
        // Prepare
        Locale locale = new Locale("en", "EN");

        // Action
        Locale result = CurrencyUtils.resolveLocale(locale);

        // Verify
        assertEquals("US", result.getCountry());
        assertEquals("en", result.getLanguage());
    }

    @Test
    public void testShouldKeepValidEnglishUSLocale() {
        // Prepare
        Locale locale = new Locale("en", "US");

        // Action
        Locale result = CurrencyUtils.resolveLocale(locale);

        // Verify
        assertEquals("US", result.getCountry());
        assertEquals("en", result.getLanguage());
    }
}
