package com.example.timewise;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;

import com.google.android.material.datepicker.MaterialCalendar;

import org.jetbrains.annotations.NonNls;

import java.util.Calendar;

public class calender_journal extends AppCompatActivity
{
    CalendarView calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_journal);

        calendar = (CalendarView) findViewById(R.id.calender);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String Date = dayOfMonth + "-" + (month + 1) + "-" + year;
                openJournalPage(Date);
            }
        });
    }
    private void openJournalPage(String selectedDate)
    {
        Intent journalIntent = new Intent(this, journal1.class);
        journalIntent.putExtra("selectedDate", selectedDate);
        startActivity(journalIntent);
    }
}