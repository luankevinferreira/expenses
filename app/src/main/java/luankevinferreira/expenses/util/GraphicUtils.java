package luankevinferreira.expenses.util;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;

import java.util.Calendar;

import luankevinferreira.expenses.dao.ExpenseDAO;
import luankevinferreira.expenses.enumeration.LabelsType;

import static java.util.Calendar.MONTH;
import static luankevinferreira.expenses.util.DateUtils.FOUR_MONTH;

public class GraphicUtils {

    public StaticLabelsFormatter getLabels(GraphView graph) {
        String[] labels = getStringsLabels(Calendar.getInstance());

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(labels);

        return staticLabelsFormatter;
    }

    @NonNull
    String[] getStringsLabels(Calendar calendar) {
        String[] labels = new String[FOUR_MONTH];
        for (int i = 0, j = 3; i < labels.length; i++, j--) {
            labels[i] = LabelsType.getLabel(calendar.get(MONTH) - j);
        }
        return labels;
    }

    public DataPoint[] getDataPoints(Context context, String filter) {
        DataPoint[] points = new DataPoint[FOUR_MONTH];
        ExpenseDAO dao = new ExpenseDAO(context);

        for (int i = 0, j = 3; i < points.length; i++, j--) {
            Calendar date = Calendar.getInstance();
            date.set(MONTH, date.get(MONTH) - j);
            try {
                double total = dao.selectTotalMonth(date.getTime(), filter);
                DataPoint point = new DataPoint(i, total);
                points[i] = point;
            } catch (Exception exception) {
                Log.e(getClass().getCanonicalName(), exception.getMessage(), exception);
            }
        }
        dao.close();
        return points;
    }

}
