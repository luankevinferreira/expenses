package luankevinferreira.expenses.util;

import android.widget.Adapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import luankevinferreira.expenses.domain.Type;

public class SpinnerUtils {

    public List<Type> retrieveAllItems(Spinner theSpinner) {
        Adapter adapter = theSpinner.getAdapter();
        int n = adapter.getCount();
        List<Type> types = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            Type type = new Type();
            type.setName(adapter.getItem(i).toString());
            type.setId(i);

            types.add(type);
        }
        return types;
    }
}
