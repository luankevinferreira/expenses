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

import luankevinferreira.expenses.database.DatabaseManager;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.domain.Type;
import luankevinferreira.expenses.util.DateUtils;

import static luankevinferreira.expenses.database.DatabaseManager.DESCRIPTION;
import static luankevinferreira.expenses.database.DatabaseManager.EXPENSE_DATE;
import static luankevinferreira.expenses.database.DatabaseManager.TABLE_EXPENSE;
import static luankevinferreira.expenses.database.DatabaseManager.TYPE;
import static luankevinferreira.expenses.database.DatabaseManager.VALUE;
import static luankevinferreira.expenses.database.DatabaseManager._ID;

public class ExpenseDAO implements Approachable<Expense>, Closeable {

    public static final String NO_FILTER_EN = "* ALL *";
    private static final String NO_FILTER_BR = "* TODOS *";
    private static final int QUERY_ERROR = -1;
    private static final int ZERO = 0;
    private DatabaseManager databaseManager;
    private SQLiteDatabase sqLiteDatabase;
    private DecimalFormat decimalFormat;
    private DateUtils dateUtils;

    public ExpenseDAO(Context context) {
        databaseManager = new DatabaseManager(context);
        decimalFormat = new DecimalFormat("00");
        dateUtils = new DateUtils();
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseManager.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    @Override
    public void close() {
        databaseManager.close();
        sqLiteDatabase = null;
    }

    @Override
    public boolean insert(Expense expense) throws Exception {
        ContentValues values = new ContentValues();
        values.put(VALUE, expense.getValue());
        values.put(EXPENSE_DATE, dateUtils.getStringDateTime(expense.getDate()));
        values.put(DESCRIPTION, expense.getDescription());
        values.put(TYPE, expense.getType());

        return getSqLiteDatabase().insert(TABLE_EXPENSE, null, values) != QUERY_ERROR;
    }

    @Override
    public boolean delete(Expense expense) throws Exception {
        String whereClause = _ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(expense.getId())};
        int removed = getSqLiteDatabase().delete(TABLE_EXPENSE, whereClause, whereArgs);
        return removed > ZERO;
    }

    @Override
    public boolean update(Expense expense) throws Exception {
        ContentValues values = new ContentValues();
        String[] whereArgs = new String[]{String.valueOf(expense.getId())};

        values.put(VALUE, expense.getValue());
        values.put(EXPENSE_DATE, dateUtils.getStringDateTime(expense.getDate()));
        values.put(DESCRIPTION, expense.getDescription());
        values.put(TYPE, expense.getType());

        return getSqLiteDatabase().update(TABLE_EXPENSE, values, _ID + " = ?", whereArgs) != QUERY_ERROR;
    }

    public List<Expense> select(Date date, String filter) throws ParseException {
        DateFormat format = dateUtils.getDateFormat();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String strMonth = decimalFormat.format(calendar.get(Calendar.MONTH) + DateUtils.ONE_MONTH);
        strMonth += decimalFormat.format(calendar.get(Calendar.YEAR));

        String query = "SELECT * FROM " + TABLE_EXPENSE + " WHERE strftime('%m%Y', " + EXPENSE_DATE + ") = ?";
        String[] whereArgs = new String[]{strMonth};

        if ((filter != null) && (!filter.isEmpty())) {
            if ((!filter.equals(NO_FILTER_BR)) && (!filter.equals(NO_FILTER_EN))) {
                query += " AND " + TYPE + " = ?";
                whereArgs = new String[]{strMonth, filter};
            }
        }

        Cursor cursor = getSqLiteDatabase().rawQuery(query, whereArgs);

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

    public double selectTotalMonth(Date date, String filter) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String actualMonth = decimalFormat.format(calendar.get(Calendar.MONTH) + DateUtils.ONE_MONTH);
        actualMonth += decimalFormat.format(calendar.get(Calendar.YEAR));

        String query = "SELECT SUM(" + VALUE + ") FROM " + TABLE_EXPENSE + " WHERE strftime('%m%Y', "
                + EXPENSE_DATE + ") = ?";

        String[] whereArgs = new String[]{actualMonth};

        if ((filter != null) && (!filter.isEmpty())) {
            if ((!filter.equals(NO_FILTER_BR)) && (!filter.equals(NO_FILTER_EN))) {
                query += " AND " + TYPE + " = ?";
                whereArgs = new String[]{actualMonth, filter};
            }
        }

        Cursor cursor = getSqLiteDatabase().rawQuery(query, whereArgs);

        double total = ZERO;

        if (cursor.moveToFirst()) {
            do {
                total += cursor.getDouble(0);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return total;
    }

    /**
     * Select in the database all distinct expense type from all expenses.
     *
     * @return List of distinct expenses types.
     * @throws Exception case any problem happened.
     */
    public List<Type> selectTypesExpenses() throws Exception {
        List<Type> types = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        String strMonth = decimalFormat.format(calendar.get(Calendar.MONTH) + DateUtils.ONE_MONTH);
        strMonth += decimalFormat.format(calendar.get(Calendar.YEAR));

        String query = "SELECT DISTINCT " + TYPE + " FROM " + TABLE_EXPENSE + " WHERE strftime('%m%Y', "
                + EXPENSE_DATE + ") = ?";

        String[] whereArgs = new String[]{strMonth};

        Cursor cursor = getSqLiteDatabase().rawQuery(query, whereArgs);

        int index = ZERO;
        if (cursor.moveToFirst()) {
            do {
                Type type = new Type();
                type.setId(++index);
                type.setName(cursor.getString(0));

                types.add(type);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return types;
    }

    /**
     * @return true if all records are deleted
     */
    boolean deleteAll() {
        return getSqLiteDatabase().delete(TABLE_EXPENSE, null, new String[]{}) == -1;
    }
}

