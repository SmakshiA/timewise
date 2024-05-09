package com.example.timewise;

import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// ListItem.java
public class ListItem {
    private String Category;
    private String Expense;


    public ListItem(String firstInput, String secondInput) {
        this.Category = firstInput;
        this.Expense = secondInput;
    }
    private String getCurrentDate() {
        // Create a date formatter
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Get the current date
        Date date = new Date(System.currentTimeMillis());

        // Format the date and return as a string
        return sdf.format(date);
    }

    public String getCategory() {
        return Category;
    }

    public String getExpense() {
        return Expense;
    }
}
