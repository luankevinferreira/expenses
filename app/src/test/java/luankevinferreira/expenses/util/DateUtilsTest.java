package luankevinferreira.expenses.util;

import android.util.Log;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class DateUtilsTest {

    @BeforeClass
    public static void beforeClass() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void whenGetStringDatePassingNullMustReturnNewDateString() {
        // Prepare
        DateUtils dateUtils = new DateUtils();

        // Action
        String expected = dateUtils.getStringDateTime(null);

        // Verify
        assertNotNull(expected);
    }

    @Test
    public void whenGetDateFormatMustReturnFullDateFormat() {
        // Prepare
        DateUtils dateUtils = new DateUtils();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2016, Calendar.JULY, 1, 2, 3, 4);

        // Action
        SimpleDateFormat format = dateUtils.getDateFormat();

        // Verify
        Assert.assertEquals("2016-07-01 02:03:04", format.format(calendar.getTime()));
    }
}
