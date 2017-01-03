package luankevinferreira.expenses.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import luankevinferreira.expenses.R;
import luankevinferreira.expenses.domain.Expense;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ExpenseAdapter extends BaseAdapter {

    private List<Expense> expenses;
    private Context context;
    private DecimalFormat formatter;
    private SimpleDateFormat dateFormat;

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        this.expenses = expenses;
        this.context = context;
        formatter = new DecimalFormat(context.getString(R.string.decimal_pattern));
        Locale locale = new Locale(context.getString(R.string.language), context.getString(R.string.country));
        dateFormat = new SimpleDateFormat(context.getString(R.string.date_pattern), locale);
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return expenses.get(position).hashCode();
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        View lineAdapter = inflater.inflate(R.layout.line_for_list, parent, false);

        TextView value = (TextView) lineAdapter.findViewById(R.id.line_expense);
        TextView description = (TextView) lineAdapter.findViewById(R.id.line_description);
        TextView type = (TextView) lineAdapter.findViewById(R.id.line_type);
        TextView date = (TextView) lineAdapter.findViewById(R.id.line_date);

        Expense expense = expenses.get(position);

        value.setText(formatter.format(expense.getValue()));
        description.setText(expense.getDescription());
        type.setText(expense.getType());
        date.setText(dateFormat.format(expense.getDate()));

        return lineAdapter;
    }
}
