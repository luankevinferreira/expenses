package luankevinferreira.expenses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import luankevinferreira.expenses.dao.TypeDAO;
import luankevinferreira.expenses.enumeration.CodeIntentType;

public class TypeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.save_expense_type) {
            // save type
            try {
                TypeDAO dao = new TypeDAO(getApplicationContext());
                if (dao.insert()) {
                    setResult(CodeIntentType.STATUS_OK.getCode());
                    finish();
                } else {

                }
            } catch (Exception ex) {

            } finally {
                dao.close();
            }
        }
    }
}
