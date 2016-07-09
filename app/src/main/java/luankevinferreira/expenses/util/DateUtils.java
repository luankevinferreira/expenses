package luankevinferreira.expenses.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    public static final int ONE_MONTH = 1;
    public static final int FOUR_MONTH = 4;
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public SimpleDateFormat dateFormat;

    public DateUtils() {
        dateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS, Locale.getDefault());
    }

    public String getStringDateTime(Date date) {
        if (date == null) {
            date = new Date();
        }

        return dateFormat.format(date);
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
