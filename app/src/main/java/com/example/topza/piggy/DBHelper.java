package com.example.topza.piggy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by topza on 16/6/2559.
 */
public class DBHelper extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();
    private SQLiteDatabase sqLiteDatabase;

    public DBHelper(Context context) {
        super(context, HistoryTable.DATABASE_NAME, null, HistoryTable.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_HISTORY_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY  AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)",
                HistoryTable.TABLE,
                HistoryTable.Column.ID,
                HistoryTable.Column.Application,
                HistoryTable.Column.Money,
                HistoryTable.Column.Time,
                HistoryTable.Column.Input_Output
        );

        Log.i(TAG, CREATE_HISTORY_TABLE);

        db.execSQL(CREATE_HISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_HISTORY_TABLE = "DROP TABLE IF EXISTS " + HistoryTable.TABLE;
        db.execSQL(DROP_HISTORY_TABLE);
        Log.i(TAG, "Upgrade Database from " + oldVersion + " to " + newVersion);
        onCreate(db);
    }

    public Cursor getHistoryList() {
        sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query
                (HistoryTable.TABLE, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        sqLiteDatabase.close();

        return cursor;
    }

    public HistoryTable getHistory(String id) {
        sqLiteDatabase = this.getReadableDatabase();
        HistoryTable history = new HistoryTable();

        Cursor cursor = sqLiteDatabase.query(HistoryTable.TABLE,
                null, HistoryTable.Column.ID + " = " + id, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        history.setId(cursor.getInt(0));
        history.setHistoryApp(cursor.getString(1));
        history.setHistoryMoney(cursor.getString(2));
        history.setHistoryTime(cursor.getString(3));
        history.setHistoryIO(cursor.getString(4));

        return history;
    }

    public void addHistory(HistoryTable history) {
        sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(HistoryTable.Column.ID,history.getId());
        values.put(HistoryTable.Column.Application, history.getHistoryApp());
        values.put(HistoryTable.Column.Money, history.getHistoryMoney());
        values.put(HistoryTable.Column.Time, history.getHistoryTime());
        values.put(HistoryTable.Column.Input_Output, history.getHistoryIO());

        sqLiteDatabase.insert(HistoryTable.TABLE, null, values);
        sqLiteDatabase.close();
    }

    public int getCountDatabase() {

        sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(HistoryTable.TABLE,
                null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        int count = cursor.getCount();

        return count;
    }

    public void deleteHistory(String id) {

        sqLiteDatabase = this.getWritableDatabase();

    /*sqLiteDatabase.delete(Friend.TABLE, Friend.Column.ID + " = ? ",
            new String[] { String.valueOf(friend.getId()) });*/
        sqLiteDatabase.delete(HistoryTable.TABLE, HistoryTable.Column.ID + " = " + id, null);

        sqLiteDatabase.close();
    }

}
