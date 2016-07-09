package luankevinferreira.expenses.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import luankevinferreira.expenses.database.DatabaseHelper;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.util.DateUtils;

import static luankevinferreira.expenses.database.DatabaseHelper.Expense.DESCRIPTION;
import static luankevinferreira.expenses.database.DatabaseHelper.Expense.EXPENSE_DATE;
import static luankevinferreira.expenses.database.DatabaseHelper.Expense.TABLE;
import static luankevinferreira.expenses.database.DatabaseHelper.Expense.TYPE;
import static luankevinferreira.expenses.database.DatabaseHelper.Expense.VALUE;
import static luankevinferreira.expenses.database.DatabaseHelper.Expense._ID;

public class ExpenseDAO implements Approachable<Expense>, Closeable {

    public static final int QUERY_ERROR = -1;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private DecimalFormat decimalFormat;
    private DateUtils dateUtils;

    public ExpenseDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
        decimalFormat = new DecimalFormat("00");
        dateUtils = new DateUtils();
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    @Override
    public void close() {
        databaseHelper.close();
        sqLiteDatabase = null;
    }

    @Override
    public boolean insert(Expense expense) throws Exception {
        ContentValues values = new ContentValues();
        values.put(VALUE, expense.getValue());
        values.put(EXPENSE_DATE, dateUtils.getStringDateTime(expense.getDate()));
        values.put(DESCRIPTION, expense.getDescription());
        values.put(TYPE, expense.getType());

        return getSqLiteDatabase().insert(TABLE, null, values) != QUERY_ERROR;
    }

    @Override
    public boolean delete(Expense expense) throws Exception {
        String whereClause = _ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(expense.getId())};
        int removed = getSqLiteDatabase().delete(TABLE, whereClause, whereArgs);
        return removed > 0;
    }

    @Override
    public boolean update(Expense expense) throws Exception {
        ContentValues values = new ContentValues();
        String[] whereArgs = new String[]{String.valueOf(expense.getId())};

        values.put(VALUE, expense.getValue());
        values.put(EXPENSE_DATE, dateUtils.getStringDateTime(expense.getDate()));
        values.put(DESCRIPTION, expense.getDescription());
        values.put(TYPE, expense.getType());

        return getSqLiteDatabase().update(TABLE, values, _ID + " = ?", whereArgs) != QUERY_ERROR;
    }

    public List<Expense> select(Date date) throws ParseException {
        DateFormat format = dateUtils.getDateFormat();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String strMonth = decimalFormat.format(calendar.get(Calendar.MONTH) + DateUtils.ONE_MONTH);

        Cursor cursor = getSqLiteDatabase().rawQuery("SELECT * FROM " + TABLE
                + " WHERE strftime('%m', " + EXPENSE_DATE + ") = ?", new String[]{strMonth});

        List<Expense> expenses = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense();
                expense.setId(cursor.getLong(cursor.getColumnIndex(_ID)));
                expense.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
                expense.setValue(cursor.getDouble(cursor.getColumnIndex(VALUE)));
                expense.setDate(format.parse(cursor.getString(cursor.getColumnIndex(EXPENSE_DATE))));
                expense.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
                expenses.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return expenses;
    }

    public double selectTotalMonth(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String strMonth = decimalFormat.format(calendar.get(Calendar.MONTH) + DateUtils.ONE_MONTH);

        Cursor cursor = getSqLiteDatabase().rawQuery("SELECT SUM(" + VALUE + ") FROM " + TABLE
                + "  WHERE strftime('%m', " + EXPENSE_DATE + ") = ?", new String[]{strMonth});

        double total = 0;

        if (cursor.moveToFirst()) {
            do {
                total += cursor.getDouble(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return total;
    }
}
