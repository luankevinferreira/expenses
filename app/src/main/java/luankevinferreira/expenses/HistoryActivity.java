package luankevinferreira.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import luankevinferreira.expenses.components.ExpenseAdapter;
import luankevinferreira.expenses.dao.ExpenseDAO;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.enumeration.ExtraType;
import luankevinferreira.expenses.util.DateUtils;

import static android.view.View.OnClickListener;
import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static luankevinferreira.expenses.enumeration.CodeIntentType.REQUEST_EDIT_EXPENSE;
import static luankevinferreira.expenses.enumeration.CodeIntentType.STATUS_OK;

public class HistoryActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {

    private TextView txvMonth, txvTotalHistory;
    private ListView listView;
    private Animation clickButton;
    private ImageButton btnNext, btnPrev;

    private List<Expense> expenses;
    private DecimalFormat decimalFormat;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        calendar = Calendar.getInstance();
        Locale locale = new Locale(getString(R.string.language), getString(R.string.country));
        dateFormat = new SimpleDateFormat(getString(R.string.date_month_pattern), locale);

        decimalFormat = new DecimalFormat(getString(R.string.decimal_pattern));

        txvMonth = (TextView) findViewById(R.id.month_expense);
        txvMonth.setText(dateFormat.format(calendar.getTime()));

        txvTotalHistory = (TextView) findViewById(R.id.total_history);

        listView = (ListView) findViewById(R.id.listView);
        if (listView != null)
            listView.setOnItemClickListener(this);

        btnNext = (ImageButton) findViewById(R.id.next_button);
        btnPrev = (ImageButton) findViewById(R.id.prev_button);
        if ((btnNext != null) && (btnPrev != null)) {
            btnNext.setOnClickListener(this);
            btnPrev.setOnClickListener(this);
        }

        clickButton = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click_alpha);

        readExpensesMoth(calendar.getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_EXPENSE.getCode()) {
            if (resultCode == STATUS_OK.getCode()) {
                readExpensesMoth(calendar.getTime());
            }
        }
    }

    private void readExpensesMoth(Date date) {
        try (ExpenseDAO dao = new ExpenseDAO(getApplicationContext())) {
            expenses = dao.select(date);
        } catch (Exception exception) {
            makeText(getApplicationContext(), getString(R.string.error_select), LENGTH_LONG);
            finish();
        }

        //order by desc date
        Collections.sort(expenses, new Comparator<Expense>() {

            @Override
            public int compare(Expense e1, Expense e2) {
                return e2.getDate().compareTo(e1.getDate());
            }
        });

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getValue();
        }

        txvTotalHistory.setText(decimalFormat.format(total));
        txvMonth.setText(dateFormat.format(calendar.getTime()));

        listView.setAdapter(new ExpenseAdapter(getApplicationContext(), expenses));
        listView.invalidate();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.next_button) {
            btnNext.startAnimation(clickButton);
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + DateUtils.ONE_MONTH);
            readExpensesMoth(calendar.getTime());
        } else if (id == R.id.prev_button) {
            btnPrev.startAnimation(clickButton);
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - DateUtils.ONE_MONTH);
            readExpensesMoth(calendar.getTime());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Expense expense = expenses.get(position);
        if (expense != null) {
            Intent intent = new Intent(getApplicationContext(), ExpenseActivity.class);
            intent.putExtra(ExtraType.EXPENSE.getExtraAttribute(), expense);
            startActivityForResult(intent, REQUEST_EDIT_EXPENSE.getCode());
        }
    }
}
