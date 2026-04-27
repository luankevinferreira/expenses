package luankevinferreira.expenses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.OutputStream;
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
import luankevinferreira.expenses.util.CurrencyUtils;
import luankevinferreira.expenses.util.DateUtils;
import luankevinferreira.expenses.util.ExpenseCsvUtils;

import static android.view.View.OnClickListener;
import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static luankevinferreira.expenses.enumeration.CodeIntentType.REQUEST_EDIT_EXPENSE;
import static luankevinferreira.expenses.enumeration.CodeIntentType.REQUEST_EXPORT_EXPENSES;
import static luankevinferreira.expenses.enumeration.CodeIntentType.STATUS_OK;

public class HistoryActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {

    private TextView txvMonth, txvTotalHistory;
    private ListView listView;
    private Animation clickButton;
    private ImageButton btnNext, btnPrev;

    private List<Expense> expenses;
    private Locale locale;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        calendar = Calendar.getInstance();
        locale = new Locale(getString(R.string.language), getString(R.string.country));
        dateFormat = new SimpleDateFormat(getString(R.string.date_month_pattern), locale);

        txvMonth = findViewById(R.id.month_expense);
        txvMonth.setText(dateFormat.format(calendar.getTime()));

        txvTotalHistory = findViewById(R.id.total_history);

        listView =findViewById(R.id.listView);
        if (listView != null)
            listView.setOnItemClickListener(this);

        btnNext = findViewById(R.id.next_button);
        btnPrev = findViewById(R.id.prev_button);
        if ((btnNext != null) && (btnPrev != null)) {
            btnNext.setOnClickListener(this);
            btnPrev.setOnClickListener(this);
        }

        clickButton = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.click_alpha);

        readExpensesMoth(calendar.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_export) {
            createExportDocument();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDIT_EXPENSE.getCode()) {
            if (resultCode == STATUS_OK.getCode()) {
                readExpensesMoth(calendar.getTime());
            }
        } else if ((requestCode == REQUEST_EXPORT_EXPENSES.getCode()) && (resultCode == RESULT_OK)
                && (data != null) && (data.getData() != null)) {
            exportExpenses(data.getData());
        }
    }

    private void readExpensesMoth(Date date) {
        ExpenseDAO dao = new ExpenseDAO(getApplicationContext());
        try {
            expenses = dao.select(date, ExpenseDAO.NO_FILTER_EN);
        } catch (Exception exception) {
            makeText(getApplicationContext(), getString(R.string.error_select), LENGTH_LONG).show();
            finish();
        } finally {
            dao.close();
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

        txvTotalHistory.setText(CurrencyUtils.formatCurrency(total, locale));
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

    private void createExportDocument() {
        SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        String fileName = getString(R.string.export_file_name,
                fileNameDateFormat.format(new Date()));

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, REQUEST_EXPORT_EXPENSES.getCode());
    }

    private void exportExpenses(Uri uri) {
        ExpenseDAO dao = null;

        try {
            dao = new ExpenseDAO(getApplicationContext());
            List<Expense> exportedExpenses = dao.selectAll();

            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                if (outputStream == null) {
                    throw new IOException("Unable to open export output stream");
                }

                ExpenseCsvUtils.write(exportedExpenses, outputStream);
            }

            makeText(getApplicationContext(), getString(R.string.success_export), LENGTH_LONG).show();
        } catch (Exception exception) {
            makeText(getApplicationContext(), getString(R.string.error_export), LENGTH_LONG).show();
        } finally {
            if (dao != null) {
                dao.close();
            }
        }
    }
}
