package luankevinferreira.expenses.components;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import luankevinferreira.expenses.R;

import static android.app.DatePickerDialog.OnDateSetListener;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener {

    private Button button;
    private SimpleDateFormat format;
    private Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale = new Locale(getString(R.string.language), getString(R.string.country));
        format = new SimpleDateFormat(getString(R.string.date_pattern), locale);
        calendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle bundle) {
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH);
        int day = calendar.get(DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(requireActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        GregorianCalendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        if (button == null) {
            button = getActivity().findViewById(R.id.date_picker);
        }
        if (button != null) {
            button.setText(format.format(calendar.getTime()));
        }
    }
}
