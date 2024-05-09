package com.example.timewise;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;

import java.io.IOException;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    private List<Exercise> exerciseList;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private DatabaseHelperGym dbHelper;
    private SQLiteDatabase database;
    private Context context;


    public ExerciseAdapter(List<Exercise> exerciseList, Context context) {
        this.exerciseList = exerciseList;
        dbHelper = new DatabaseHelperGym(context);
        database = dbHelper.getWritableDatabase();
        mediaPlayer = new MediaPlayer();
        this.context = context;
    }

    public void addExercise(Exercise exercise) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelperGym.COLUMN_IMAGE_RESOURCE, exercise.getImageResource());
        values.put(DatabaseHelperGym.COLUMN_NAME, exercise.getName());
        values.put(DatabaseHelperGym.COLUMN_DURATION, exercise.getDuration());
        values.put(DatabaseHelperGym.COLUMN_AUDIO_RESOURCE, exercise.getAudioResource());

        long id = database.insert(DatabaseHelperGym.TABLE_EXERCISE, null, values);
        if (id != -1) {
            exercise.setId((int) id);
            exerciseList.add(exercise);
            notifyItemInserted(exerciseList.size() - 1);
        }
    }

    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();
        Cursor cursor = database.query(
                DatabaseHelperGym.TABLE_EXERCISE,
                null, null, null, null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                @SuppressLint("Range") int imageResource = cursor.getInt(cursor.getColumnIndex(DatabaseHelperGym.COLUMN_IMAGE_RESOURCE));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DatabaseHelperGym.COLUMN_NAME));
                @SuppressLint("Range") int duration = cursor.getInt(cursor.getColumnIndex(DatabaseHelperGym.COLUMN_DURATION));
                @SuppressLint("Range") int audioResource = cursor.getInt(cursor.getColumnIndex(DatabaseHelperGym.COLUMN_AUDIO_RESOURCE));

                Exercise exercise = new Exercise(imageResource, name, duration, audioResource);
                exercise.setId(id);
                exercises.add(exercise);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return exercises;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gymm, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        final Exercise exercise = exerciseList.get(position);
        holder.exerciseImage.setImageResource(exercise.getImageResource());
        holder.exerciseName.setText(exercise.getName());

        holder.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int duration = exercise.getDuration() * 1000; // Convert seconds to milliseconds

                // Disable the play button during playback
                holder.playButton.setEnabled(false);
                showCountdownDialog(holder.getContext(), duration);

                // Start the countdown timer
                startCountdownTimer(holder, duration, exercise);
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDurationDialog(exercise);
            }


        });
    }

    private void showEditDurationDialog(final Exercise exercise) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.edit_duration, null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        final EditText editDurationEditText = dialogView.findViewById(R.id.editDurationEditText);
        Button editDurationOKButton = dialogView.findViewById(R.id.editDurationOKButton);

        // Pre-fill the edit dialog with the current duration
        editDurationEditText.setText(String.valueOf(exercise.getDuration()));

        // Set a click listener for the "OK" button
        editDurationOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDurationText = editDurationEditText.getText().toString();
                if (!newDurationText.isEmpty()) {
                    int newDuration = Integer.parseInt(newDurationText);
                    // Update the exercise's duration
                    exercise.setDuration(newDuration);

                    // Notify the adapter that the data has changed
                    notifyDataSetChanged();

                    // Dismiss the dialog
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "Duration cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }



    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    private void showCountdownDialog(Context context, int duration) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.timer, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        final AlertDialog dialog = builder.create();
        dialog.show();

        final TextView countdownTimer = dialogView.findViewById(R.id.countdownTimer);
        countdownTimer.setText(formatTime(duration));

        new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownTimer.setText(formatTime(millisUntilFinished));
            }

            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
    }

    private String formatTime(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        ImageView exerciseImage;
        TextView exerciseName;
        Button playButton;
        Button editButton;

        ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseImage = itemView.findViewById(R.id.exerciseImage);
            exerciseName = itemView.findViewById(R.id.exerciseName);
            playButton = itemView.findViewById(R.id.playButton);
            editButton = itemView.findViewById(R.id.editButton);
        }

        // Add a method to access the context within the ViewHolder
        public Context getContext() {
            return itemView.getContext();
        }
    }

    private void startCountdownTimer(final ExerciseViewHolder holder, final int duration, final Exercise exercise) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(duration, 1000) {
            public void onTick(long millisUntilFinished) {
                int remainingTime = (int) (millisUntilFinished / 1000);
            }

            public void onFinish() {
                holder.playButton.setEnabled(true);
                playAudio(holder.getContext(), exercise.getAudioResource()); // Play audio here
            }
        }.start();
    }

    private void playAudio(Context context, int audioResource) {
        try {
            mediaPlayer.reset();
            String packageName = context.getPackageName();
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://" + packageName + "/" + audioResource));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
