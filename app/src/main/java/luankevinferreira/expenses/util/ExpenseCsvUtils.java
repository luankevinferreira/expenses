package luankevinferreira.expenses.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import luankevinferreira.expenses.domain.Expense;

public final class ExpenseCsvUtils {

    private static final String CSV_HEADER = "id,date,description,type,value\n";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private ExpenseCsvUtils() {
    }

    public static String buildCsv(List<Expense> expenses) {
        StringBuilder csvContent = new StringBuilder(CSV_HEADER);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.US);

        if (expenses == null) {
            return csvContent.toString();
        }

        for (Expense expense : expenses) {
            csvContent.append(expense.getId()).append(',');
            csvContent.append(escapeField(expense.getDate() == null
                    ? ""
                    : dateFormat.format(expense.getDate()))).append(',');
            csvContent.append(escapeField(expense.getDescription())).append(',');
            csvContent.append(escapeField(expense.getType())).append(',');
            csvContent.append(formatValue(expense.getValue())).append('\n');
        }

        return csvContent.toString();
    }

    public static void write(List<Expense> expenses, OutputStream outputStream) throws IOException {
        outputStream.write(buildCsv(expenses).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    private static String formatValue(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static String escapeField(String value) {
        String safeValue = value == null ? "" : value;

        if ((safeValue.contains(",")) || (safeValue.contains("\"")) || (safeValue.contains("\n"))
                || (safeValue.contains("\r"))) {
            return '"' + safeValue.replace("\"", "\"\"") + '"';
        }

        return safeValue;
    }
}
