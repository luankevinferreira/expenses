package luankevinferreira.expenses;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import luankevinferreira.expenses.components.DatePickerFragment;
import luankevinferreira.expenses.dao.ExpenseDAO;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.domain.Type;
import luankevinferreira.expenses.enumeration.CodeIntentType;
import luankevinferreira.expenses.enumeration.ExtraType;
import luankevinferreira.expenses.util.SpinnerUtils;

import static android.view.View.OnClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class ExpenseActivity extends AppCompatActivity implements OnClickListener {

    private EditText expenseValue, expenseDescription;
    private TextView expenseDate;
    private Spinner expenseType;

    private Expense expenseExtra;
    private SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        Calendar calendar = Calendar.getInstance();
        Locale locale = new Locale(getString(R.string.language), getString(R.string.country));
        format = new SimpleDateFormat(getString(R.string.date_pattern), locale);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expenseValue = (EditText) findViewById(R.id.expense_value);
        expenseDescription = (EditText) findViewById(R.id.expense_description);
        expenseType = (Spinner) findViewById(R.id.expense_type);
        expenseDate = (TextView) findViewById(R.id.date_picker);

        Button btnSave = (Button) findViewById(R.id.save_expense);
        if (btnSave != null)
            btnSave.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            expenseExtra = (Expense) bundle.getSerializable(ExtraType.EXPENSE.getExtraAttribute());

        // if exist extra attribute, set the edition mode to screen
        if (expenseExtra != null)
            editionMode(calendar);

        if (expenseDate != null) {
            expenseDate.setOnClickListener(this);
            expenseDate.setText(format.format(calendar.getTime()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (expenseExtra != null) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            try (ExpenseDAO dao = new ExpenseDAO(getApplicationContext())) {
                dao.delete(expenseExtra);
                makeText(getApplicationContext(), getString(R.string.success_delete), LENGTH_LONG).show();
                setResult(CodeIntentType.STATUS_OK.getCode());
            } catch (Exception exception) {
                setResult(CodeIntentType.STATUS_ERROR.getCode());
                makeText(getApplicationContext(), getString(R.string.error_delete), LENGTH_LONG);
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.date_picker) {
            new DatePickerFragment().show(getFragmentManager(), "datePicker");
        } else if (id == R.id.save_expense) {
            if (expenseValue.getText().toString().isEmpty()) {
                expenseValue.setError(getString(R.string.msg_error_value));
                return;
            }
            try (ExpenseDAO dao = new ExpenseDAO(getApplicationContext())) {
                saveExpense(dao);
            } catch (Exception exception) {
                setResult(CodeIntentType.STATUS_ERROR.getCode());
                makeText(getApplicationContext(), getString(R.string.error_save), LENGTH_LONG);
            }
            finish();
        }
    }

    private void saveExpense(ExpenseDAO dao) throws Exception {
        Date dateExpense = format.parse(expenseDate.getText().toString());

        Expense expense = new Expense();
        expense.setDate(dateExpense);
        expense.setValue(Double.valueOf(expenseValue.getText().toString()));
        expense.setDescription(expenseDescription.getText().toString());
        expense.setType(expenseType.getSelectedItem().toString());

        boolean result;
        if (expenseExtra != null) {
            expense.setId(expenseExtra.getId());
            result = dao.update(expense);
        } else {
            result = dao.insert(expense);
        }

        if (result) {
            Toast.makeText(this, getString(R.string.success_save), Toast.LENGTH_SHORT).show();
            setResult(CodeIntentType.STATUS_OK.getCode());
        }
    }

    private void editionMode(Calendar calendar) {
        calendar.setTime(expenseExtra.getDate());
        expenseValue.setText(String.valueOf(expenseExtra.getValue()));
        expenseDescription.setText(expenseExtra.getDescription());

        List<Type> items = new SpinnerUtils().retrieveAllItems(expenseType);
        for (Type type : items) {
            if (expenseExtra.getType().equals(type.getName()))
                expenseType.setSelection(type.getId());
        }
    }
}
