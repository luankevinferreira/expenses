package luankevinferreira.expenses.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static luankevinferreira.expenses.database.DatabaseManager.Expense.DESCRIPTION;
import static luankevinferreira.expenses.database.DatabaseManager.Expense.EXPENSE_DATE;
import static luankevinferreira.expenses.database.DatabaseManager.Expense.TABLE;
import static luankevinferreira.expenses.database.DatabaseManager.Expense.TYPE;
import static luankevinferreira.expenses.database.DatabaseManager.Expense.VALUE;
import static luankevinferreira.expenses.database.DatabaseManager.Expense._ID;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "expenses";
    private static final int VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, DATA_BASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" + _ID + " INTEGER PRIMARY KEY, " +
                DESCRIPTION + " TEXT, " + VALUE + " DOUBLE," + EXPENSE_DATE
                + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + TYPE + " TEXT DEFAULT \"Undefined\");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public static class Expense {
        public static final String TABLE = "expense";
        public static final String _ID = "_id";
        public static final String DESCRIPTION = "description";
        public static final String VALUE = "value";
        public static final String EXPENSE_DATE = "expense_date";
        public static final String TYPE = "expense_type";
    }
}
