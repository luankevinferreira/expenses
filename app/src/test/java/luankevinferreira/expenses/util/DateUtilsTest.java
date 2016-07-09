package luankevinferreira.expenses.util;

import android.util.Log;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class DateUtilsTest {

    @BeforeClass
    public static void beforeClass() {
        PowerMockito.mockStatic(Log.class);
    }

    @Test
    public void when_get_string_date_passing_null_must_return_new_date_string() {
        // Prepare
        Date date = null;
        DateUtils dateUtils = new DateUtils();

        // Action
        String expected = dateUtils.getStringDateTime(date);

        // Verify
        assertNotNull(expected);
    }
}
