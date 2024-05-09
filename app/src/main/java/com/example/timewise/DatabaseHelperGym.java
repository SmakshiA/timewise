package com.example.timewise;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperGym extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "exercise_db"; // Change to public
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_EXERCISE = "exercise"; // Change to public
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IMAGE_RESOURCE = "image_resource";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_AUDIO_RESOURCE = "audio_resource";

    // Create the exercise table
    private static final String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISE + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_IMAGE_RESOURCE + " INTEGER," +
            COLUMN_NAME + " TEXT," +
            COLUMN_DURATION + " INTEGER," +
            COLUMN_AUDIO_RESOURCE + " INTEGER" +
            ");";

    public DatabaseHelperGym(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EXERCISE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
    }
}
