package luankevinferreira.expenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import luankevinferreira.expenses.dao.TypeDAO;
import luankevinferreira.expenses.domain.Type;
import luankevinferreira.expenses.enumeration.CodeIntentType;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

public class TypeActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextType;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextType = findViewById(R.id.add_expense_type);

        btnSave = findViewById(R.id.save_expense_type);
        if (btnSave != null)
            btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.save_expense_type) {
            TypeDAO dao = null;
            try {
                dao = new TypeDAO(getApplicationContext());
                Type type = new Type();
                type.setName(editTextType.getText().toString());
                if (dao.insert(type)) {
                    makeText(getApplicationContext(), getString(R.string.success_save), LENGTH_LONG).show();
                    setResult(CodeIntentType.STATUS_OK.getCode());
                } else {
                    setResult(CodeIntentType.STATUS_ERROR.getCode());
                    makeText(getApplicationContext(), getString(R.string.error_save), LENGTH_LONG).show();
                }
            } catch (Exception ex) {
                setResult(CodeIntentType.STATUS_ERROR.getCode());
                makeText(getApplicationContext(), getString(R.string.error_save), LENGTH_LONG).show();
            } finally {
                dao.close();
                finish();
            }
        }
    }
}
