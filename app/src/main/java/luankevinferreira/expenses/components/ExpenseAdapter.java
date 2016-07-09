package luankevinferreira.expenses.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import luankevinferreira.expenses.R;
import luankevinferreira.expenses.domain.Expense;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ExpenseAdapter extends BaseAdapter {

    private List<Expense> expenses;
    private Context context;
    private DecimalFormat formatter;

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        this.expenses = expenses;
        this.context = context;
        formatter = new DecimalFormat(context.getString(R.string.decimal_pattern));
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

        TextView expense = (TextView) lineAdapter.findViewById(R.id.line_expense);
        TextView description = (TextView) lineAdapter.findViewById(R.id.line_description);
        TextView type = (TextView) lineAdapter.findViewById(R.id.line_type);

        expense.setText(formatter.format(expenses.get(position).getValue()));
        description.setText(expenses.get(position).getDescription());
        type.setText(expenses.get(position).getType());

        return lineAdapter;
    }
}
