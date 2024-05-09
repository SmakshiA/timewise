package com.example.timewise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Finance extends AppCompatActivity {

    //    private static final int ADD_EXPENSE_REQUEST = ;
    private DBHelper_Fin databaseHelper;
    private ImageButton EditBudget;
    private Button add;
    private TextView budget;
    private TextView rembudget;

    public RecyclerView recyclerView;
    private MyAdapter adapter;
    static int mystaticbudget=0;
    static int mystaticexp=0;
    private List<ListItem> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance);

        databaseHelper = new DBHelper_Fin(this);
        EditBudget = findViewById(R.id.EditBudget);
        budget = findViewById(R.id.budget);
        add = findViewById(R.id.addExpense);
        rembudget = findViewById(R.id.rembudget);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);


        budget.setText(Double.toString(databaseHelper.getBudget()));
        //Log.d("budget",Double.toString(databaseHelper.getBudget()));
        loadExpensesFromDatabase();
        updateRemainingBudget();
//        databaseHelper.deleteAllExpenses();

        EditBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show the pop-up window
                showPopUp();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUp2();
            }
        });
    }

    private void showPopUp2(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_add_expense);

        Button addButton = dialog.findViewById(R.id.addbutton);
        EditText categoryEditText = dialog.findViewById(R.id.categoryEditText);
        EditText exp = dialog.findViewById(R.id.exp);
        addButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                String category = categoryEditText.getText().toString();
                String userExp = exp.getText().toString();
                Double amtexp = Double.parseDouble(userExp);
//                Double exp = Double.parseDouble(budget.getText().toString());
//                Double amt = exp - amtexp;
//                rembudget.setText("Remaining budget : "+amt.toString());
                boolean isInserted = databaseHelper.addExpense(category, amtexp);
                if (isInserted) {
                    Toast.makeText(Finance.this, "Expense added to database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Finance.this, "Failed to add expense", Toast.LENGTH_SHORT).show();
                }

                // Update the remaining budget
                updateRemainingBudget();

                itemList.add(new ListItem(category,userExp));
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showPopUp() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_layout);

        final EditText editText = dialog.findViewById(R.id.editTextNumberDecimal);
        Button okButton = dialog.findViewById(R.id.okButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get text from the EditText and update the TextView
                String userInput = editText.getText().toString();
                Double amtexp = Double.parseDouble(userInput);

                boolean isInserted = databaseHelper.addBudget(amtexp);

                if (isInserted) {
                    Toast.makeText(Finance.this, "Budget added to database", Toast.LENGTH_SHORT).show();
                    budget.setText(userInput); // Update the TextView with the new budget value
                    databaseHelper.updateBudget(amtexp);

                    Cursor cursor = databaseHelper.getAllExpenses();
                    double totalExpenses = 0;

                    if (cursor.moveToFirst()) {
                        do {
                            @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                            totalExpenses += amount;
                        } while (cursor.moveToNext());
                    }

                    double remainingBudget = databaseHelper.getBudget() - totalExpenses;
                    rembudget.setText("Remaining budget: " + remainingBudget);
                } else {
                    Toast.makeText(Finance.this, "Failed to add budget", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss(); // Close the pop-up window
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss(); // Close the pop-up window
            }
        });

        dialog.show();
    }

    private void updateRemainingBudget() {
        Cursor cursor = databaseHelper.getAllExpenses();
        double totalExpenses = 0;

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                totalExpenses += amount;
            } while (cursor.moveToNext());
        }

        double remainingBudget = databaseHelper.getBudget() - totalExpenses;
        rembudget.setText("Remaining budget: " + remainingBudget);
    }

    private void loadExpensesFromDatabase() {
        Cursor cursor = databaseHelper.getAllExpenses();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                itemList.add(new ListItem(category, String.valueOf(amount)));
            }
            cursor.close(); // Close the cursor after fetching data
        }

        // Notify the adapter about the changes in the data
        adapter.notifyDataSetChanged();

        // Update the remaining budget based on loaded expenses
        updateRemainingBudget();
    }


}
