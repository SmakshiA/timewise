package com.example.timewise;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainPreloader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preloader);
        ImageButton gym = findViewById(R.id.img1);
        ImageButton todo = findViewById(R.id.img2);
        ImageButton ex = findViewById(R.id.img3);
        ImageButton fin = findViewById(R.id.img4);
        ImageButton meditation = findViewById(R.id.img6);
        ImageButton jour = findViewById(R.id.img5);


        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPreloader.this, Finance.class);
                startActivity(intent);
            }
        });

        meditation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPreloader.this, Meditation.class);
                startActivity(intent);
            }
        });

        jour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPreloader.this, calender_journal.class);
                startActivity(intent);
            }
        });
        gym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainPreloader.this, gym.class);
                startActivity(intent1);
            }
        });

        todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPreloader.this, todolist.class);
                startActivity(intent);
            }
        });

        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent explicitIntent = new Intent(MainPreloader.this, MedicineR.class);
                startActivity(explicitIntent);
            }
        });
    }
}