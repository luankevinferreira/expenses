package luankevinferreira.expenses.util;

import android.content.Context;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;

import java.text.ParseException;
import java.util.Calendar;

import luankevinferreira.expenses.dao.ExpenseDAO;
import luankevinferreira.expenses.enumeration.LabelsType;

import static java.util.Calendar.MONTH;
import static luankevinferreira.expenses.util.DateUtils.FOUR_MONTH;
import static luankevinferreira.expenses.util.DateUtils.ONE_MONTH;

public class GraphicUtils {

    public StaticLabelsFormatter getLabels(GraphView graph) {
        Calendar calendar = Calendar.getInstance();

        String[] labels = new String[FOUR_MONTH];
        for (int i = 0, j = 3; i < labels.length; i++, j--) {
            labels[i] = LabelsType.getLabel(calendar.get(MONTH) - j);
        }

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(labels);

        return staticLabelsFormatter;
    }

    public DataPoint[] getDataPoints(Context context) {
        DataPoint[] points = new DataPoint[4];

        Calendar date = Calendar.getInstance();
        date.set(MONTH, date.get(MONTH) - FOUR_MONTH);

        ExpenseDAO dao = new ExpenseDAO(context);

        for (int i = 0; i < points.length; i++) {
            date.set(MONTH, date.get(MONTH) + ONE_MONTH);
            try {
                double total = dao.selectTotalMonth(date.getTime());
                DataPoint point = new DataPoint(i, total);
                points[i] = point;
            } catch (ParseException exception) {
                Log.e(getClass().getCanonicalName(), exception.getMessage(), exception);
            }
        }
        dao.close();
        return points;
    }

}