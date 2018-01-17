package luankevinferreira.expenses.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Luan Kevin Ferreira
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public static final String TABLE_EXPENSE = "expense";
    public static final String TABLE_TYPE = "type";
    public static final String _ID = "_id";
    public static final String DESCRIPTION = "description";

    public static final String VALUE = "value";
    public static final String EXPENSE_DATE = "expense_date";
    public static final String TYPE = "expense_type";

    private static final String DATA_BASE_NAME = "expenses";
    private static final int VERSION = 2;

    public DatabaseManager(Context context) {
        super(context, DATA_BASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_EXPENSE + " (" + _ID + " INTEGER PRIMARY KEY, " +
                DESCRIPTION + " TEXT, " + VALUE + " DOUBLE," + EXPENSE_DATE
                + " DATETIME DEFAULT CURRENT_TIMESTAMP, " + TYPE + " TEXT DEFAULT \"Undefined\");");

        db.execSQL("CREATE TABLE " + TABLE_TYPE + " (" + _ID + " INTERGER PRIMARY KEY, " +
                DESCRIPTION + " TEXT);");

        insertDefaultExpenseTypes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL("CREATE TABLE " + TABLE_TYPE + " (" + _ID + " INTERGER PRIMARY KEY, " +
                        DESCRIPTION + " TEXT);");
                insertDefaultExpenseTypes(db);
                break;
            default:
                break;
        }

    }

    private void insertDefaultExpenseTypes(SQLiteDatabase db) {
        List<ContentValues> values = new ArrayList<>();
        if (Locale.getDefault().getLanguage().equals(Locale.ENGLISH.getLanguage())) {
            ContentValues food = new ContentValues();
            food.put(DESCRIPTION, "Food");
            values.add(food);

            ContentValues education = new ContentValues();
            education.put(DESCRIPTION, "Education");
            values.add(education);

            ContentValues recreation = new ContentValues();
            recreation.put(DESCRIPTION, "Recreation");
            values.add(recreation);

            ContentValues dwelling = new ContentValues();
            dwelling.put(DESCRIPTION, "Dwelling");
            values.add(dwelling);

            ContentValues payments = new ContentValues();
            payments.put(DESCRIPTION, "Payments");
            values.add(payments);

            ContentValues clothes = new ContentValues();
            clothes.put(DESCRIPTION, "Clothes");
            values.add(clothes);

            ContentValues transportation = new ContentValues();
            transportation.put(DESCRIPTION, "Transportation");
            values.add(transportation);

            ContentValues others = new ContentValues();
            others.put(DESCRIPTION, "Others");
            values.add(others);
        } else {
            ContentValues food = new ContentValues();
            food.put(DESCRIPTION, "Alimentação");
            values.add(food);

            ContentValues education = new ContentValues();
            education.put(DESCRIPTION, "Educação");
            values.add(education);

            ContentValues recreation = new ContentValues();
            recreation.put(DESCRIPTION, "Lazer");
            values.add(recreation);

            ContentValues dwelling = new ContentValues();
            dwelling.put(DESCRIPTION, "Moradia");
            values.add(dwelling);

            ContentValues payments = new ContentValues();
            payments.put(DESCRIPTION, "Pagamentos");
            values.add(payments);

            ContentValues clothes = new ContentValues();
            clothes.put(DESCRIPTION, "Roupa");
            values.add(clothes);

            ContentValues transportation = new ContentValues();
            transportation.put(DESCRIPTION, "Transporte");
            values.add(transportation);

            ContentValues others = new ContentValues();
            others.put(DESCRIPTION, "Vários");
            values.add(others);
        }

        for (ContentValues c: values) {
            db.insert(TABLE_TYPE, null, c);
        }

    }
}
