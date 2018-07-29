package com.uksw.mzebrowski.magicsquare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.Date;

public class MagicSquareDBHelper extends SQLiteOpenHelper {

    public MagicSquareDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DatabaseConsts.SCORES_TABLE_NAME + "("
                + DatabaseConsts.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseConsts.COL_DATE + " DATETIME NOT NULL, "
                + DatabaseConsts.COL_SCORE + " INTEGER NOT NULL); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public long addScoreAtNow(int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseConsts.COL_DATE, DateFormat.getDateTimeInstance().format(new Date()));
        cv.put(DatabaseConsts.COL_SCORE, score);
        return db.insert(DatabaseConsts.SCORES_TABLE_NAME, null, cv);
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(DatabaseConsts.SCORES_TABLE_NAME, DatabaseConsts.COLUMNS_TO_SHOW, null, null, null, null, null);
    }
}
