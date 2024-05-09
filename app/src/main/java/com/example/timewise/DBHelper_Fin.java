package com.example.timewise;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_Fin extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expenses_databasesample";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "expenses_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AMOUNT = "amount";

    private static final String TABLE_BUDGET = "budget_table";
    private static final String COLUMN_BUDGET_ID = "id";
    private static final String COLUMN_BUDGET_AMOUNT = "amount";

    private static final String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + " ("
            + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BUDGET_AMOUNT + " REAL)";

    public DBHelper_Fin(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_AMOUNT + " REAL)";
        db.execSQL(createTable);
        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + " ("
                + COLUMN_BUDGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BUDGET_AMOUNT + " REAL)";
        db.execSQL(CREATE_BUDGET_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addExpense(String category, double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CATEGORY, category);
        contentValues.put(COLUMN_AMOUNT, amount);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public void deleteAllExpenses() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public boolean addBudget(double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BUDGET_AMOUNT, amount);
        long result = db.insert(TABLE_BUDGET, null, contentValues);
        return result != -1;
    }

    public boolean updateBudget(double newAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BUDGET_AMOUNT, newAmount);
        int updatedRows = db.update(TABLE_BUDGET, contentValues, null, null);
        return updatedRows > 0;
    }

    @SuppressLint("Range")
    public double getBudget() {
        SQLiteDatabase db = this.getReadableDatabase();
        double budget = 0;
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_BUDGET_AMOUNT + " FROM " + TABLE_BUDGET, null);
        if (cursor.moveToFirst()) {
            budget = cursor.getDouble(cursor.getColumnIndex(COLUMN_BUDGET_AMOUNT));
        }
        cursor.close();
        return budget;
    }

}