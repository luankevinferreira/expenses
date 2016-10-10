package luankevinferreira.expenses.util;

import android.content.Context;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import luankevinferreira.expenses.R;
import luankevinferreira.expenses.components.ExpenseAdapter;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.domain.Type;

public class SpinnerUtilsTest {

    @Test
    @Ignore
    public void whenRetrieveAllItemsMustBeRetrievedAllItems() {
        // Prepare
        String description = "Description";
        String type = "Type";
        double value = 123.12;
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getString(R.string.decimal_pattern)).thenReturn("$######0.00");
        List<Expense> expenses = new ArrayList<>();
        Expense expense = new Expense();
        expense.setDate(new Date());
        expense.setDescription(description);
        expense.setId(Long.MAX_VALUE);
        expense.setType(type);
        expense.setValue(value);
        expenses.add(expense);
        ExpenseAdapter adapter = new ExpenseAdapter(context, expenses);
        Spinner spinner = new Spinner(context);
        spinner.setAdapter(adapter);
        Mockito.when(spinner.getAdapter()).thenReturn((SpinnerAdapter)adapter);

        // Action
        List<Type> types =  new SpinnerUtils().retrieveAllItems(spinner);

        // Verify
        Assert.assertEquals(type, types.get(0).getName());
    }
}
