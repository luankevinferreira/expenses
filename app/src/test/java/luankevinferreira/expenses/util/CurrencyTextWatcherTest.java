package luankevinferreira.expenses.util;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the currency text watcher formatting logic.
 * Since CurrencyTextWatcher delegates formatting to CurrencyUtils,
 * these tests verify the user typing simulation: raw digit input
 * being converted through parseToCents -> formatCents pipeline.
 */
public class CurrencyTextWatcherTest {

    private static final Locale LOCALE_PT_BR = new Locale("pt", "BR");
    private static final Locale LOCALE_EN_US = new Locale("en", "US");

    /**
     * Simulates the formatting that CurrencyTextWatcher performs:
     * strips non-digits, parses as cents, formats as currency.
     */
    private String simulateFormat(String rawInput, Locale locale) {
        long cents = CurrencyUtils.parseToCents(rawInput);
        return CurrencyUtils.formatCents(cents, locale);
    }

    @Test
    public void testShouldFormatSingleDigitAsCentsBrazilian() {
        // Prepare - user typed "1"
        String input = "1";

        // Action
        String result = simulateFormat(input, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,01", result);
    }

    @Test
    public void testShouldFormatTwoDigitsAsCentsBrazilian() {
        // Prepare - user typed "12"
        String input = "12";

        // Action
        String result = simulateFormat(input, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,12", result);
    }

    @Test
    public void testShouldFormatThreeDigitsBrazilian() {
        // Prepare - user typed "123"
        String input = "123";

        // Action
        String result = simulateFormat(input, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 1,23", result);
    }

    @Test
    public void testShouldFormatFiveDigitsBrazilian() {
        // Prepare - user typed "12345"
        String input = "12345";

        // Action
        String result = simulateFormat(input, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 123,45", result);
    }

    @Test
    public void testShouldFormatWithThousandSeparatorBrazilian() {
        // Prepare - user typed "1234567"
        String input = "1234567";

        // Action
        String result = simulateFormat(input, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 12.345,67", result);
    }

    @Test
    public void testShouldFormatEmptyInputBrazilian() {
        // Prepare
        String input = "";

        // Action
        String result = simulateFormat(input, LOCALE_PT_BR);

        // Verify
        assertEquals("R$ 0,00", result);
    }

    @Test
    public void testShouldFormatSingleDigitAsCentsEnglish() {
        // Prepare - user typed "1"
        String input = "1";

        // Action
        String result = simulateFormat(input, LOCALE_EN_US);

        // Verify
        assertEquals("$0.01", result);
    }

    @Test
    public void testShouldFormatTwoDigitsAsCentsEnglish() {
        // Prepare - user typed "12"
        String input = "12";

        // Action
        String result = simulateFormat(input, LOCALE_EN_US);

        // Verify
        assertEquals("$0.12", result);
    }

    @Test
    public void testShouldFormatThreeDigitsEnglish() {
        // Prepare - user typed "123"
        String input = "123";

        // Action
        String result = simulateFormat(input, LOCALE_EN_US);

        // Verify
        assertEquals("$1.23", result);
    }

    @Test
    public void testShouldFormatFiveDigitsEnglish() {
        // Prepare - user typed "12345"
        String input = "12345";

        // Action
        String result = simulateFormat(input, LOCALE_EN_US);

        // Verify
        assertEquals("$123.45", result);
    }

    @Test
    public void testShouldFormatWithThousandSeparatorEnglish() {
        // Prepare - user typed "1234567"
        String input = "1234567";

        // Action
        String result = simulateFormat(input, LOCALE_EN_US);

        // Verify
        assertEquals("$12,345.67", result);
    }

    @Test
    public void testShouldExtractValueFromFormattedBrazilianText() {
        // Prepare - simulates backspace: formatted text is re-parsed
        String formatted = "R$ 123,45";

        // Action
        long cents = CurrencyUtils.parseToCents(formatted);
        double value = CurrencyUtils.centsToDouble(cents);

        // Verify
        assertEquals(12345, cents);
        assertEquals(123.45, value, 0.001);
    }

    @Test
    public void testShouldExtractValueFromFormattedEnglishText() {
        // Prepare
        String formatted = "$1,234.56";

        // Action
        long cents = CurrencyUtils.parseToCents(formatted);
        double value = CurrencyUtils.centsToDouble(cents);

        // Verify
        assertEquals(123456, cents);
        assertEquals(1234.56, value, 0.001);
    }

    @Test
    public void testShouldSimulateTypingSequenceBrazilian() {
        // Simulate user typing digits one at a time: 1, 2, 3, 4, 5
        assertEquals("R$ 0,01", simulateFormat("1", LOCALE_PT_BR));
        assertEquals("R$ 0,12", simulateFormat("12", LOCALE_PT_BR));
        assertEquals("R$ 1,23", simulateFormat("123", LOCALE_PT_BR));
        assertEquals("R$ 12,34", simulateFormat("1234", LOCALE_PT_BR));
        assertEquals("R$ 123,45", simulateFormat("12345", LOCALE_PT_BR));
    }

    @Test
    public void testShouldSimulateTypingSequenceEnglish() {
        // Simulate user typing digits one at a time: 1, 2, 3, 4, 5
        assertEquals("$0.01", simulateFormat("1", LOCALE_EN_US));
        assertEquals("$0.12", simulateFormat("12", LOCALE_EN_US));
        assertEquals("$1.23", simulateFormat("123", LOCALE_EN_US));
        assertEquals("$12.34", simulateFormat("1234", LOCALE_EN_US));
        assertEquals("$123.45", simulateFormat("12345", LOCALE_EN_US));
    }

    @Test
    public void testShouldSimulateBackspaceBrazilian() {
        // User typed "12345" (R$ 123,45), then deletes last digit
        // After deletion, formatted text becomes "R$ 123,4" -> re-parsed digits = "1234"
        String afterBackspace = "1234";
        assertEquals("R$ 12,34", simulateFormat(afterBackspace, LOCALE_PT_BR));
    }

    @Test
    public void testShouldHandleDeleteAllDigitsBrazilian() {
        // User deletes all digits
        assertEquals("R$ 0,00", simulateFormat("", LOCALE_PT_BR));
    }

    @Test
    public void testShouldConvertDoubleValueToCentsAndFormatBrazilian() {
        // Simulates edit mode: setting an existing expense value
        double expenseValue = 250.75;
        long cents = CurrencyUtils.doubleToCents(expenseValue);
        String formatted = CurrencyUtils.formatCents(cents, LOCALE_PT_BR);

        assertEquals(25075, cents);
        assertEquals("R$ 250,75", formatted);
    }

    @Test
    public void testShouldConvertDoubleValueToCentsAndFormatEnglish() {
        // Simulates edit mode: setting an existing expense value
        double expenseValue = 250.75;
        long cents = CurrencyUtils.doubleToCents(expenseValue);
        String formatted = CurrencyUtils.formatCents(cents, LOCALE_EN_US);

        assertEquals(25075, cents);
        assertEquals("$250.75", formatted);
    }
}
