package luankevinferreira.expenses.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.Locale;

/**
 * A TextWatcher that applies a real-time currency mask to an EditText.
 * Uses right-to-left cents input: typing digits fills from right to left,
 * starting at the cents position.
 *
 * Example for pt-BR locale, user types 1, 2, 3, 4, 5:
 *   R$ 0,01 -> R$ 0,12 -> R$ 1,23 -> R$ 12,34 -> R$ 123,45
 */
public class CurrencyTextWatcher implements TextWatcher {

    private final EditText editText;
    private final Locale locale;
    private String current = "";

    public CurrencyTextWatcher(EditText editText, Locale locale) {
        this.editText = editText;
        this.locale = CurrencyUtils.resolveLocale(locale);
    }

    /**
     * Returns the current monetary value as a double.
     * Use this when saving the expense instead of parsing the EditText text directly.
     *
     * @return the current value as a double
     */
    public double getValue() {
        String text = editText.getText().toString();
        long cents = CurrencyUtils.parseToCents(text);
        return CurrencyUtils.centsToDouble(cents);
    }

    /**
     * Sets a double value into the EditText, formatted as currency.
     * Use this when populating the field in edit mode.
     *
     * @param value the monetary value to display
     */
    public void setValue(double value) {
        long cents = CurrencyUtils.doubleToCents(value);
        String formatted = CurrencyUtils.formatCents(cents, locale);
        current = formatted;
        editText.setText(formatted);
        editText.setSelection(formatted.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // No action needed
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // No action needed
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        // Prevent infinite loop: only reformat if text differs from last format
        if (text.equals(current)) {
            return;
        }

        // Strip all non-digit characters and parse as cents
        long cents = CurrencyUtils.parseToCents(text);

        // Format cents as locale-aware currency string
        String formatted = CurrencyUtils.formatCents(cents, locale);

        // Update tracking variable and set the formatted text
        current = formatted;
        editText.removeTextChangedListener(this);
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}
