package com.example.timewise;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class journal1 extends AppCompatActivity
{
    DbHelper_journal dbHelper;
    String journalEntry;
    EditText journalEntryEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal1);

        dbHelper = new DbHelper_journal(this);
        SQLiteDatabase dbw = dbHelper.getWritableDatabase();
        SQLiteDatabase dbr = dbHelper.getReadableDatabase();

        EditText dateTextView = findViewById(R.id.editTextDate);

        String selectedDate = getIntent().getStringExtra("selectedDate");
        dateTextView.setText(selectedDate);

        journalEntryEditText = findViewById(R.id.editTextText);


        if (entryExistsInDatabase(selectedDate))
        {
            String textFromDatabase = retrieveEntryFromDatabase(selectedDate,dbr).toString();
            Log.d("testing",textFromDatabase);
            journalEntryEditText.setText(textFromDatabase);
        }

        Button okay = (Button) findViewById(R.id.button);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                journalEntry = journalEntryEditText.getText().toString();
                Log.d("willwork",journalEntry);


                    if (entryExistsInDatabase(selectedDate))
                    {
                        // Entry already exists, update it in the database

                        ContentValues values = new ContentValues();
                        values.put(DbHelper_journal.COLUMN_TEXT, journalEntry);

                        String whereClause = DbHelper_journal.COLUMN_DATE + " = ?";
                        String[] whereArgs = {selectedDate};

                        dbw.update(DbHelper_journal.TABLE_ENTRIES, values, whereClause, whereArgs);
                        dbw.close();
                    }
                    else
                    {
                        ContentValues values = new ContentValues();
                        values.put(DbHelper_journal.COLUMN_DATE, selectedDate);
                        values.put(DbHelper_journal.COLUMN_TEXT, journalEntry);
                        dbw.insert(DbHelper_journal.TABLE_ENTRIES,null,values);

                        Log.d("testing",journalEntry);

                        dbw.close();
                    }
                }

        });

    }

    public boolean entryExistsInDatabase(String date)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = DbHelper_journal.COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};
        Cursor cursor = db.query(DbHelper_journal.TABLE_ENTRIES, null, selection, selectionArgs, null, null, null);

        boolean entryExists = cursor.getCount() > 0;
        cursor.close();
        return entryExists;
    }

    public String retrieveEntryFromDatabase(String date, SQLiteDatabase db)
    {
        String[] projection = {DbHelper_journal.COLUMN_TEXT};
        String selection = DbHelper_journal.COLUMN_DATE + " = ?";
        String[] selectionArgs = {date};
        Cursor cursor = db.query(DbHelper_journal.TABLE_ENTRIES, projection, selection, selectionArgs, null, null, null);

        String entry = "";

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DbHelper_journal.COLUMN_TEXT);
            entry = cursor.getString(columnIndex);
        }

        cursor.close();
        return entry;
    }

}

