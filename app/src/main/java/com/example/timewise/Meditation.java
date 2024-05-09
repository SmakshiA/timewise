package com.example.timewise;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Meditation extends AppCompatActivity {

    private EditText timeInput;
    private Button startButton;
    private Spinner spinner;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private int selectedAudioPosition = -1; // Initialize with an invalid position

    private TextView timeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation);

        timeInput = findViewById(R.id.timeInput);
        startButton = findViewById(R.id.startButton);
        spinner = findViewById(R.id.spinner);
        timeRemaining = findViewById(R.id.timeRemaining);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.audio_files, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    String timeText = timeInput.getText().toString();
                    if (!timeText.isEmpty() && selectedAudioPosition != -1) {
                        long timeInSeconds = Long.parseLong(timeText) * 1000;

                        playOrPauseAudio(selectedAudioPosition);

                        countDownTimer = new CountDownTimer(timeInSeconds, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                long secondsRemaining = millisUntilFinished / 1000;
                                long minutes = secondsRemaining / 60;
                                long seconds = secondsRemaining % 60;
                                String time = String.format("%02d:%02d", minutes, seconds);
                                timeRemaining.setText(time);
                            }

                            @Override
                            public void onFinish() {
                                timeRemaining.setText("TIME UP");
                                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                }
                                timeInput.setVisibility(View.GONE);
                                startButton.setText("Start");
                                isRunning = false;
                            }
                        }.start();

                        isRunning = true;
                        startButton.setText("Pause");
                    }
                } else {
                    countDownTimer.cancel();
                    playOrPauseAudio(selectedAudioPosition);
                    startButton.setText("Start");
                    isRunning = false;
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAudioPosition = position;
                if (isRunning) {
                    countDownTimer.cancel();
                    playOrPauseAudio(selectedAudioPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Not required to do anything here for audio selection
            }
        });
    }

    private void playOrPauseAudio(int selectedAudioPosition) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        } else {
            int audioResourceId = getAudioResourceId(selectedAudioPosition);
            mediaPlayer = MediaPlayer.create(this, audioResourceId);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    private int getAudioResourceId(int position) {
        switch (position) {
            case 0:
                return R.raw.lounge;
            case 1:
                return R.raw.nature;
            case 2:
                return R.raw.yoga;
            // Add more cases if needed
            default:
                return -1;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
