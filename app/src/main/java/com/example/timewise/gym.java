package com.example.timewise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class gym extends AppCompatActivity {

    private List<Exercise> exerciseList = new ArrayList<>();
    private RecyclerView exerciseRecyclerView;
    private ExerciseAdapter exerciseAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym);

        Context appContext = getApplicationContext();

        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView);
        exerciseAdapter = new ExerciseAdapter(exerciseList, this);
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        exerciseRecyclerView.setAdapter(exerciseAdapter);

        loadExercisesFromDatabase(); // Load exercises from the database on app startup



        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a dialog to prompt the user for exercise details
                final Dialog dialog = new Dialog(gym.this);
                dialog.setContentView(R.layout.exer);

                // Find the views in the dialog layout
                final EditText exerciseNameEditText = dialog.findViewById(R.id.exerciseNameEditText);
                final EditText exerciseDurationEditText = dialog.findViewById(R.id.exerciseDurationEditText);
                Button addExerciseButton = dialog.findViewById(R.id.addExerciseButton);

                // Set a click listener for the "Add Exercise" button in the dialog
                addExerciseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String exerciseName = exerciseNameEditText.getText().toString();
                        String durationText = exerciseDurationEditText.getText().toString();

                        if (!exerciseName.isEmpty() && !durationText.isEmpty()) {
                            int duration = Integer.parseInt(durationText);

                            // Create the exercise and add it to the database
                            Exercise newExercise = new Exercise(R.drawable.m1, exerciseName, duration, R.raw.sample);
                            exerciseAdapter.addExercise(newExercise); // Add to database

                            // Dismiss the dialog
                            dialog.dismiss();

                            // Show a Toast message when a new exercise is added
                            Toast.makeText(gym.this, "New exercise added", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle invalid input
                            Toast.makeText(gym.this, "Exercise name and duration are required.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                dialog.show();
            }
        });
    }

    private void loadExercisesFromDatabase() {

        exerciseList.clear(); // Clear the existing list
        exerciseList.addAll(exerciseAdapter.getAllExercises()); // Load exercises from the database
        exerciseAdapter.notifyDataSetChanged();
    }

}