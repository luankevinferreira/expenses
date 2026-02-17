package luankevinferreira.expenses;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import luankevinferreira.expenses.dao.TypeDAO;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.domain.Type;
import luankevinferreira.expenses.enumeration.CodeIntentType;
import luankevinferreira.expenses.enumeration.ExtraType;
import luankevinferreira.expenses.util.SpinnerUtils;

import static android.view.View.OnClickListener;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;
import static luankevinferreira.expenses.enumeration.CodeIntentType.REQUEST_NEW_EXPENSE_TYPE;
import static luankevinferreira.expenses.enumeration.CodeIntentType.STATUS_OK;

public class ExpenseActivity extends AppCompatActivity implements OnClickListener {

    private EditText expenseValue, expenseDescription;
    private TextView expenseDate;
    private Spinner expenseType;
    private ImageView expense_type_add;

    private Expense expenseExtra;
    private SimpleDateFormat format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setSupportActionBar(findViewById(R.id.toolbar));

        Calendar calendar = Calendar.getInstance();
        Locale locale = new Locale(getString(R.string.language), getString(R.string.country));
        format = new SimpleDateFormat(getString(R.string.date_pattern), locale);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expenseValue = findViewById(R.id.expense_value);
        expenseDescription = findViewById(R.id.expense_description);
        expenseDate = findViewById(R.id.date_picker);

        expense_type_add = findViewById(R.id.expense_type_add);
        if (expense_type_add != null) {
            expense_type_add.setOnClickListener(this);
        }

        expenseType = findViewById(R.id.expense_type);
        TypeDAO typeDAO = new TypeDAO(getApplicationContext());
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, typeDAO.findAllDescriptions());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseType.setAdapter(dataAdapter);
        typeDAO.close();

        Button btnSave = findViewById(R.id.save_expense);
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
            ExpenseDAO dao = new ExpenseDAO(getApplicationContext());
            try {
                dao.delete(expenseExtra);
                makeText(getApplicationContext(), getString(R.string.success_delete), LENGTH_LONG).show();
                setResult(CodeIntentType.STATUS_OK.getCode());
            } catch (Exception exception) {
                setResult(CodeIntentType.STATUS_ERROR.getCode());
                makeText(getApplicationContext(), getString(R.string.error_delete), LENGTH_LONG).show();
            } finally {
                dao.close();
            }
            finish();
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.date_picker) {
            new DatePickerFragment().show(getSupportFragmentManager(), "datePicker");
        } else if (id == R.id.save_expense) {
            if (expenseValue.getText().toString().isEmpty()) {
                expenseValue.setError(getString(R.string.msg_error_value));
                return;
            }
            ExpenseDAO dao = new ExpenseDAO(getApplicationContext());
            try {
                saveExpense(dao);
            } catch (Exception exception) {
                setResult(CodeIntentType.STATUS_ERROR.getCode());
                makeText(getApplicationContext(), getString(R.string.error_save), LENGTH_LONG).show();
            } finally {
                dao.close();
            }
            finish();
        } else if (id == R.id.expense_type_add) {
            Intent intent = new Intent(getApplicationContext(), TypeActivity.class);
            startActivityForResult(intent, REQUEST_NEW_EXPENSE_TYPE.getCode());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NEW_EXPENSE_TYPE.getCode()) {
            if (resultCode == STATUS_OK.getCode()) {
                TypeDAO typeDAO = new TypeDAO(getApplicationContext());
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, typeDAO.findAllDescriptions());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                expenseType.setAdapter(dataAdapter);
                typeDAO.close();
            }
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
            if (expenseExtra.getType().equals(type.getName())) {
                // Find index of type.getName() in adapter
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) expenseType.getAdapter();
                int position = adapter.getPosition(type.getName());
                if (position >= 0) {
                    expenseType.setSelection(position);
                }
            }
        }
    }
}
