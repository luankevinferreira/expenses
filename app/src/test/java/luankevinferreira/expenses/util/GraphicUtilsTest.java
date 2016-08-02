package luankevinferreira.expenses.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import luankevinferreira.expenses.enumeration.LabelsType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GraphicUtilsTest {

    @Test
    public void whenGetGraphicLabelsMustReturnBeforeFourMonths() {
        // Prepare
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 6, 31);
        GraphicUtils graphicUtils = new GraphicUtils();

        // Action
        String[] labels = graphicUtils.getStringsLabels(calendar);

        // Verify
        assertNotNull(labels);
        assertEquals(4, labels.length);
        assertEquals(LabelsType.APRIL.getMonth(), labels[0]);
        assertEquals(LabelsType.MAY.getMonth(), labels[1]);
        assertEquals(LabelsType.JUNE.getMonth(), labels[2]);
        assertEquals(LabelsType.JULY.getMonth(), labels[3]);
        assertEquals("31/07/2016", format.format(calendar.getTime()));
    }
}
