package luankevinferreira.expenses.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import luankevinferreira.expenses.database.DatabaseManager;
import luankevinferreira.expenses.domain.Type;

import static luankevinferreira.expenses.database.DatabaseManager.DESCRIPTION;
import static luankevinferreira.expenses.database.DatabaseManager.TABLE_TYPE;

/**
 * @author Luan Kevin Ferreira
 */
public class TypeDAO implements Approachable<Type>, Closeable {

    private static final int QUERY_ERROR = -1;

    private DatabaseManager databaseManager;
    private SQLiteDatabase sqLiteDatabase;

    public TypeDAO(Context context) {
        databaseManager = new DatabaseManager(context);
    }

    private SQLiteDatabase getSqLiteDatabase() {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseManager.getWritableDatabase();
        }
        return sqLiteDatabase;
    }

    /**
     * @return list of all description types of expenses stored in the database.
     */
    public List<String> findAllDescriptions() {
        Cursor cursor = getSqLiteDatabase().rawQuery("SELECT * FROM type", null);

        List<String> types = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                types.add(cursor.getString(cursor.getColumnIndex(DESCRIPTION)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return types;
    }

    @Override
    public boolean insert(Type item) throws Exception {
        ContentValues values = new ContentValues();
        values.put(DESCRIPTION, item.getName());

        return getSqLiteDatabase().insert(TABLE_TYPE, null, values) != QUERY_ERROR;
    }

    @Override
    public boolean update(Type item) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Type item) throws Exception {
        return false;
    }

    @Override
    public void close() {
        databaseManager.close();
        sqLiteDatabase = null;
    }
}
