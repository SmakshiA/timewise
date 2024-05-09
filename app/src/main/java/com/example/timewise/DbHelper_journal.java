package com.example.timewise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper_journal extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "trial.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_ENTRIES = "journal_entries";
    public static final String COLUMN_DATE = "entry_date";
    public static final String COLUMN_TEXT = "entry_text";


    public DbHelper_journal(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + TABLE_ENTRIES + "(" + COLUMN_DATE +" TEXT, "
                + COLUMN_TEXT
                + " TEXT)";
        db.execSQL(DATABASE_CREATE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

