package com.example.timewise;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicineR extends AppCompatActivity {

    String medicineName;
    int hour;
    int minute;
    List<String> items = new ArrayList<>();
    String newitem;
    medicineadaptor adapter; // Assuming you have a custom adapter called MedicineAdapter
    RecyclerView recyclerView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_activity);

        DatabaseHelperMed dbHelper = new DatabaseHelperMed(MedicineR.this);
        db = dbHelper.getWritableDatabase();

        scheduleNotifications();

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(MedicineR.this));
        adapter = new medicineadaptor(items,db); // Initialize your custom adapter
        recyclerView.setAdapter(adapter);



        FloatingActionButton add = findViewById(R.id.floatingActionButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMedicineDialog();
            }
        });

        loadActiveItemsFromDatabase();
    }

    private void addMedicine(String MedicineName, int hour, int minute) {

        String newMedicine = MedicineName + " " + hour + ":" + minute;
        items.add(newMedicine);
        adapter.notifyDataSetChanged();
        saveActiveItemToDatabase(MedicineName, hour, minute);
        NotificationHelper.scheduleNotifications(this,MedicineName,hour,minute);
        scheduleNotification(hour, minute,MedicineName);

    }

    private void removeItemFromDatabase(String canceledItem) {
        String selection = DatabaseHelperMed.COLUMN_MEDICINE_NAME + " = ?";
        String[] selectionArgs = { canceledItem };

        try {
            int deletedRows = db.delete(DatabaseHelperMed.TABLE_ACTIVE_MEDICINE, selection, selectionArgs);

            if (deletedRows > 0) {
                // Item removed from the database
                // You can handle this event if needed, e.g., showing a message
                Log.d("Database", "Deleted " + deletedRows + " rows.");
            } else {
                Log.e("Database", "No rows deleted.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Database", "Error deleting item: " + e.getMessage());
        }
    }


    private long saveActiveItemToDatabase(String medName, int hour, int minute) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperMed.COLUMN_MEDICINE_NAME, medName);
        values.put(DatabaseHelperMed.COLUMN_HOUR, hour);
        values.put(DatabaseHelperMed.COLUMN_MINUTE, minute);

        long newRowId = -1; // Initialize to -1 in case of an error

        try {
            newRowId = db.insertOrThrow(DatabaseHelperMed.TABLE_ACTIVE_MEDICINE, null, values);
            // The insertOrThrow method will throw an exception if the insertion fails.
            // If newRowId is not -1, the insertion was successful.
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error, e.g., show an error message or log the exception.
        }

        return newRowId;
    }


    private void loadActiveItemsFromDatabase() {
        items.clear();
        Cursor cursor = db.query(
                DatabaseHelperMed.TABLE_ACTIVE_MEDICINE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String mName = cursor.getString(cursor.getColumnIndex(DatabaseHelperMed.COLUMN_MEDICINE_NAME));
                @SuppressLint("Range") int hour = cursor.getInt(cursor.getColumnIndex(DatabaseHelperMed.COLUMN_HOUR));
                @SuppressLint("Range") int minute = cursor.getInt(cursor.getColumnIndex(DatabaseHelperMed.COLUMN_MINUTE));
                String newItem = mName + " " + hour + ":" + minute;
                items.add(newItem);
            }

            cursor.close();
            adapter.notifyDataSetChanged();
        }
    }

    private void scheduleNotification(int hour, int minute, String mN) {
        // Use the AlarmManager to schedule a notification at the specified time
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent notificationIntent = new Intent(this, NotificationReceiver.class);

        notificationIntent.putExtra("hour",hour);
        notificationIntent.putExtra("minute",minute);
        notificationIntent.putExtra("medn",mN);
        int requestCode = hour * 100 + minute; // Unique request code based on time
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode, // Use the unique request code
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        long triggerTime = calendar.getTimeInMillis();

        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
    }

    private void scheduleNotifications() {
        // Query the database to get all medicine times
        Cursor cursor = db.query(
                DatabaseHelperMed.TABLE_ACTIVE_MEDICINE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int hour = cursor.getInt(cursor.getColumnIndex(DatabaseHelperMed.COLUMN_HOUR));
                @SuppressLint("Range") int minute = cursor.getInt(cursor.getColumnIndex(DatabaseHelperMed.COLUMN_MINUTE));
                @SuppressLint("Range") String medicineName = cursor.getString(cursor.getColumnIndex(DatabaseHelperMed.COLUMN_MEDICINE_NAME));
                // Schedule a notification for this time
                scheduleNotification(hour, minute,medicineName);
            }
            cursor.close();
        }
    }

    private void openMedicineDialog() {
        final Dialog dialog = new Dialog(MedicineR.this);
        dialog.setContentView(R.layout.medicineadd);
        dialog.show();

        Button setButton = dialog.findViewById(R.id.buttonSet);
        Button cancelButton = dialog.findViewById(R.id.buttonCancel);

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText medicineNameEditText = dialog.findViewById(R.id.medicinename);
                TimePicker timePicker = dialog.findViewById(R.id.time);

                medicineName = medicineNameEditText.getText().toString();
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    hour = timePicker.getHour();
                    minute = timePicker.getMinute();
                } else {
                    hour = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();
                }
                newitem = medicineName + " " + hour + ":" + minute;
                addMedicine(medicineName, hour, minute);
                dialog.dismiss();
            }
        });



        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
