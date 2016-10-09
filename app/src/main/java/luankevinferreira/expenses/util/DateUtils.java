package luankevinferreira.expenses.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final int ONE_MONTH = 1;
    static final int FOUR_MONTH = 4;
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private SimpleDateFormat dateFormat;

    public DateUtils() {
        dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.getDefault());
    }

    public String getStringDateTime(Date date) {
        Date localDate = date;
        if (localDate == null) {
            localDate = new Date();
        }

        return dateFormat.format(localDate);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
