package com.example.timewise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperMed extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "medicine1.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ACTIVE_MEDICINE = "active_medicine";
    public static final String TABLE_CANCELED_MEDICINE = "canceled_medicine";
    public static final String COLUMN_CANCELED_MEDICINE_NAME = "canceled_medicine_name";

    // Other constants...
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MEDICINE_NAME = "medicine_name";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTE = "minute";

    // Create the active medicine table
    private static final String CREATE_ACTIVE_MEDICINE_TABLE =
            "CREATE TABLE " + TABLE_ACTIVE_MEDICINE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MEDICINE_NAME + " TEXT, " +
                    COLUMN_HOUR + " INTEGER, " +
                    COLUMN_MINUTE + " INTEGER " +
                    ")";

    // Create the canceled medicine table
    private static final String CREATE_CANCELED_MEDICINE_TABLE =
            "CREATE TABLE " + TABLE_CANCELED_MEDICINE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_MEDICINE_NAME + " TEXT " +
                    ")";

    public DatabaseHelperMed(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACTIVE_MEDICINE_TABLE);
        db.execSQL(CREATE_CANCELED_MEDICINE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVE_MEDICINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANCELED_MEDICINE);
        onCreate(db);
    }


}

