package luankevinferreira.expenses;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import luankevinferreira.expenses.dao.ExpenseDAO;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.domain.Type;
import luankevinferreira.expenses.util.GraphicUtils;

import static android.view.animation.AnimationUtils.loadAnimation;
import static luankevinferreira.expenses.enumeration.CodeIntentType.REQUEST_DETAIL_EXPENSES;
import static luankevinferreira.expenses.enumeration.CodeIntentType.REQUEST_NEW_EXPENSE;
import static luankevinferreira.expenses.enumeration.CodeIntentType.STATUS_OK;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int DELAY_MILLIS = 300;
    public static final int ORDER = 0;
    private Button totalMonth;
    private FloatingActionButton fab;
    private Animation rotateForward, rotateBackward, clickAlpha;
    private GraphView graph;
    private DecimalFormat formatter;
    private LineGraphSeries<DataPoint> series;
    private GraphicUtils graphicUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        formatter = new DecimalFormat(getString(R.string.decimal_pattern));

        totalMonth = findViewById(R.id.total_month);
        if (totalMonth != null)
            totalMonth.setOnClickListener(this);

        graphicUtils = new GraphicUtils();
        graph = findViewById(R.id.graph);
        if (graph != null)
            configGraphic();

        // load animations
        rotateForward = loadAnimation(getApplicationContext(), R.anim.rotate_foward);
        rotateBackward = loadAnimation(getApplicationContext(), R.anim.rotate_backward);
        clickAlpha = loadAnimation(getApplicationContext(), R.anim.click_alpha);

        fab = findViewById(R.id.fab);
        if (fab != null)
            fab.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTotalMonth(ExpenseDAO.NO_FILTER_EN);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);

        ExpenseDAO dao = new ExpenseDAO(getApplicationContext());
        List<Type> types = new ArrayList<>();

        try {
            types = dao.selectTypesExpenses();
        } catch (Exception exception) {
            Log.e(getClass().getCanonicalName(), exception.getMessage(), exception);
        }

        for (Type t : types) {
            menu.add(R.id.group_filter, t.getId(), ORDER, t.getName());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String filter = item.getTitle().toString();
        updateTotalMonth(filter);
        series.resetData(graphicUtils.getDataPoints(getApplicationContext(), filter));
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fab) {
            fab.startAnimation(rotateForward);
            final Intent intent = new Intent(getApplicationContext(), ExpenseActivity.class);
            fab.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab.setVisibility(View.GONE);
                    startActivityForResult(intent, REQUEST_NEW_EXPENSE.getCode());
                }
            }, DELAY_MILLIS);
        } else if (id == R.id.total_month) {
            totalMonth.startAnimation(clickAlpha);
            Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
            startActivityForResult(intent, REQUEST_DETAIL_EXPENSES.getCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_EXPENSE.getCode()) {
            fab.startAnimation(rotateBackward);
            fab.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab.setVisibility(View.GONE);
                }
            }, DELAY_MILLIS);
            if (resultCode == STATUS_OK.getCode()) {
                series.resetData(graphicUtils.getDataPoints(getApplicationContext(), ExpenseDAO.NO_FILTER_EN));
                recreate();
            }
        } else if (requestCode == REQUEST_DETAIL_EXPENSES.getCode()) {
            if (resultCode == STATUS_OK.getCode()) {
                series.resetData(graphicUtils.getDataPoints(getApplicationContext(), ExpenseDAO.NO_FILTER_EN));
                recreate();
            }
        }
    }

    private void configGraphic() {
        series = new LineGraphSeries<>(graphicUtils.getDataPoints(getApplicationContext(), ExpenseDAO.NO_FILTER_EN));
        series.setTitle(getString(R.string.total));
        series.setColor(Color.RED);
        graph.addSeries(series);

        graph.setTitle(getString(R.string.title_graphic));

        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        StaticLabelsFormatter labels = new GraphicUtils().getLabels(graph);
        graph.getGridLabelRenderer().setLabelFormatter(labels);
    }

    private void updateTotalMonth(String filter) {
        List<Expense> expenses = new ArrayList<>();

        ExpenseDAO expenseDAO = null;
        try {
            expenseDAO = new ExpenseDAO(getApplicationContext());
            expenses = expenseDAO.select(new Date(), filter);
        } catch (ParseException exception) {
            Log.e(getClass().getCanonicalName(), exception.getMessage(), exception);
        } finally {
            if (expenseDAO != null)
                expenseDAO.close();
        }

        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getValue();
        }

        totalMonth.setText(formatter.format(total));
    }
}
